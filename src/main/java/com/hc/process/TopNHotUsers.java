package com.hc.process;

import com.hc.model.UserViewCount;
import org.apache.flink.api.common.state.ListState;
import org.apache.flink.api.common.state.ListStateDescriptor;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.util.Collector;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TopNHotUsers extends KeyedProcessFunction<Long, UserViewCount, String> {

    private int topSize;
    private ListState<UserViewCount> userViewCountListState;

    public TopNHotUsers(int topSize) {
        this.topSize = topSize;
    }

    @Override
    public void onTimer(long timestamp, OnTimerContext ctx, Collector<String> out) throws Exception {
        super.onTimer(timestamp, ctx, out);
        List<UserViewCount> userViewCounts = new ArrayList<>();
        for(UserViewCount userViewCount : userViewCountListState.get()) {
            userViewCounts.add(userViewCount);
        }

        userViewCountListState.clear();

        userViewCounts.sort(new Comparator<UserViewCount>() {
            @Override
            public int compare(UserViewCount o1, UserViewCount o2) {
                return (int)(o2.getViewCount() - o1.getViewCount());
            }
        });

        // 将排名信息格式化成 String, 便于打印
        StringBuilder result = new StringBuilder();
        result.append("====================================\n");
        result.append("时间: ").append(new Timestamp(timestamp-1)).append("\n");
        for (int i = 0; i < topSize; i++) {
            UserViewCount currentItem = userViewCounts.get(i);
            // No1:  商品ID=12224  浏览量=2413
            result.append("No").append(i).append(":")
                    .append("  用户名=").append(currentItem.getUserName())
                    .append("  浏览量=").append(currentItem.getViewCount())
                    .append("\n");
        }
        result.append("====================================\n\n");

        Thread.sleep(1000);

        out.collect(result.toString());

    }

    @Override
    public void open(org.apache.flink.configuration.Configuration parameters) throws Exception {
        super.open(parameters);
        ListStateDescriptor<UserViewCount> userViewCountListStateDescriptor = new ListStateDescriptor<>(
                "user-state",
                UserViewCount.class
        );
        userViewCountListState = getRuntimeContext().getListState(userViewCountListStateDescriptor);

    }

    @Override
    public void processElement(UserViewCount value, Context ctx, Collector<String> out) throws Exception {
        userViewCountListState.add(value);
        ctx.timerService().registerEventTimeTimer(value.getWindowEnd() + 1000);
    }
}
