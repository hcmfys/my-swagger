package org.springbus.kfka;

import com.google.common.collect.Lists;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.PartitionInfo;
import org.jooq.meta.derby.sys.Sys;

import java.time.Duration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class KafKaClientTest {
 private  static  String kafkaUrl="localhost:9092";


    private  static   void getMsg(String topic) {
        Thread t=new Thread(() -> {
            Properties kafkaProperties = new Properties();
            kafkaProperties.put("bootstrap.servers", kafkaUrl);

            kafkaProperties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
            kafkaProperties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
            kafkaProperties.put("group.id", "log-group");

            KafkaConsumer  kafkaConsumer = new KafkaConsumer(kafkaProperties);
            Map<String, List<PartitionInfo>>   topicMap=kafkaConsumer.listTopics();
            System.out.println( topicMap);
            kafkaConsumer.subscribe(Lists.newArrayList(topic));
            while (true) {
                ConsumerRecords records=  kafkaConsumer.poll( Duration.ofSeconds(2000L));
                Iterator<ConsumerRecord> iterable=records.records(topic).iterator();
                while(iterable.hasNext()) {
                    ConsumerRecord record= iterable.next();
                    System.out.println(record.key()+"==>" +record.value());
                }

            }
        });
        t.start();

    }



    public static  void main(String[] args) throws InterruptedException {
        getMsg("test");


    }

}
