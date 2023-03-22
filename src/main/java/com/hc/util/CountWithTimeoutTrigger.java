//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.hc.util;

import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.common.state.ReducingState;
import org.apache.flink.api.common.state.ReducingStateDescriptor;
import org.apache.flink.api.common.typeutils.base.LongSerializer;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.windowing.triggers.Trigger;
import org.apache.flink.streaming.api.windowing.triggers.TriggerResult;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;

public class CountWithTimeoutTrigger<T> extends Trigger<T, TimeWindow> {
    private int maxSize;
    private TimeCharacteristic timeType;
    private ReducingStateDescriptor<Long> countStateDescriptor;

    public CountWithTimeoutTrigger(int maxSize, TimeCharacteristic timeType) {
        this.countStateDescriptor = new ReducingStateDescriptor("counter", new Sum(), LongSerializer.INSTANCE);
        this.maxSize = maxSize;
        this.timeType = timeType;
    }

    private TriggerResult fireAndPurge(TimeWindow window, Trigger.TriggerContext ctx) throws Exception {
        this.clear(window, ctx);
        return TriggerResult.FIRE_AND_PURGE;
    }

    public TriggerResult onElement(T element, long timestamp, TimeWindow window, Trigger.TriggerContext ctx) throws Exception {
        ReducingState<Long> countState = (ReducingState)ctx.getPartitionedState(this.countStateDescriptor);
        countState.add(1L);
        if ((Long)countState.get() >= (long)this.maxSize) {
            return this.fireAndPurge(window, ctx);
        } else {
            return timestamp >= window.getEnd() ? this.fireAndPurge(window, ctx) : TriggerResult.CONTINUE;
        }
    }

    public TriggerResult onProcessingTime(long time, TimeWindow window, Trigger.TriggerContext ctx) throws Exception {
        if (this.timeType != TimeCharacteristic.ProcessingTime) {
            return TriggerResult.CONTINUE;
        } else {
            return time >= window.getEnd() ? TriggerResult.CONTINUE : this.fireAndPurge(window, ctx);
        }
    }

    public TriggerResult onEventTime(long time, TimeWindow window, Trigger.TriggerContext ctx) throws Exception {
        if (this.timeType != TimeCharacteristic.EventTime) {
            return TriggerResult.CONTINUE;
        } else {
            return time >= window.getEnd() ? TriggerResult.CONTINUE : this.fireAndPurge(window, ctx);
        }
    }

    public void clear(TimeWindow window, Trigger.TriggerContext ctx) throws Exception {
        ReducingState<Long> countState = (ReducingState)ctx.getPartitionedState(this.countStateDescriptor);
        countState.clear();
    }

    class Sum implements ReduceFunction<Long> {
        Sum() {
        }

        public Long reduce(Long value1, Long value2) throws Exception {
            return value1 + value2;
        }
    }
}
