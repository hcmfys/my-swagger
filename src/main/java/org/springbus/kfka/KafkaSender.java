package org.springbus.kfka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;
import java.util.concurrent.Future;

public class KafkaSender {

    private  static  String kafkaUrl="localhost:9092";

    public static  void main(String[] args) throws InterruptedException {

        senMsg("test","this is 测试");


    }
    private  static   void senMsg(String topic,String msg) {

        Properties kafkaProperties = new Properties();
        kafkaProperties.put("bootstrap.servers", kafkaUrl);
        kafkaProperties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        kafkaProperties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        KafkaProducer producer = new KafkaProducer(kafkaProperties);
        ProducerRecord record = new ProducerRecord(topic, "name", msg);
        try {
            Future result = producer.send(record);
            System.out.println(result.get());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
