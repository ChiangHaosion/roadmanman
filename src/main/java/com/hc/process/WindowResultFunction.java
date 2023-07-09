package com.hc.process;

import com.hc.model.UserViewCount;
import org.apache.flink.streaming.api.functions.windowing.WindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

public class WindowResultFunction implements WindowFunction<Long, UserViewCount, String, TimeWindow> {


    @Override
    public void apply(String key, TimeWindow window, Iterable<Long> input, Collector<UserViewCount> out) throws Exception {
        Long count = input.iterator().next();
        UserViewCount userViewCount = new UserViewCount(key, window.getEnd(), count);
        out.collect(userViewCount);
    }

}
