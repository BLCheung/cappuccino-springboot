package com.blcheung.cappuccino.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
public class Category extends BaseEntity {
    @Id
    @GeneratedValue
    private Long    id;
    private String  name;
    private String  description;
    private Boolean isRoot;
    private Long    parentId;
    private String  img;
    private Long    index;
    private Boolean online;
    private Integer level;

    @ManyToMany(mappedBy = "categoryList", fetch = FetchType.LAZY)
    private List<Coupon> couponList;
}
