package com.flinkpoint;

import cn.hutool.core.date.DateUtil;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

import java.time.Duration;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * 水位线的 what ，why，how
 *
 * 水位线生成策略、自定义实现、window的使用
 *
 * 窗口的限定范围是固定的，从1970-01-01 00:00:00 开始
 *
 * watermark=max(currenteventtime)-delaytime-1ms
 *
 *
 *
 * @Author HaosionChiang
 * @Date 2024/04/12
 **/
public class WindowWaterMarkJob {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        //设置1，是为了防止多并行度读取数据，有的并行度无数据，导致水位线无法推进
        env.setParallelism(1);

        DataStreamSource<String> source = env.socketTextStream("localhost", 9999);

        source
                .map(new MapFunction<String, Tuple2<String,Long>>() {
                    @Override
                    public Tuple2< String,Long> map(String s) throws Exception {
                        String[] split = s.split(",");
                        Tuple2<String, Long> of = Tuple2.of(split[0],
                                DateUtil.parseLocalDateTime(split[1], "yyyyMMdd HH:mm:ss")
                                .toEpochSecond(ZoneOffset.of("+8"))*1000
                        );
                        return of;
                    }
                })
                .assignTimestampsAndWatermarks(WatermarkStrategy
                        .<Tuple2<String,Long>>forBoundedOutOfOrderness(Duration.ofSeconds(2))
                        .withTimestampAssigner((e,v)->e.f1)
                )
                        .keyBy(e->e.f0)
                                .window(TumblingEventTimeWindows.of(Time.seconds(10)))
                                        .process(new ProcessWindowFunction<Tuple2<String, Long>, String, String, TimeWindow>() {
                                            @Override
                                            public void process(String s, ProcessWindowFunction<Tuple2<String, Long>, String, String, TimeWindow>.Context context, Iterable<Tuple2<String, Long>> iterable, Collector<String> collector) throws Exception {
                                                List<Tuple2<String, Long>> list=new ArrayList<>();
                                                for (Tuple2<String, Long> it : iterable) {
                                                    list.add(it);
                                                }
                                                collector.collect(s+","+list.size()+","+
                                                                new Date(context.window().getStart())
                                                        +","+new Date(context.window().getEnd()));
                                            }
                                        }).print();


        env.execute("word count job");
    }

}
