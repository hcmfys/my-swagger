package org.springbus.zdis.pub;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PubController {

    private final RedisTemplate<String, String> redisTemplate;

    public PubController(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 使用 RedisTemplate 发送消息也非常简单，
     * 直接调用 convertAndSend 方法即可。
     *
     */
    @PostMapping("/sendChannel")
    public String send() {
        redisTemplate.convertAndSend("channel-name","这是来自RedisTemplate发布者的消息");
        return "send ok";
    }

}
