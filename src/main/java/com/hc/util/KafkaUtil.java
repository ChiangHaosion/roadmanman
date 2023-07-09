package com.hc.util;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;


/**
 * 往kafka 发送数据
 */
public class KafkaUtil {

    public static final String broker_list = "localhost:9092";
    public static final String topic = "test";

    public static void sendToKafka(String json) {
        Properties props = new Properties();
        props.put("bootstrap.servers", broker_list);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer"); //key 序列化
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer"); //value 序列化
        KafkaProducer<String, String> producer = new KafkaProducer<>(props);
        ProducerRecord<String, String> record = new ProducerRecord<>(topic, null, null, json);
        producer.send(record);
        System.out.println("发送数据: " + json);
        producer.flush();

    }
}
