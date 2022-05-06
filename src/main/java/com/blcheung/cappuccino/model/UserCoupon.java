package com.blcheung.cappuccino.model;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "user_coupon")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCoupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long    id;
    private Long    userId;
    private Long    couponId;
    private Long    orderId;
    private Integer status;
    private Date    createTime;
}
