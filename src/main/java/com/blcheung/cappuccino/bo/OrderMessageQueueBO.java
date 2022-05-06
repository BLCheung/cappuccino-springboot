package com.blcheung.cappuccino.bo;

import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author BLCheung
 * @date 2021/11/12 12:38 上午
 */
@Getter
@Setter
public class OrderMessageQueueBO {
    private Long       userId;
    private Long       orderId;
    private List<Long> couponIds;
    private String     message;

    public OrderMessageQueueBO(String message) {
        this.message = message;
        this.parseMessage(this.message);
    }

    private void parseMessage(String message) {
        String[] temps = message.split("&");
        this.userId    = Long.valueOf(temps[ 0 ]);
        this.orderId   = Long.valueOf(temps[ 1 ]);
        this.couponIds = Arrays.stream(temps[ 2 ].split(","))
                               .map(Long::valueOf)
                               .collect(Collectors.toList());
        // List<Long> list = new ArrayList<>();
        // for (String s : temps[ 2 ].split(",")) {
        //     list.add(Long.valueOf(s));
        // }
        // this.couponIds = list;
    }
}
