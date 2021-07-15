package com.blcheung.missyou.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Getter
@Setter
public class Category {
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
}
