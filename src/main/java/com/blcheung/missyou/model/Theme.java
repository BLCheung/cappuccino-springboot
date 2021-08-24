package com.blcheung.missyou.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
public class Theme extends BaseEntity {
    @Id
    @GeneratedValue
    private Long    id;
    private String  title;
    private String  description;
    private String  name;
    private String  tplName;
    private String  entranceImg;
    private String  extend;
    private String  internalTopImg;
    private String  titleImg;
    private Boolean online;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "theme_spu",
               joinColumns = @JoinColumn(name = "theme_id"),
               inverseJoinColumns = @JoinColumn(name = "spu_id"))
    private List<Spu> spuList;
}
