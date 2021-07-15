package com.blcheung.missyou.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

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
