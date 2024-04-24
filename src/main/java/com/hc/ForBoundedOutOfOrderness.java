package com.hc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

//在assignTimestampsAndWatermarks中用WatermarkStrategy.forBoundedOutOfOrderness方法抽取Timestamp和生成周期性水位线示例
public class ForBoundedOutOfOrderness {

    public static void main(String[] args) throws  Exception{
        //创建流处理环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        //设置周期生成Watermark间隔(10毫秒）
        env.getConfig().setAutoWatermarkInterval(10L);
        //并行度1
        env.setParallelism(1);
        //演示数据
        DataStreamSource<ClickEvent> mySource = env.fromElements(
                new ClickEvent(LocalDateTime.now(), "user1", 1L, 1),
                new ClickEvent(LocalDateTime.now(), "user1", 2L, 2),
                new ClickEvent(LocalDateTime.now(), "user1", 3L, 3),
                new ClickEvent(LocalDateTime.now(), "user1", 4L, 4),
                new ClickEvent(LocalDateTime.now(), "user1", 5L, 5),
                new ClickEvent(LocalDateTime.now(), "user1", 6L, 6),
                new ClickEvent(LocalDateTime.now(), "user1", 7L, 7),
                new ClickEvent(LocalDateTime.now(), "user1", 8L, 8)
        );
        //WatermarkStrategy.forBoundedOutOfOrderness周期性生成水位线
        //可更好处理延迟数据
        //BoundedOutOfOrdernessWatermarks<T>实现WatermarkGenerator<T>
        SingleOutputStreamOperator<ClickEvent> streamTS = mySource.assignTimestampsAndWatermarks(
                //指定Watermark生成策略，最大延迟长度5毫秒
                WatermarkStrategy.<ClickEvent>forBoundedOutOfOrderness(Duration.ofMillis(5))
                        .withTimestampAssigner(
                                //SerializableTimestampAssigner接口中实现了extractTimestamp方法来指定如何从事件数据中抽取时间戳
                                new SerializableTimestampAssigner<ClickEvent>() {
                                    @Override
                                    public long extractTimestamp(ClickEvent event, long recordTimestamp) {
                                        return event.getDateTime(event.getEventTime());
                                    }
                                })
        );
        //结果打印
        streamTS.print();
        env.execute();
    }
    @Data
    static class ClickEvent{
        private String user;
        private long l;
        private int i;
        private LocalDateTime eventTime;
        public ClickEvent(LocalDateTime eventTime, String user, long l, int i) {
            this.eventTime = eventTime;
            this.user = user;
            this.l = l;
            this.i = i;
        }
        public long getDateTime(LocalDateTime dt) {
            ZoneOffset zoneOffset8 = ZoneOffset.of("+8");
            return dt.toInstant(zoneOffset8).toEpochMilli();
        }
    }
}

