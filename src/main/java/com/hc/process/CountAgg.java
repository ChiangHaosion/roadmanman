package com.hc.process;

import com.hc.model.UserBehaviorData;
import org.apache.flink.api.common.functions.AggregateFunction;

public class CountAgg implements AggregateFunction<UserBehaviorData, Long, Long> {
    @Override
    public Long createAccumulator() {
        return 0L;
    }

    @Override
    public Long add(UserBehaviorData value, Long accumulator) {
        return accumulator + 1;
    }

    @Override
    public Long getResult(Long accumulator) {
        return accumulator;
    }

    @Override
    public Long merge(Long a, Long b) {
        return a + b;
    }
}
