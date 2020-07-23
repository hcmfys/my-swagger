package org.springbus.zdis;

import org.jooq.meta.derby.sys.Sys;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;
import java.util.Set;
@RestController
public class RedisZsetController {
    private final RedisTemplate<String, String> redisTemplate;

    public RedisZsetController(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @RequestMapping(value = "/addZset10000", method = RequestMethod.POST)
    public Set<String> addZset10000(String key) {
        ZSetOperations<String, String> zSetOperations = redisTemplate.opsForZSet();
        Random r=new Random(System.currentTimeMillis());
        for(int i=0;i<1000000;i++) {
            long score = r.nextInt(10000000);
            zSetOperations.add(key, "mx"+i, score);
        }
        return zSetOperations.range(key, 0, 1000);
    }

    @RequestMapping(value = "/addZset", method = RequestMethod.POST)
    public Set<String> addZset(String key,String value,double score) {
        ZSetOperations<String,String> zSetOperations = redisTemplate.opsForZSet();
        zSetOperations.add(key, value,score);
        return zSetOperations.range(key, 0, -1);
    }

    @RequestMapping(value = "/incrementScore", method = RequestMethod.POST)
    public Set<ZSetOperations.TypedTuple<String>> incrementScore(String key,String value,int delta) {
        ZSetOperations<String, String> zSetOperations = redisTemplate.opsForZSet();
        zSetOperations.incrementScore(key, value, delta);

        return zSetOperations.reverseRangeByScoreWithScores(key, 0, Double.MAX_VALUE);
    }

    @RequestMapping(value = "/reverseRangeByScoreWithScores", method = RequestMethod.POST)
    public Set<ZSetOperations.TypedTuple<String>> reverseRange(String key) {
        ZSetOperations<String,String> zSetOperations = redisTemplate.opsForZSet();

        return zSetOperations.reverseRangeByScoreWithScores(key, 0, Double.MAX_VALUE);
    }

    @RequestMapping(value = "/rangeWithScores", method = RequestMethod.POST)
    public Set<ZSetOperations.TypedTuple<String>>  rangeWithScores(String key) {
        ZSetOperations<String,String> zSetOperations = redisTemplate.opsForZSet();
        return zSetOperations.rangeWithScores(key, 0, -1);
    }

    @RequestMapping(value = "/rangeWithMaxScores", method = RequestMethod.POST)
    public Set<ZSetOperations.TypedTuple<String>>  rangeWithMaxScores(String key) {
        ZSetOperations<String,String> zSetOperations = redisTemplate.opsForZSet();
        return zSetOperations.rangeWithScores(key, -1, -1);
    }

    @RequestMapping(value = "/rangeWithMinScores", method = RequestMethod.POST)
    public Set<ZSetOperations.TypedTuple<String>>  rangeWithMinScores(String key) {
        ZSetOperations<String,String> zSetOperations = redisTemplate.opsForZSet();
        return zSetOperations.rangeWithScores(key, 0, 0);
    }

    @RequestMapping(value = "/zCard", method = RequestMethod.POST)
    public  Long  zCard(String key) {
        ZSetOperations<String,String> zSetOperations = redisTemplate.opsForZSet();
        return zSetOperations.zCard(key );
    }

}
