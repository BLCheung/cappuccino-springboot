package com.blcheung.cappuccino.model;

import com.blcheung.cappuccino.bo.SkuOrderBO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor  // 无参构造函数，不加会导致反序列化失败，反序列化需要实例化对象，所以需要构造函数
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
