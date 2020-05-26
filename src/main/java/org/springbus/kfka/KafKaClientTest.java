package org.springbus.kfka;

import com.google.common.collect.Lists;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Iterator;
import java.util.Properties;

public class KafKaClientTest {
 private  static  String kafkaUrl="localhost:9092";


    private  static   void getMsg(String topic) {
        Thread t=new Thread(new Runnable(){

            @Override
            public void run() {
                Properties kafkaProperties = new Properties();
                kafkaProperties.put("bootstrap.servers", kafkaUrl);

                kafkaProperties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
                kafkaProperties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
                kafkaProperties.put("group.id", "log-group");

                KafkaConsumer  kafkaConsumer = new KafkaConsumer(kafkaProperties);

                kafkaConsumer.subscribe(Lists.newArrayList(topic));
                while (true) {
                    ConsumerRecords records=  kafkaConsumer.poll(1000);
                    Iterator<ConsumerRecord> iterable=records.records(topic).iterator();
                    while(iterable.hasNext()) {
                        ConsumerRecord record= iterable.next();
                        System.out.println(record.key()+"==>" +record.value());
                    }

                }
            }
        });
        t.start();

    }



    public static  void main(String[] args) throws InterruptedException {
        getMsg("test");


    }

}
