package com.blcheung.missyou.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 一个规格模型
 */
@Getter
@Setter
public class Spec {
    // 规格名id
    @JsonProperty("key_id") // 指定在数据库内的源字段，否则序列化时对应不上，数据为null
    private Long   keyId;
    // 规格名称
    private String key;
    // 规格值id
    @JsonProperty("value_id")
    private Long   valueId;
    // 规格值
    private String value;
}
