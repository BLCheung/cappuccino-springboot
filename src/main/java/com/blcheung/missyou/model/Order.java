package com.blcheung.missyou.model;

import com.blcheung.missyou.core.enumeration.OrderStatus;
import com.blcheung.missyou.util.CommonUtils;
import com.blcheung.missyou.util.GenericJSONConverter;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.*;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "`Order`")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Where(clause = "delete_time is null")
public class Order extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long       id;
    private String     orderNo;
    private Long       userId;
    private BigDecimal totalPrice;
    private BigDecimal finalTotalPrice;
    private Long       totalCount;
    private Date       expiredTime;
    private Date       placedTime;
    private Date       payTime;
    private String     snapImg;
    private String     snapTitle;
    private String     snapItems;
    private String     snapAddress;
    private String     prepayId;
    private Integer    status;
    private String     remark;
    private Boolean    payInExpired;    // 是否为超时后支付成功的订单

    /**
     * 该订单是否可以支付
     *
     * @return
     */
    public Boolean canPay() {
        if (!OrderStatus.UNPAID.equals(this.getStatusEnum())) return false;

        return CommonUtils.isInRangeDate(new Date(), this.getPlacedTime(), this.getExpiredTime());
    }

    public void setSnapItems(List<SkuOrder> skuOrderList) {
        if (skuOrderList.isEmpty()) return;
        this.snapItems = GenericJSONConverter.convertObjectToJSON(skuOrderList);
    }

    public List<SkuOrder> getSnapItems() {
        return GenericJSONConverter.convertJSONToObject(this.snapItems, new TypeReference<List<SkuOrder>>() {});
    }

    public OrderStatus getStatusEnum() {
        return OrderStatus.toType(this.getStatus())
                          .orElse(null);
    }
}
