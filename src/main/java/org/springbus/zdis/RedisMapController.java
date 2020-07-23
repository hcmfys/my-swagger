package org.springbus.zdis;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Random;
import java.util.Set;

@RestController
public class RedisMapController {

    private final RedisTemplate<String, String> redisTemplate;

    public RedisMapController(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @RequestMapping(value = "/addMap", method = RequestMethod.POST)
    public Set<String> addMap(String key,String hashKey,String value) {
        HashOperations<String,String,String> hashOperations = redisTemplate.opsForHash();
        Random r = new Random(System.currentTimeMillis());
        hashOperations.put(key,hashKey, value);
        return  hashOperations.keys(key);
    }

    @RequestMapping(value = "/getMap", method = RequestMethod.POST)
    public Map<String,String> getMap(String key) {
        HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();
        return hashOperations.entries(key);
    }

}
