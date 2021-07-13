package com.blcheung.missyou.model;

import lombok.Getter;
import lombok.Setter;

/**
 * 一个规格模型
 */
@Getter
@Setter
public class Spec {
    // 规格名id
    private Long   keyId;
    // 规格名称
    private String key;
    // 规格值id
    private Long   valueId;
    // 规格值
    private String value;
}
