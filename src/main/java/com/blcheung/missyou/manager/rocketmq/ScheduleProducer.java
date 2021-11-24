package com.blcheung.missyou.manager.rocketmq;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import javax.annotation.PostConstruct;

/**
 * @author BLCheung
 * @date 2021/11/18 3:11 上午
 */
@Component
public class ScheduleProducer {
    private DefaultMQProducer defaultMQProducer;

    @Value("${rocketmq.namesrv_addr}")
    private String namesrvAddr;
    @Value("${rocketmq.producer.group}")
    private String producerGroup;
    @Value("${rocketmq.delay_level}")
    private int    delayTimeLevel;

    public ScheduleProducer() {}

    // 无参构造ScheduleProducer() -> 类内部的@Autowired,@Value的变量 ... -> @PostConstruct

    @PostConstruct  // 内部功能通过反射实现，并只会执行一次，并且会忽略方法的返回值，方法的权限任意，但不能是static
    private void initDefaultMQProducer() {
        if (ObjectUtils.isEmpty(this.defaultMQProducer)) {
            this.defaultMQProducer = new DefaultMQProducer(this.producerGroup);
            this.defaultMQProducer.setNamesrvAddr(this.namesrvAddr);
        }

        try {
            this.defaultMQProducer.start();
            System.out.println("------------ScheduleProducer start --------------");
        } catch (MQClientException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送一个延时消息
     *
     * @param topic
     * @param msg
     * @return java.lang.Boolean
     * @author BLCheung
     * @date 2021/11/18 11:55 下午
     */
    public Boolean send(String topic, String msg) {
        Message message = new Message(topic, msg.getBytes());
        message.setDelayTimeLevel(this.delayTimeLevel);

        try {
            SendResult result = this.defaultMQProducer.send(message);
            System.out.println("ScheduleProducer: " + result);
        } catch (MQClientException | RemotingException | MQBrokerException | InterruptedException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
