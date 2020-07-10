package org.springbus.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

//@Component
@Slf4j
public class DemoListener {

  @KafkaListener(id = "a", topics = "my-topic-3",groupId = "my-id")
  public void readMsg(String msg) {
    log.info(" ===  my-id group ===get kafka1---> msg ={}", msg);
    }

 // @KafkaListener(id = "阿", topics = "my-topic")
  public void readMsg2(String msg) {
    log.info(" =====get kafka2 msg ={}", msg);
  }


}
