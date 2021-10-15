package com.blcheung.missyou.model;

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
public class Order {
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
    private String     snapImg;
    private String     snapTitle;
    private String     snapItems;
    private String     snapAddress;
    private Long       prepayId;
    private Integer    status;
    private String     remark;

    public void setSnapItems(List<SkuOrder> skuOrderList) {
        if (skuOrderList.isEmpty()) return;
        this.snapItems = GenericJSONConverter.convertObjectToJSON(skuOrderList);
    }

    public List<SkuOrder> getSnapItems() {
        return GenericJSONConverter.convertJSONToObject(this.snapItems, new TypeReference<List<SkuOrder>>() {});
    }
}
