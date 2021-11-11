package com.blcheung.missyou.manager.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

import java.text.MessageFormat;

/**
 * Redis消息监听配置
 *
 * @author BLCheung
 * @date 2021/11/11 10:59 下午
 */
@Configuration
public class RedisMessageListenerConfiguration {
    @Value("${redis.expired_pattern}")
    private String  expiredPattern;
    @Value("${redis.db.order_expired}")
    private Integer dbIndex;

    @Bean
    public RedisMessageListener redisMessageListener() {
        return new RedisMessageListener();
    }

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory factory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(factory);

        PatternTopic expiredTopic = this.assembleKeyEventExpiredTopic();

        container.addMessageListener(this.redisMessageListener(), expiredTopic);
        return container;
    }


    /**
     * 生成过期事件的监听主题
     *
     * @return org.springframework.data.redis.listener.PatternTopic
     * @author BLCheung
     * @date 2021/11/11 11:17 下午
     */
    private PatternTopic assembleKeyEventExpiredTopic() {
        String pattern = MessageFormat.format(this.expiredPattern, dbIndex);
        return new PatternTopic(pattern);
    }
}
