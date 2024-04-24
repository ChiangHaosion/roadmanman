package com.temp;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

import java.time.Duration;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @Author HaosionChiang
 * @Date 2024/04/12
 **/
public class windowtemp {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        //设置1，是为了防止多并行度读取数据，有的并行度无数据，导致水位线无法推进
        env.setParallelism(2);

        DataStreamSource<String> source = env.socketTextStream("localhost", 9999);

        source
                .map(new MapFunction<String, Tuple2<String,Long>>() {
                    @Override
                    public Tuple2< String,Long> map(String s) throws Exception {
                        return Tuple2.of(s, RandomUtil.randomLong());
                    }
                })
                        .keyBy(e->e.f0)
                                .window(TumblingProcessingTimeWindows.of(Time.minutes(1)))
                                        .process(new ProcessWindowFunction<Tuple2<String, Long>, String, String, TimeWindow>() {
                                            @Override
                                            public void process(String s, ProcessWindowFunction<Tuple2<String, Long>, String, String, TimeWindow>.Context context, Iterable<Tuple2<String, Long>> iterable, Collector<String> collector) throws Exception {
                                                List<Tuple2<String, Long>> list=new ArrayList<>();
                                                for (Tuple2<String, Long> it : iterable) {
                                                    list.add(it);
                                                }
                                                collector.collect(s+","+list.size()+","+
                                                        context.window().hashCode()+"\t"+
                                                                new Date(context.window().getStart())
                                                        +","+new Date(context.window().getEnd()));
                                            }
                                        }).print();


        env.execute("word count job");
    }

}
