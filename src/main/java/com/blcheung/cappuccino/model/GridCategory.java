package com.blcheung.cappuccino.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class GridCategory extends BaseEntity {
    @Id
    @GeneratedValue
    private Long   id;
    private String title;
    private String img;
    private String name;
    private Long   categoryId;
    private Long   rootCategoryId;

}
