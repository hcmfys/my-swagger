package org.springbus.binlog;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class KafKaListerner {

    @KafkaListener(  groupId = "maxwell-group_id",topics = "maxwell")

    public  void readLog(String logMsg) {
        log.info(" =============>\n{}",  logMsg);
    }
}
