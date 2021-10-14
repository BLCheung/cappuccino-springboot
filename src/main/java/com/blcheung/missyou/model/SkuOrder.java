package com.blcheung.missyou.model;

import com.blcheung.missyou.bo.SkuOrderBO;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
public class SkuOrder {
    private Long         id;
    private Long         spuId;
    private BigDecimal   totalPrice;
    private BigDecimal   price;
    private Long         count;
    private String       title;
    private String       img;
    private List<String> specValues;

    public SkuOrder(Sku sku, SkuOrderBO skuOrderBO) {
        this.id         = sku.getId();
        this.spuId      = sku.getSpuId();
        this.totalPrice = skuOrderBO.getTotalPrice();
        this.price      = skuOrderBO.getActualPrice();
        this.count      = skuOrderBO.getCount();
        this.title      = sku.getTitle();
        this.img        = sku.getImg();
        this.specValues = sku.getSpecValues();
    }
}
