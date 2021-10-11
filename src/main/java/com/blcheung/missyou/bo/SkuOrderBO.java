package com.blcheung.missyou.bo;

import com.blcheung.missyou.dto.SkuDTO;
import com.blcheung.missyou.model.Sku;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class SkuOrderBO {
    private BigDecimal actualPrice;
    private Long       count;
    private Long       categoryId;

    public SkuOrderBO(Sku sku, SkuDTO skuDTO) {
        this.count       = skuDTO.getCount();
        this.categoryId  = sku.getCategoryId();
        this.actualPrice = sku.getActualPrice();
    }

    /**
     * 获取总价
     *
     * @return
     */
    public BigDecimal getTotalPrice() {
        return this.actualPrice.multiply(new BigDecimal(this.count));
    }
}
