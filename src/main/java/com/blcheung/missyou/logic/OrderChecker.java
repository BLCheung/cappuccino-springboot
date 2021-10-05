package com.blcheung.missyou.logic;

import com.blcheung.missyou.dto.CreateOrderDTO;
import com.blcheung.missyou.dto.SkuDTO;
import com.blcheung.missyou.exception.http.ForbiddenException;
import com.blcheung.missyou.exception.http.NotFoundException;
import com.blcheung.missyou.exception.http.ParameterException;
import com.blcheung.missyou.model.Sku;
import com.blcheung.missyou.model.UserAddress;
import com.blcheung.missyou.repository.SkuRepository;
import com.blcheung.missyou.repository.UserAddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)   // 多例
public class OrderChecker {
    @Autowired
    private UserAddressRepository userAddressRepository;
    @Autowired
    private SkuRepository         skuRepository;
    @Value("${zbl.sku.limit_buy_count}")
    private Long                  limitBuyCount;

    public OrderChecker() {
    }

    public void isOK(CreateOrderDTO orderDTO) {
        // 前端Sku集合
        List<SkuDTO> skuDTOList = orderDTO.getSkuList();
        // 前端Sku ID集合
        List<Long> skuIds = skuDTOList.stream()
                                      .map(SkuDTO::getId)
                                      .collect(Collectors.toList());
        // 服务端Sku集合
        List<Sku> serverSkuList = this.skuRepository.findAllByIdIn(skuIds);

        // 是否存在对应Sku
        this.isSkuNotOnSale(skuIds.size(), serverSkuList.size());

        // 服务端订单总价
        BigDecimal totalServerPrice = new BigDecimal("0");

        for (int i = 0, length = serverSkuList.size(); i < length; i++) {
            Sku sku = serverSkuList.get(i);
            SkuDTO skuDTO = skuDTOList.get(i);
            // 是否无库存
            this.isOutOfStock(sku);
            // 是否超出购买数量
            this.isLimitMaxBuy(skuDTO, sku.getLimitBuyCount());
            // 累加总价
            totalServerPrice = totalServerPrice.add(this.calculateSkuItemPrice(sku, skuDTO));
        }

        // 校验优惠券相关
    }


    /**
     * 是否下架或不存在
     *
     * @param size
     * @param size1
     */
    private void isSkuNotOnSale(Integer size, Integer size1) {
        if (!size.equals(size1)) throw new ParameterException(70003);
        if (size1 == 0) throw new NotFoundException(70003);
    }

    /**
     * 是否库存不足
     *
     * @param sku
     */
    private void isOutOfStock(Sku sku) {
        if (sku.getStock() <= 0) throw new ParameterException(70004);
    }

    /**
     * 是否超过最大购买限制
     *
     * @param skuDTO
     * @param maxBuyCount
     */
    private void isLimitMaxBuy(SkuDTO skuDTO, Long maxBuyCount) {
        if (maxBuyCount != null || maxBuyCount >= 0) {
            if (skuDTO.getCount() >= maxBuyCount) throw new ParameterException(70005);
        } else {
            if (skuDTO.getCount() >= this.limitBuyCount) throw new ParameterException(70005);
        }
    }

    /**
     * 计算某个规格商品的总价
     *
     * @param sku
     * @param skuDTO
     * @return
     */
    private BigDecimal calculateSkuItemPrice(Sku sku, SkuDTO skuDTO) {
        if (skuDTO.getCount() <= 0) throw new ParameterException(70006);

        return sku.getActualPrice()
                  .multiply(new BigDecimal(skuDTO.getCount()));
    }
}
