
package com.hc.util;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.util.Collector;

public class WindowCleanDataStream {

    public static <IN> DataStream<List<IN>> toDataStreamList(DataStream<IN> inDataStream) {
        DataStream<Tuple2<String, IN>> baseDataStream = inDataStream.flatMap(new FlatMapFunction<IN, Tuple2<String, IN>>() {
            public void flatMap(IN value, Collector<Tuple2<String, IN>> out) throws Exception {
                if (value != null) {
                    int random = ThreadLocalRandom.current().nextInt(128);
                    String hash = String.valueOf(random);

                    out.collect(Tuple2.of(hash, value));
                }

            }
        });
        @SuppressWarnings("unchecked")
        DataStream<List<IN>> dataResultStream = baseDataStream.keyBy(new int[]{0}).timeWindow(Time.seconds(10L))
                .trigger(new CountWithTimeoutTrigger<>(100, TimeCharacteristic.ProcessingTime))
                .process(new BatchCleanWindowProcess());
        return dataResultStream;
    }

    public static <IN> DataStream<List<IN>> toDataStreamList(DataStream<IN> inDataStream, Time size, int maxSize) {
        DataStream<Tuple2<String, IN>> baseDataStream = inDataStream.flatMap(new FlatMapFunction<IN, Tuple2<String, IN>>() {
            public void flatMap(IN value, Collector<Tuple2<String, IN>> out) throws Exception {
                if (value != null) {
                    int random = ThreadLocalRandom.current().nextInt(128);
                    String hash = String.valueOf(random);

                    out.collect(Tuple2.of(hash, value));
                }

            }
        });
        @SuppressWarnings("unchecked")
        DataStream<List<IN>> dataResultStream = baseDataStream.keyBy(new int[]{0}).timeWindow(size)
                .trigger(new CountWithTimeoutTrigger<>(maxSize, TimeCharacteristic.ProcessingTime))
                .process(new BatchCleanWindowProcess());
        return dataResultStream;
    }
}
