package com.blcheung.cappuccino.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class BannerItem extends BaseEntity {
    @Id
    @GeneratedValue
    private Long id;
    private String img;
    private String keyword;
    private Short type;
    // 当前banner_item表的外键与banner表中的id关联
    private Long bannerId;
    private String name;
}
