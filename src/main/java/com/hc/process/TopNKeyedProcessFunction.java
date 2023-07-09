package com.hc.process;

import com.hc.model.UserViewCount;
import org.apache.flink.api.common.state.ListState;
import org.apache.flink.api.common.state.ListStateDescriptor;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.util.Collector;

import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.stream.Collectors;

/**
 * @Author HaosionChiang
 * @Date 2023/7/8
 **/
public class TopNKeyedProcessFunction extends KeyedProcessFunction<Long, UserViewCount, String> {
    private int n;

    private ListState<UserViewCount> state;

    public TopNKeyedProcessFunction(int n) {
        this.n = n;
    }

    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);
        ListStateDescriptor<UserViewCount> userViewCountListStateDescriptor = new ListStateDescriptor<>(
                "state",
                UserViewCount.class
        );
        state = getRuntimeContext().getListState(userViewCountListStateDescriptor);
    }

    @Override
    public void processElement(UserViewCount userViewCount, KeyedProcessFunction<Long, UserViewCount, String>.Context context, Collector<String> collector) throws Exception {
        //每来一条数据, 将其加入状态
        //然后注册一个定时器,当前窗口的结束时间+1触发
        state.add(userViewCount);
        context.timerService().registerEventTimeTimer(context.getCurrentKey()+1);
    }

    @Override
    public void onTimer(long timestamp, KeyedProcessFunction<Long, UserViewCount, String>.OnTimerContext ctx, Collector<String> out) throws Exception {
        super.onTimer(timestamp, ctx, out);
        PriorityQueue<UserViewCount> queue = new PriorityQueue<>(new Comparator<UserViewCount>() {
            @Override
            public int compare(UserViewCount o1, UserViewCount o2) {
                return (int) (o2.getViewCount() - o1.getViewCount());
            }
        });
        //排序
        for (UserViewCount userViewCount : state.get()) {
           queue.add(userViewCount);
        }

        //封装输出

        StringBuilder sb = new StringBuilder();
        sb.append("----窗口结束时间-----").append(ctx.getCurrentKey()).append("\n");
        for (int i = 0; i < n; i++) {
            if (!queue.isEmpty()){
                UserViewCount peek = queue.peek();
                sb.append("用户名:").append(peek.getUserName()).append("\t").append("访问量:").append(peek.getViewCount()).append("\n");
                queue.poll();
            }
        }
        sb.append("-------------------\n");

        out.collect(sb.toString());

    }
}
