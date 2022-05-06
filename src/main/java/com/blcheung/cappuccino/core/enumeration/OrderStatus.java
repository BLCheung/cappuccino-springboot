package com.blcheung.cappuccino.core.enumeration;

import lombok.Getter;

import java.util.Optional;
import java.util.stream.Stream;

public enum OrderStatus {
    ALL(0, "全部"),
    UNPAID(1, "待支付"),
    PAID(2, "已支付"),
    DELIVERED(3, "已发货"),
    FINISHED(4, "已完成"),
    CANCELED(5, "已取消");

    @Getter
    private Integer value;

    OrderStatus(Integer value, String desc) {
        this.value = value;
    }

    public static Optional<OrderStatus> toType(Integer value) {
        return Stream.of(OrderStatus.values())
                     .filter(orderStatus -> orderStatus.getValue()
                                                       .equals(value))
                     .findAny();
    }

}
