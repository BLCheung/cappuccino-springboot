package com.blcheung.missyou.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Getter
@Setter
public class SaleExplain extends BaseEntity {
    @Id
    @GeneratedValue
    private Long    id;
    private Boolean fixed;
    private String  text;
    private Long    spuId;
    private Integer index;
    private Long    replaceId;
}
