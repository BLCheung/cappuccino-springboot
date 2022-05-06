package com.blcheung.cappuccino.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.MappedSuperclass;
import java.util.Date;

@Getter
@Setter
@MappedSuperclass   // 当前不是数据库映射表的类，而是其中的基类，因为有Entity注解的类继承了该类
public abstract class BaseEntity {
    @JsonIgnore
    private Date createTime;
    @JsonIgnore
    private Date updateTime;
    @JsonIgnore
    private Date deleteTime;
}
