package com.blcheung.cappuccino.model;

import com.blcheung.cappuccino.util.GenericJSONConverter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@Where(clause = "delete_time is null and online = 1")
public class Sku extends BaseEntity {
    @Id
    @GeneratedValue
    private Long       id;
    private BigDecimal price;
    private BigDecimal discountPrice;
    private Boolean    online;
    private String     img;
    private String     title;
    private Long       spuId;
    //    @Convert(converter = ListJSONConverter.class)
    //    private List<Object> specs;
    //    @Convert(converter = MapJSONConverter.class)    // 调用一个特定的转换器转换该字段
    //    private Map<String, Object> test;
    private String     specs;
    private String     code;
    private Long       stock;
    private Long       limitBuyCount;
    private Long       categoryId;
    private Long       rootCategoryId;

    public List<Spec> getSpecs() {
        if (this.specs == null) return Collections.emptyList();
        //        return GenericJSONConverter.convertJSONToList(this.specs);
        //        return GenericJSONConverter.convertJSONToList(this.specs, new TypeReference<List<Spec>>() {});
        return GenericJSONConverter.convertJSONToObject(this.specs, new TypeReference<List<Spec>>() {});
    }

    public void setSpecs(List<Spec> specs) {
        if (specs.isEmpty()) return;
        this.specs = GenericJSONConverter.convertObjectToJSON(specs);
    }

    /**
     * 获取Sku的实际价格
     *
     * @return
     */
    public BigDecimal getActualPrice() {
        return this.discountPrice != null ? this.discountPrice : this.price;
    }

    /**
     * 获取规格值名称数组
     *
     * @return
     */
    @JsonIgnore // 不需要传到前端
    public List<String> getSpecValues() {
        return this.getSpecs()
                   .stream()
                   .map(Spec::getValue)
                   .collect(Collectors.toList());
    }
}
