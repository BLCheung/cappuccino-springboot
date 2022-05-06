package com.blcheung.cappuccino.service;

import com.blcheung.cappuccino.bo.SkuOrderBO;
import com.blcheung.cappuccino.constant.Macro;
import com.blcheung.cappuccino.core.enumeration.OrderStatus;
import com.blcheung.cappuccino.dto.CreateOrderDTO;
import com.blcheung.cappuccino.dto.OrderPagingDTO;
import com.blcheung.cappuccino.dto.SkuDTO;
import com.blcheung.cappuccino.exception.http.ForbiddenException;
import com.blcheung.cappuccino.exception.http.NotFoundException;
import com.blcheung.cappuccino.exception.http.ParameterException;
import com.blcheung.cappuccino.kit.LocalUserKit;
import com.blcheung.cappuccino.kit.PagingKit;
import com.blcheung.cappuccino.kit.ResultKit;
import com.blcheung.cappuccino.logic.CouponChecker;
import com.blcheung.cappuccino.logic.OrderChecker;
import com.blcheung.cappuccino.model.*;
import com.blcheung.cappuccino.repository.OrderRepository;
import com.blcheung.cappuccino.repository.SkuRepository;
import com.blcheung.cappuccino.repository.UserCouponRepository;
import com.blcheung.cappuccino.util.CommonUtils;
import com.blcheung.cappuccino.vo.OrderDetailVO;
import com.blcheung.cappuccino.vo.OrderPagingVO;
import com.blcheung.cappuccino.vo.PagingResultDozer;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class OrderService {
    @Autowired
    private SkuRepository        skuRepository;
    @Autowired
    private OrderRepository      orderRepository;
    @Autowired
    private UserCouponRepository userCouponRepository;
    @Autowired
    private UserService          userService;
    @Autowired
    private CouponService        couponService;
    @Autowired
    private OrderChecker         orderChecker;
    @Autowired
    private CouponChecker        couponChecker;
    @Value("${zbl.order.limit_pay_time}")
    private Long                 limitPayTime;
    @Autowired
    private StringRedisTemplate  stringRedisTemplate;
    // @Autowired
    // private ScheduleProducer     scheduleProducer;
    @Value("${rocketmq.topic.order}")
    private String               orderTopic;

    @Transactional  // 加上事务，同时对多张表进行增删查改时确保多张表能正确同步，一旦出错能够同时回滚所有事务
    public Long createOrder(CreateOrderDTO orderDTO) {
        User user = LocalUserKit.getUser();
        // 用户地址
        UserAddress userAddress = this.userService.getUserAddressById(user.getId(), orderDTO.getAddressId());

        // 前端Sku集合
        List<SkuDTO> skuDTOList = orderDTO.getSkuList();
        List<Long> skuIds = skuDTOList.stream()
                                      .map(SkuDTO::getId)
                                      .collect(Collectors.toList());
        // 服务端Sku集合
        List<Sku> serverSkuList = this.skuRepository.findAllByIdIn(skuIds);

        // 是否存在对应Sku
        this.orderChecker.isSkuNotOnSale(serverSkuList.size(), skuIds.size());

        // 订单总价
        BigDecimal finalOrderTotalPrice;

        finalOrderTotalPrice = this.calcOrderTotalPrice(serverSkuList, skuDTOList, orderDTO);
        // 订单总价是否合法
        this.orderChecker.isFinalTotalServerPriceOK(finalOrderTotalPrice);

        // 构建订单
        String orderNo = CommonUtils.makeOrderNo();
        Order order = Order.builder()
                           .orderNo(orderNo)
                           .userId(user.getId())
                           .totalPrice(this.orderChecker.getOrderTotalPrice())
                           .finalTotalPrice(finalOrderTotalPrice)
                           .totalCount(this.orderChecker.getOrderTotalCount())
                           .snapAddress(userAddress.getSnapAddress())
                           .snapTitle(this.orderChecker.getOrderPrimaryTitle())
                           .snapImg(this.orderChecker.getOrderPrimaryImg())
                           .placedTime(new Date())
                           .expiredTime(CommonUtils.getFutureDateWithSecond(this.limitPayTime))
                           .status(OrderStatus.UNPAID.getValue())
                           .build();
        order.setSnapItems(this.orderChecker.getSkuOrderList());
        this.orderRepository.save(order);

        // 扣减库存
        this.reduceStock(this.orderChecker.getSkuOrderList());

        // 核销优惠券
        String couponIds = String.valueOf(Macro.Fail);
        if (orderDTO.getCouponIds() != null && !orderDTO.getCouponIds()
                                                        .isEmpty()) {
            couponIds = StringUtils.join(orderDTO.getCouponIds(), ",");
            this.writeOffCoupon(user.getId(), order.getId(), orderDTO.getCouponIds());
        }

        // 加入到延时消息队列
        this.save2MessageQueue(user.getId(), order.getId(), couponIds);

        return order.getId();
    }

    /**
     * 获取订单列表
     *
     * @param orderPagingDTO
     * @return
     */
    public PagingResultDozer<Order, OrderPagingVO> getOrderList(OrderPagingDTO orderPagingDTO) {
        OrderStatus orderStatus = OrderStatus.toType(orderPagingDTO.getStatus())
                                             .orElseThrow(() -> new ParameterException(70012));
        User user = LocalUserKit.getUser();

        Pageable pageable = PagingKit.buildLatest(orderPagingDTO);
        Page<Order> orderPaging;

        switch (orderStatus) {
            case ALL:   // 全部
                orderPaging = this.orderRepository.findByUserId(user.getId(), pageable);
                break;
            case UNPAID:    // 待支付，单独查询，因为单靠status查询可能不准确
                orderPaging = this.orderRepository.findByUserIdAndStatusAndExpiredTimeGreaterThan(user.getId(),
                                                                                                  orderStatus.getValue(),
                                                                                                  new Date(), pageable);
                break;
            default:
                orderPaging = this.orderRepository.findByUserIdAndStatus(user.getId(), orderStatus.getValue(),
                                                                         pageable);
                break;
        }

        PagingResultDozer<Order, OrderPagingVO> pagingResultDozer = new PagingResultDozer<>(orderPaging,
                                                                                            OrderPagingVO.class);
        // noinspection unchecked
        pagingResultDozer.getList()
                         .forEach((orderPagingVO) -> ( (OrderPagingVO) orderPagingVO ).setLimitPayTime(
                                 this.limitPayTime));
        return pagingResultDozer;
    }

    /**
     * 获取订单详情
     *
     * @param orderId
     * @return
     */
    public OrderDetailVO getOrderDetail(Long orderId) {
        User user = LocalUserKit.getUser();

        Optional<Order> orderOptional = this.orderRepository.findFirstByUserIdAndId(user.getId(), orderId);
        Order order = orderOptional.orElseThrow(() -> new NotFoundException(70001));

        return new OrderDetailVO(order, this.limitPayTime);
    }

    /**
     * 保存订单的微信订单id
     *
     * @param orderId
     * @param prepayId
     */
    public void updateOrderPrepayId(Long orderId, String prepayId) {
        User user = LocalUserKit.getUser();
        Order order = this.orderRepository.findFirstByUserIdAndId(user.getId(), orderId)
                                          .orElseThrow(() -> new ParameterException(10007));
        order.setPrepayId(prepayId);
        this.orderRepository.save(order);
    }


    /**
     * 计算订单总价
     *
     * @param serverSkuList
     * @param skuDTOList
     * @param orderDTO
     * @return
     */
    private BigDecimal calcOrderTotalPrice(List<Sku> serverSkuList, List<SkuDTO> skuDTOList, CreateOrderDTO orderDTO) {
        // 服务端订单总价
        BigDecimal finalTotalServerPrice = new BigDecimal("0");
        // 订单所有sku业务对象集合
        List<SkuOrderBO> skuOrderBOList = new ArrayList<>();
        // 订单sku模型对象集合
        List<SkuOrder> skuOrderList = new ArrayList<>();

        Sku sku;
        SkuDTO skuDTO;
        for (int i = 0; i < serverSkuList.size(); i++) {
            sku    = serverSkuList.get(i);
            skuDTO = skuDTOList.get(i);
            // 是否售罄或库存不足
            this.orderChecker.isOutOfStock(sku, skuDTO);
            // 是否超出最大购买限制
            this.orderChecker.isLimitMaxBuy(skuDTO, sku.getLimitBuyCount());

            SkuOrderBO skuOrderBO = new SkuOrderBO(sku, skuDTO);
            SkuOrder skuOrder = new SkuOrder(sku, skuOrderBO);
            skuOrderBOList.add(skuOrderBO);
            skuOrderList.add(skuOrder);
            finalTotalServerPrice = finalTotalServerPrice.add(skuOrderBO.getTotalPrice());
        }

        // 保存进orderChecker
        this.orderChecker.setSkuOrderList(skuOrderList);

        if (orderDTO.getCouponIds() != null && !orderDTO.getCouponIds()
                                                        .isEmpty()) {
            finalTotalServerPrice = this.calcOrderCouponTotalPrice(orderDTO.getCouponIds(), skuOrderBOList,
                                                                   finalTotalServerPrice);
        }

        return finalTotalServerPrice;
    }

    /**
     * 计算含有优惠券的订单总价
     *
     * @param couponIds
     * @param skuOrderBOList
     * @param finalTotalServerPrice
     * @return
     */
    private BigDecimal calcOrderCouponTotalPrice(List<Long> couponIds, List<SkuOrderBO> skuOrderBOList,
                                                 BigDecimal finalTotalServerPrice) {
        // 最终经过优惠的订单总价
        BigDecimal finalOrderCouponTotalPrice = null;
        // 当前用户是否拥有某张优惠券
        List<Coupon> userCouponList = this.couponService.getUserAllAvailableCouponIn(couponIds);
        if (userCouponList.isEmpty()) throw new NotFoundException(20011);
        if (couponIds.size() != userCouponList.size()) throw new NotFoundException(20011);

        this.couponChecker.hasSameCategory(userCouponList);
        this.couponChecker.isExpired(userCouponList);

        finalOrderCouponTotalPrice = this.couponChecker.calc(userCouponList, skuOrderBOList, finalTotalServerPrice);

        return finalOrderCouponTotalPrice;
    }

    /**
     * 扣减库存
     *
     * @param skuOrderList
     */
    private void reduceStock(List<SkuOrder> skuOrderList) {
        for (SkuOrder skuOrder : skuOrderList) {
            int result = this.skuRepository.reduceStock(skuOrder.getId(), skuOrder.getCount());
            if (result != Macro.OK) throw new ForbiddenException(70004);
        }
    }

    /**
     * 核销优惠券
     *
     * @param userId
     * @param orderId
     * @param couponIds
     */
    private void writeOffCoupon(Long userId, Long orderId, List<Long> couponIds) {
        for (Long couponId : couponIds) {
            int result = this.userCouponRepository.writeOff(userId, couponId, orderId);
            if (result != Macro.OK) throw new ForbiddenException(50006);
        }
    }

    private void save2MessageQueue(Long uid, Long oid, String cids) {
        String orderKey = uid + "&" + oid + "&" + cids;
        this.add2Redis(orderKey);
        // this.add2Producer(orderKey);
    }


    /**
     * 加入到rocketMQ生产者延时任务
     *
     * @param orderKey
     * @author BLCheung
     * @date 2021/11/23 1:30 上午
     */
    private void add2Producer(String orderKey) {
        // this.scheduleProducer.send(this.orderTopic, orderKey);
    }

    /**
     * 加入到redis
     *
     * @param orderKey
     * @author BLCheung
     * @date 2021/11/23 1:31 上午
     */
    private void add2Redis(String orderKey) {
        try {
            stringRedisTemplate.opsForValue()
                               .set(orderKey, String.valueOf(Macro.OK), this.limitPayTime, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
            // 有可能redis宕机或没开启
            ResultKit.reject("redis出现异常，或未开启");
        }
    }
}
