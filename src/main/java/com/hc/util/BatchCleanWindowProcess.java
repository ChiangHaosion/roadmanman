
package com.hc.util;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

public class BatchCleanWindowProcess<IN> extends ProcessWindowFunction<Tuple2<String, IN>, List<IN>, Tuple, TimeWindow> {
    public BatchCleanWindowProcess() {
    }

    public void process(Tuple tuple, ProcessWindowFunction<Tuple2<String, IN>, List<IN>, Tuple, TimeWindow>.Context context, Iterable<Tuple2<String, IN>> elements, Collector<List<IN>> out) throws Exception {
        List<IN> list = new ArrayList();
        elements.forEach((x) -> {
            list.add(x.f1);
        });
        if (CollectionUtils.isNotEmpty(list)) {
            out.collect(list);
        }

    }
}
