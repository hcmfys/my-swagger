package org.springbus.zdis;

import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RedisListController {

    private final RedisTemplate<String, String> redisTemplate;

    public RedisListController(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @RequestMapping(value = "/leftPush", method = RequestMethod.POST)
    public List<String> leftPush(String key, String value) {
        ListOperations<String,String> listOperations = redisTemplate.opsForList();

        listOperations.leftPush(key, value);
        return   listOperations.range(key, 0, -1);
    }

    @RequestMapping(value = "/rightPush", method = RequestMethod.POST)
    public  List<String> rightPush(String key, String value) {
        ListOperations<String, String> listOperations = redisTemplate.opsForList();
        listOperations.rightPush(key, value);
        return listOperations.range(key, 0, -1);
    }

    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    public  List<String> remove(String key, long count,String value) {
        ListOperations<String, String> listOperations = redisTemplate.opsForList();
        listOperations.remove(key,count, value);
        return listOperations.range(key, 0, -1);
    }

    @RequestMapping(value = "/size", method = RequestMethod.POST)
    public  long  size(String key) {
        ListOperations<String, String> listOperations = redisTemplate.opsForList();
        return listOperations.size(key);

    }
}
