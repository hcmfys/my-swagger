package org.springbus.zdis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
public class RedisSetController {
    @Autowired
    private RedisTemplate<String,String>  redisTemplate;


    @RequestMapping(value = "/addSet",method = RequestMethod.POST)
    public Set<String> addSet(String key, String value) {
        redisTemplate.opsForSet().add(key, value);
        return redisTemplate.opsForSet().members(key);
    }

    @RequestMapping(value = "/randomMember",method = RequestMethod.POST)
    public  String  randomMember(String key, String value) {

        return redisTemplate.opsForSet().randomMember(key);
    }

    @RequestMapping(value = "/pop",method = RequestMethod.POST)
    public  String  pop(String key) {

        return redisTemplate.opsForSet().pop(key);
    }

    @RequestMapping(value = "/popCount",method = RequestMethod.POST)
    public List<String> popCount(String key, long count) {

        return redisTemplate.opsForSet().pop(key,count);
    }

}
