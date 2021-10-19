package com.blcheung.missyou.vo;

import com.blcheung.missyou.model.SkuOrder;
import com.blcheung.missyou.util.GenericJSONConverter;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class OrderPagingVO {
    private Long       id;
    private String     orderNo;
    private Long       userId;
    private BigDecimal totalPrice;
    private BigDecimal finalTotalPrice;
    private Long       totalCount;
    private Date       expiredTime;
    private Date       placedTime;
    private String     snapImg;
    private String     snapTitle;
    private String     snapItems;
    private Long       prepayId;
    private Integer    status;
    private Long       limitPayTime;

    public void setSnapItems(List<SkuOrder> skuOrderList) {
        if (skuOrderList.isEmpty()) return;
        this.snapItems = GenericJSONConverter.convertObjectToJSON(skuOrderList);
    }

    public List<SkuOrder> getSnapItems() {
        return GenericJSONConverter.convertJSONToObject(this.snapItems, new TypeReference<List<SkuOrder>>() {});
    }
}
