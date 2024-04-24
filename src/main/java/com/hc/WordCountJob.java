package com.hc;

import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @Author HaosionChiang
 * @Date 2021/6/20
 **/
public class WordCountJob {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        KafkaSource<String> source = KafkaSource.<String>builder()
                .setBootstrapServers("192.168.88.131:9092")
//                .setTopics("ODS_LOG_USERLOG")
//                .setGroupId("group_test_20231126")
                .setTopics("ODS_DB_BUSSINESS_DATA")
                .setGroupId("group_test_20231210")
                .setStartingOffsets(OffsetsInitializer.latest())
                .setValueOnlyDeserializer(new SimpleStringSchema())
                .build();

        DataStreamSource<String> kafka_source = env.fromSource(source, WatermarkStrategy.noWatermarks(), "Kafka Source");

        kafka_source.print();

//        kafka_source.keyBy(String::hashCode)
//                .process(new StateTimerFunction(3))
//                .print();


        env.execute("word count job");
    }

}
