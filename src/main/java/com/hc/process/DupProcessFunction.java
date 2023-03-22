package com.hc.process;

import org.apache.flink.api.common.state.StateTtlConfig;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.time.Time;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.util.Collector;

/**
 * @Author HaosionChiang
 * @Date 2022/9/9
 **/
public class DupProcessFunction extends KeyedProcessFunction<String, String, String> {


    private transient ValueState<Tuple2<Long, Long>> sum;


    @Override
    public void open(Configuration parameters) throws Exception {

        StateTtlConfig ttlConfig = StateTtlConfig
                .newBuilder(Time.seconds(60 * 60))
                .setUpdateType(StateTtlConfig.UpdateType.OnCreateAndWrite)
                .setStateVisibility(StateTtlConfig.StateVisibility.NeverReturnExpired)
                .build();

        ValueStateDescriptor<String> stateDescriptor = new ValueStateDescriptor<>("text state", String.class);
        stateDescriptor.enableTimeToLive(ttlConfig);

    }

    @Override
    public void processElement(String value, Context ctx, Collector<String> out) throws Exception {

    }
}
