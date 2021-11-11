package com.blcheung.missyou.manager.redis;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

/**
 * Redis消息监听
 *
 * @author BLCheung
 * @date 2021/11/11 9:37 下午
 */
public class RedisMessageListener implements MessageListener {
    @Override
    public void onMessage(Message message, byte[] bytes) {
        String body = new String(message.getBody());
        String channel = new String(message.getChannel());

        System.out.println("body:" + body);
        System.out.println("channel:" + channel);
    }
}
