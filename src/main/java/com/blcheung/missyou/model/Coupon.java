package com.blcheung.missyou.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;

@Entity
@Getter
@Setter
public class Coupon extends BaseEntity {
    @Id
    @GeneratedValue
    private Long       id;
    private Long       activityId;
    private String     title;
    private Date       startTime;
    private Date       endTime;
    private String     description;
    private BigDecimal fullMoney;
    private BigDecimal minus;
    private BigDecimal rate;
    private Integer    type;
    private String     remark;
    // 是否全场通用优惠券
    private Boolean    wholeStore;
}
