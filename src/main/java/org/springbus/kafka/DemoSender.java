package org.springbus.kafka;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class DemoSender {

    @Autowired
    private KafkaTemplate kafkaTemplate;

  @RequestMapping(value = "/send", method = RequestMethod.GET)
  public void testDemo() throws InterruptedException {
    kafkaTemplate.send("my-topic", "this is my first demo");
    // 休眠5秒，为了使监听器有足够的时间监听到topic的数据
    log.info("====>OK==========");
    }
}
