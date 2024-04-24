package com.flinkpoint;

import cn.hutool.core.date.DateUtil;
import com.flinkpoint.entity.StationLog;
import com.flinkpoint.entity.UserEntity;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.api.common.state.ReadOnlyBroadcastState;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.BroadcastStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.co.BroadcastProcessFunction;
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
 * 状态的分类， Flink管理、用户管理
 * <p>
 * Flink管理-> 键控 算子 广播
 * <p>
 * 状态是Flink重要的机制、是容错的基石，业务里可以用于缓存的场景
 * <p>
 * 广播状态可以动态更新规则
 * <p>
 * <p>
 * 案例：读取Socket1中基站日志数据形成数据主流，读取Socket2中用户信息形成广播流，两者进行关联获取基站通话详细信息。
 *
 * @Author HaosionChiang
 * @Date 2024/04/19
 **/
public class StateBroadcastJob {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        env.setParallelism(1);

        DataStreamSource<String> mainDs = env.socketTextStream("localhost", 9999);
        DataStreamSource<String> broadDs = env.socketTextStream("localhost", 8888);


        //序列化用户点击实体
        SingleOutputStreamOperator<StationLog> stationLogMapDs = mainDs.flatMap(new FlatMapFunction<String, StationLog>() {
            @Override
            public void flatMap(String s, Collector<StationLog> collector) throws Exception {
                collector.collect(new StationLog(s.split(",")[0],
                        s.split(",")[1], s.split(",")[2], s.split(",")[3],
                        Long.parseLong(s.split(",")[4]),
                        Long.parseLong(s.split(",")[5])
                ));
            }
        });
        // 序列化用户信息实体
        SingleOutputStreamOperator<UserEntity> userInfoDs = broadDs.flatMap(new FlatMapFunction<String, UserEntity>() {
            @Override
            public void flatMap(String s, Collector<UserEntity> collector) throws Exception {
                collector.collect(new UserEntity(s.split(",")[0], s.split(",")[1]
                ));
            }
        });
        // 定义mapState描述器
        MapStateDescriptor<String, UserEntity> stateDescriptor = new MapStateDescriptor<>("userinfo", String.class, UserEntity.class);

        // 广播用户流
        BroadcastStream<UserEntity> broadcastDs = userInfoDs.broadcast(stateDescriptor);

//        主流connect广播流

        stationLogMapDs.connect(broadcastDs)
                .process(new BroadcastProcessFunction<StationLog, UserEntity, String>() {
                    @Override
                    public void processElement(StationLog stationLog, BroadcastProcessFunction<StationLog, UserEntity, String>.ReadOnlyContext readOnlyContext, Collector<String> collector) throws Exception {
                        ReadOnlyBroadcastState<String, UserEntity> broadcastState = readOnlyContext.getBroadcastState(stateDescriptor);

                        UserEntity userEntity = broadcastState.get(stationLog.getId());
                        if (userEntity!=null){
                            collector.collect("用户ID：" + userEntity.getUid()+"Name："+userEntity.getName() + "\t日志LogId：" + stationLog.getId());
                        }

                    }

                    @Override
                    public void processBroadcastElement(UserEntity userEntity, BroadcastProcessFunction<StationLog, UserEntity, String>.Context context, Collector<String> collector) throws Exception {
                        context.getBroadcastState(stateDescriptor).put(userEntity.getUid(), userEntity);
                    }
                }).print();
//        打印


        env.execute("StateBroadcastJob");
    }

}
