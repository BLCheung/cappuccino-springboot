package com.blcheung.cappuccino.manager.rocketmq;

import com.blcheung.cappuccino.bo.OrderMessageQueueBO;
import com.blcheung.cappuccino.service.OrderCancelService;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;

/**
 * @author BLCheung
 * @date 2021/11/19 11:48 下午
 */
// @Component
public class ScheduleConsumer implements CommandLineRunner { // 作用类似@PostConstruct，当前实体加入到容器后会回调run方法
    @Value("${rocketmq.namesrv_addr}")
    private String namesrvAddr;
    @Value("${rocketmq.consumer.group}")
    private String consumerGroup;
    @Value("${rocketmq.topic.order}")
    private String orderTopic;

    @Autowired
    private OrderCancelService orderCancelService;

    private void consumerMessageListener() {
        // 消费者
        DefaultMQPushConsumer defaultMQPushConsumer = new DefaultMQPushConsumer(this.consumerGroup);
        // 配置路由地址，跟生产者同
        defaultMQPushConsumer.setNamesrvAddr(this.namesrvAddr);
        // 一次消费多少条消息
        defaultMQPushConsumer.setConsumeMessageBatchMaxSize(1);
        // 监听从生产者处产生的消息队列
        defaultMQPushConsumer.registerMessageListener(
                (MessageListenerConcurrently) (messageExtList, consumeConcurrentlyContext) -> {

                    for (Message message : messageExtList) {
                        String body = new String(message.getBody());
                        System.out.println(
                                "ScheduleConsumer：" + "{topic: " + message.getTopic() + ", body: " + body + "}");
                        if (StringUtils.equals(this.orderTopic, message.getTopic())) this.cancelOrder(body);
                    }

                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                });

        try {
            // 订阅对应topic的消息
            defaultMQPushConsumer.subscribe(this.orderTopic, "*");
            defaultMQPushConsumer.start();
        } catch (MQClientException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run(String... args) throws Exception {
        this.consumerMessageListener();
    }

    private void cancelOrder(String body) {
        OrderMessageQueueBO orderMessageQueueBO = new OrderMessageQueueBO(body);
        this.orderCancelService.cancel(orderMessageQueueBO);
    }
}
