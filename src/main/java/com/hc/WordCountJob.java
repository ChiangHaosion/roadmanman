package com.hc;

import com.hc.process.StateTimerFunction;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.SlidingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.util.Collector;

import java.util.HashSet;

/**
 * hdfs  http://192.168.88.100:9870
 * yarn http://192.168.88.100:8088/cluster
 *
 * @Author HaosionChiang
 * @Date 2021/6/20
 **/
public class WordCountJob {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        DataStreamSource<String> source = env.socketTextStream("192.168.88.100", 9999);

//        KafkaSource<String> source = KafkaSource.<String>builder()
//                .setBootstrapServers("192.168.88.130:9092")
//                .setTopics("input-topic")
//                .setGroupId("my-group")
//                .setStartingOffsets(OffsetsInitializer.latest())
//                .setValueOnlyDeserializer(new SimpleStringSchema())
//                .build();
//
//        DataStreamSource<String> kafka_source = env.fromSource(source, WatermarkStrategy.noWatermarks(), "Kafka Source");

        env.setParallelism(2);

        source.keyBy(String::hashCode)
                .process(new StateTimerFunction(3))
                .print();


        env.execute("word count job");
    }

//    @RestController
//    @RequestMapping("flink")
//    public class FlinkController {
//        @RequestMapping(value = "/submit", method = RequestMethod.POST)
//        public void submit() throws Exception {
//            String[] jars = {"data_clean_flink-service-1.0-SNAPSHOT-kafka.jar"};
//            StreamExecutionEnvironment env = StreamExecutionEnvironment.createRemoteEnvironment("192.168.11.11", 8081, 2, jars);
//            Properties properties = new Properties();
//            properties.setProperty("bootstrap.servers", "192.168.11.12:9092");
//            properties.setProperty("group.id", "flinksink");
//            stream.map(new MapFunction<String, String>() {
//                private static final long serialVersionUID = -6867736771747690202L;
//                @Override
//                public String map(String value) throws Exception {
//                    System.out.println(value);
//                    return value;
//                }
//            }).print();
//            env.execute();
//        }
//    }
}
