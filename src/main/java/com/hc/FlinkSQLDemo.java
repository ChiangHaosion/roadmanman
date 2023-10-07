package com.hc;

import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.connector.file.src.FileSource;
import org.apache.flink.connector.file.src.reader.TextLineFormat;
import org.apache.flink.core.fs.Path;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.DataTypes;
import org.apache.flink.table.api.Schema;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.TableResult;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.table.descriptors.TableDescriptor;

/**
 * @Author HaosionChiang
 * @Date 2023/9/6
 **/
public class FlinkSQLDemo {


    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env);

        FileSource<String> build = FileSource.forRecordStreamFormat(new TextLineFormat(), new Path("C:\\Users\\HaosionChiang\\Desktop\\something.csv")).build();

        DataStream<String> ds = env.fromSource(build, WatermarkStrategy.noWatermarks(),"csv");

        tableEnv.createTemporaryView("myTable",ds);

        TableResult tableResult = tableEnv.executeSql("select * from myTable");

        tableResult.print();

        env.execute();
    }

}
