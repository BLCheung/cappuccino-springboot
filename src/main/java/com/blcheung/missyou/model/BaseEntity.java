package com.blcheung.missyou.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public abstract class BaseEntity {
    private Date createTime;
    private Date updateTime;
    private Date deleteTime;
}
