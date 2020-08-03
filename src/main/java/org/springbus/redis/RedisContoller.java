package org.springbus.redis;

import org.apache.kafka.common.protocol.types.Field;
import org.jooq.meta.derby.sys.Sys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Set;

@RestController
public class RedisContoller {
    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    String key="acc:user";

    @RequestMapping("/ra")
    public Set<String> a(){
       SetOperations<String, String >  opers=redisTemplate.opsForSet();
        for(int i=0;i<1000000;i++){
        opers.add(key, "index"+ i);
        }
        return opers.members(key);
    }
    @RequestMapping("/rb")
    public Set<String> b(){
     Set<String> st=new HashSet<>();


       // Cursor<String> cursor = redisTemplate.opsForSet().scan(key
         //       , ScanOptions.scanOptions().match("c").count(10).build());
       // while (cursor.hasNext()){
           // String object = cursor.next();
            //System.out.println("通过scan(K key, ScanOptions options)方法获取匹配的值:" + object);
          //  st.add(object);
      //  }
     boolean i=   redisTemplate.opsForSet().isMember(key, "index100");
        System.out.println("--size="+st.size() +" --" +i);

        return st;


    }



}
