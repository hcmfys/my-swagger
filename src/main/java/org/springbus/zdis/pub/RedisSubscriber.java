package org.springbus.zdis.pub;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.stereotype.Component;

/**
 * @Description: 监听redis通道
 */
@Component
public class RedisSubscriber extends MessageListenerAdapter {

    private final RedisTemplate<String, String> redisTemplate;

    public RedisSubscriber(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void onMessage(Message message,byte[] pattern) {
        System.out.println("通道----->" + redisTemplate.getKeySerializer().deserialize(message.getChannel()));
        System.out.println("消息----->" + redisTemplate.getValueSerializer().deserialize(message.getBody()));
    }
}

