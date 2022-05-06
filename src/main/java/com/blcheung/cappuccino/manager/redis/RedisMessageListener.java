package com.blcheung.cappuccino.manager.redis;

import com.blcheung.cappuccino.bo.OrderMessageQueueBO;
import com.blcheung.cappuccino.service.OrderCancelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

/**
 * Redis消息监听
 *
 * @author BLCheung
 * @date 2021/11/11 9:37 下午
 */
public class RedisMessageListener implements MessageListener {
    @Autowired
    private OrderCancelService orderCancelService;

    @Override
    public void onMessage(Message message, byte[] bytes) {
        String body = new String(message.getBody());
        String channel = new String(message.getChannel());

        System.out.println("body:" + body);
        System.out.println("channel:" + channel);

        OrderMessageQueueBO messageBO = new OrderMessageQueueBO(body);
        this.orderCancelService.cancel(messageBO);
    }
}
