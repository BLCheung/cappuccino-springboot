package com.blcheung.missyou.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "`Order`")
@Getter
@Setter
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long       id;
    private String     orderNo;
    private Long       userId;
    private BigDecimal totalPrice;
    private BigDecimal finalTotalPrice;
    private Long       totalCount;
    private Date       expiredTime;
    private Date       placedTime;
    private String     snapImg;
    private String     snapTitle;
    private String     snapItems;
    private String     snapAddress;
    private Long       prepayId;
    private Integer    status;
}
