package com.hc;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.hc.model.UserBehavior;
import com.hc.source.MySqlSource;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.util.Collector;

/**
 * @Author HaosionChiang
 * @Date 2021/6/20
 **/
public class UserJob {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        //单线程读取数据
        DataStreamSource<UserBehavior> dataSource = env.addSource(new MySqlSource()).setParallelism(1);
        DataStream<UserBehavior> ds=dataSource.flatMap(new FlatMapFunction<UserBehavior, UserBehavior>() {
                    @Override
                    public void flatMap(UserBehavior userBehavior, Collector<UserBehavior> collector) throws Exception {
                        if (StringUtils.isNotBlank(userBehavior.getTimestamps())) {
                            DateTime date = DateUtil.date(Long.parseLong(userBehavior.getTimestamps()) * 1000);
                            userBehavior.setTimestamps(date.toString("yyyy-MM-dd HH:mm:ss"));
                            collector.collect(userBehavior);
                        }
                    }
                }).setParallelism(1);

        SingleOutputStreamOperator<Tuple2<String, Integer>> usrDs = ds.map(new MapFunction<UserBehavior, Tuple2<String, Integer>>() {
            @Override
            public Tuple2<String, Integer> map(UserBehavior value) throws Exception {
                return Tuple2.of(value.getUserId(), 1);
            }
        });

        usrDs.keyBy(t->t.f0).timeWindow(Time.seconds(10))
                .sum(1).print();


        env.execute("userBehaviorJob");
    }
}
