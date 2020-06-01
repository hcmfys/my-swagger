package org.springbus.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;

@Configuration
public class KafkaConfig {

    private final KafkaTemplate kafkaTemplate;

    @Autowired
    public KafkaConfig(KafkaTemplate kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }
}
