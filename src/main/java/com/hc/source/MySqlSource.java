package com.hc.source;

import com.hc.model.UserBehavior;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.source.RichParallelSourceFunction;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * @Author HaosionChiang
 * @Date 2021/6/20
 **/
public class MySqlSource extends RichParallelSourceFunction<UserBehavior> {
    private boolean flag = true;
    private Connection conn = null;
    private PreparedStatement ps = null;
    private ResultSet rs = null;

    @Override
    public void open(Configuration parameters) throws Exception {
        Class.forName("com.mysql.jdbc.Driver");
        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai", "root", "admin");
    }

    @Override
    public void close() throws Exception {
        if (conn != null) {
            conn.close();
        }
        if (ps != null) {
            ps.close();
        }
        if (rs != null) {
            rs.close();
        }
    }

    @Override
    public void run(SourceContext<UserBehavior> context) throws Exception {
        int i=0;
        while (flag) {
            ps = conn.prepareStatement("select * from userbehavior limit "+i+",50");
            i=i+50;
            rs = ps.executeQuery();
            while (rs.next()) {
                String uid = rs.getString("user_id");
                String pid = rs.getString("product_id");
                String cid = rs.getString("catalog_id");
                String actionType = rs.getString("action_type");
                String timestamps = rs.getString("timestamps");
                context.collect(new UserBehavior(uid, pid, cid, actionType, timestamps));
            }
            Thread.sleep(3000);
        }
    }

    @Override
    public void cancel() {
        flag = false;
    }
}
