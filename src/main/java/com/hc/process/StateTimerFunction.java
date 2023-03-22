package com.hc.process;

import org.apache.flink.api.common.state.*;
import org.apache.flink.api.common.time.Time;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.util.Collector;

public class StateTimerFunction extends KeyedProcessFunction<Integer, String, String> {

    private MapState<String,String> mapCacheStorage;
    private ValueState<String> valCacheStorage;


    private int TTL_SEC;

    public StateTimerFunction(int TTL_MINUTE) {
        this.TTL_SEC = TTL_MINUTE;
    }

    public void open(Configuration parameters) throws Exception {
//        MapStateDescriptor<String, String> descriptor = new MapStateDescriptor<>("CacheStorage", String.class,String.class);

        ValueStateDescriptor<String> valStateDescriptor = new ValueStateDescriptor<>("valState", String.class);

        StateTtlConfig ttl = StateTtlConfig
                .newBuilder(Time.seconds(5))
                .setUpdateType(StateTtlConfig.UpdateType.OnCreateAndWrite)
                .setStateVisibility(StateTtlConfig.StateVisibility.NeverReturnExpired)
                .build();
        //valStateDescriptor.enableTimeToLive(ttl);
        valStateDescriptor.enableTimeToLive(ttl);
//        mapCacheStorage = getRuntimeContext().getMapState(descriptor);

        valCacheStorage = getRuntimeContext().getState(valStateDescriptor);
    }

    @Override
    public void processElement(String bytes, Context context, Collector<String> collector) throws Exception {
        try {
//
//            if (mapCacheStorage.get(bytes)!=null){
//                return;
//            }
//            mapCacheStorage.put(bytes,bytes);
            context.timerService().registerProcessingTimeTimer(System.currentTimeMillis() + TTL_SEC *1000);
            if (valCacheStorage.value()==null){
                valCacheStorage.update(bytes);
                collector.collect(bytes);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    //到达时间点触发事件操作
    public void onTimer(long timestamp, OnTimerContext ctx, Collector<String> out) throws Exception {
        // 查询数据库
        if (valCacheStorage.value()!=null){

            out.collect(valCacheStorage.value().concat("selectDB"));
        }
    }
}
