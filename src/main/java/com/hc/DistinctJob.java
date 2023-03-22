package com.hc;

import com.alibaba.fastjson2.JSON;
import com.hc.model.RatingAmazon;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;
import org.bson.Document;

/**
 * @Author HaosionChiang
 * @Date 2022/9/9
 **/
public class DistinctJob {

    private static String rating_data = "E:\\rec_data\\AMAZON_FASHION.json";
    private static String meta_data = "E:\\rec_data\\meta_AMAZON_FASHION.json";
    private static String uri = "mongodb://localhost:27017";

    private static MongoClient mongoClient = MongoClients.create(uri);

    public static void main(String[] args) throws Exception {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStreamSource<String> source = env.readTextFile(rating_data);

        SingleOutputStreamOperator<RatingAmazon> ds = source.flatMap(new FlatMapFunction<String, RatingAmazon>() {
            @Override
            public void flatMap(String s, Collector<RatingAmazon> collector) throws Exception {
                try {
                    RatingAmazon ratingAmazon = JSON.parseObject(s, RatingAmazon.class);
                        MongoDatabase database = mongoClient.getDatabase("Recommend");
                        MongoCollection<Document> collection = database.getCollection("rating_amazon");
                        collection.insertOne(Document.parse(JSON.toJSONString(ratingAmazon)));
                        collector.collect(ratingAmazon);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        ds.print();
        env.execute();
    }
}
