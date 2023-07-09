package com.hc;

import com.alibaba.fastjson2.JSON;
import com.hc.model.UserBehaviorData;
import com.hc.model.UserViewCount;
import com.hc.process.CountAgg;
import com.hc.process.TopNKeyedProcessFunction;
import com.hc.process.WindowResultFunction;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.SlidingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.util.Collector;

import java.time.Duration;
import java.util.Properties;

/**
 * @Author HaosionChiang
 * @Date 2023/7/7
 **/
public class TopNJob {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("zookeeper.connect", "localhost:2181");
        props.put("group.id", "metric-group");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("auto.offset.reset", "latest");

        DataStreamSource<String> dataStreamSource = env.addSource(new FlinkKafkaConsumer<>(
                "test",  //kafka topic
                new SimpleStringSchema(),  // String 序列化
                props).setStartFromLatest()
        ).setParallelism(1);

        SingleOutputStreamOperator<UserBehaviorData> userBehaviorDs = dataStreamSource.flatMap(new FlatMapFunction<String, UserBehaviorData>() {
            @Override
            public void flatMap(String s, Collector<UserBehaviorData> collector) throws Exception {
                collector.collect(JSON.parseObject(s, UserBehaviorData.class));
            }
        })
                .assignTimestampsAndWatermarks(WatermarkStrategy.<UserBehaviorData>forBoundedOutOfOrderness(Duration.ofSeconds(1))
                        .withTimestampAssigner(new SerializableTimestampAssigner<UserBehaviorData>() {
                            @Override
                            public long extractTimestamp(UserBehaviorData userBehaviorData, long l) {
                                return userBehaviorData.getTimestamp();
                            }
                        }));


        //增量聚合
        //agg统计窗口中的条数，即遇到一条数据就加一。
        //将每个key窗口聚合后的结果带上其他信息进行输出。
        SingleOutputStreamOperator<UserViewCount> aggregateDs = userBehaviorDs.keyBy(new KeySelector<UserBehaviorData, String>() {
                    @Override
                    public String getKey(UserBehaviorData userBehaviorData) throws Exception {
                        return userBehaviorData.getUserId();
                    }
                }).window(TumblingEventTimeWindows.of(Time.seconds(10)))
                .aggregate(new CountAgg(), new WindowResultFunction());
        //按照窗口统计后的数据, 排序和输出
        aggregateDs.keyBy(UserViewCount::getWindowEnd)
                .process(new TopNKeyedProcessFunction(3)).print();

        env.execute();

    }
}
