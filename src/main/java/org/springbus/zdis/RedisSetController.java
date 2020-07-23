package org.springbus.zdis;

import com.alibaba.druid.util.StringUtils;
import io.lettuce.core.ScanCursor;
import io.lettuce.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ExecutionException;

@RestController
public class RedisSetController {

    private final RedisTemplate<String, String> redisTemplate;

    public RedisSetController(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


    @RequestMapping(value = "/addSet1W", method = RequestMethod.POST)
    public Set<String> addSet1W(String key) {
        SetOperations setOperations = redisTemplate.opsForSet();
        Random r = new Random(System.currentTimeMillis());
        for (int i = 0; i < 42; i++) {
            setOperations.add(key, r.nextInt(10000) + "&index");
        }
        return setOperations.members(key);
    }

    @RequestMapping(value = "/addSet", method = RequestMethod.POST)
    public Set<String> addSet(String key, String value) {
        redisTemplate.opsForSet().add(key, value);
        return redisTemplate.opsForSet().members(key);
    }

    @RequestMapping(value = "/randomMember", method = RequestMethod.POST)
    public String randomMember(String key, String value) {

        return redisTemplate.opsForSet().randomMember(key);
    }

    @RequestMapping(value = "/pop", method = RequestMethod.POST)
    public String pop(String key) {

        return redisTemplate.opsForSet().pop(key);
    }

    @RequestMapping(value = "/popCount", method = RequestMethod.POST)
    public List<String> popCount(String key, long count) {

        return redisTemplate.opsForSet().pop(key, count);
    }

    @RequestMapping(value = "/scan", method = RequestMethod.POST)
    public List<String> scan(String key, long count) throws IOException {
        ScanOptions scanOptions = ScanOptions.scanOptions().count(20).build();
        List<String> myList = new ArrayList<>();
        Cursor<String> cursor = redisTemplate.opsForSet().scan(key, scanOptions);

        if (cursor.hasNext()) {
            myList.add(cursor.next() + "---cursorId=" + cursor.getCursorId() + " posId=" + cursor.getPosition());


        }
        cursor.close();
        return myList;
    }
    @RequestMapping(value = "/scanPage", method = RequestMethod.POST)
    public List<String>  scanPage(String key) throws Exception {
        ScanOptions scanOptions = ScanOptions.scanOptions().count(10).build();

        List<String> myList = new ArrayList<>();
        RedisSerializer<String> keySerializer = (RedisSerializer<String>) redisTemplate.getKeySerializer();
        long curPosId = -1;
        try (Cursor<byte[]> cursor = redisTemplate.execute((RedisConnection connection) ->
                connection.sScan(keySerializer.serialize(key),scanOptions), true)) {
            while (cursor != null && cursor.hasNext()) {
                long posId = cursor.getCursorId();
                System.out.println("posId =  " + posId);
                byte[] rawKey = cursor.next();
                if (curPosId == -1) {
                    curPosId = cursor.getCursorId();
                }
                if (curPosId != cursor.getCursorId()) {
                    break;
                }
                myList.add(keySerializer.deserialize(rawKey));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return myList;
    }
    @RequestMapping(value = "/scanKey", method = RequestMethod.POST)
    public DataItem<String> scanKey(String key,String cursorId) {
        DataItem dataItem = new DataItem();
        List<String> retList = new ArrayList<>();
        dataItem.setDataList(retList);
        if (StringUtils.isEmpty(cursorId)) {
            cursorId = "0";
        }

        String destCursorId = cursorId;
        redisTemplate.execute((RedisCallback<Set<byte[]>>) connection -> {
            Object obj = connection.getNativeConnection();
            RedisSerializer<String> keySerializer = (RedisSerializer<String>) redisTemplate.getKeySerializer();
            AbstractRedisAsyncCommands<byte[], byte[]> commands = (AbstractRedisAsyncCommands) obj;
            ScanCursor cursor = new ScanCursor(destCursorId, false);
            ScanArgs scanArgs = ScanArgs.Builder.limit(20);
            RedisFuture<ValueScanCursor<byte[]>> vv = commands.sscan(keySerializer.serialize(key),
                    cursor, scanArgs);
            try {
                ValueScanCursor<byte[]> result = vv.get();

                for (byte[] bb : result.getValues()) {
                    retList.add(keySerializer.deserialize(bb));
                }

                ScanCursor resultCursor=new ScanCursor();
                resultCursor.setCursor(result.getCursor());
                resultCursor.setFinished(result.isFinished());
                dataItem.setCursor(resultCursor);
                System.out.println ( result.getCursor() +"-"+ result.isFinished()  );
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            return null;
        });

        return dataItem;
    }


}
