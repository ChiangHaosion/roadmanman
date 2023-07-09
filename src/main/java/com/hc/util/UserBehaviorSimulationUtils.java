package com.hc.util;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson2.JSON;
import com.hc.core.UserBehaviorType;
import com.hc.model.UserBehaviorData;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class UserBehaviorSimulationUtils {
    private static String generateUserId() {
        String prefix = "USER";
        int suffix = new Random().nextInt(10);
        return prefix + suffix;
    }

    private static String generateProductId() {
        String prefix = "PRODUCT";
        int suffix = new Random().nextInt(100);
        return prefix + suffix;
    }

    private static UserBehaviorType generateUserBehaviorType() {
        UserBehaviorType[] types = UserBehaviorType.values();
        int randomIndex = new Random().nextInt(types.length);
        return types[randomIndex];
    }


    private static List<UserBehaviorData> generateUserBehaviorData(int numRecords) {
        List<UserBehaviorData> userBehaviorDataList = new ArrayList<>();
        for (int i = 0; i < numRecords; i++) {
            String userId = generateUserId();
            String productId = generateProductId();
            UserBehaviorType behaviorType = generateUserBehaviorType();
            UserBehaviorData userBehaviorData = new UserBehaviorData(userId, productId, behaviorType, 0L);
            userBehaviorDataList.add(userBehaviorData);
        }
        return userBehaviorDataList;
    }

    public static void main(String[] args) throws InterruptedException {
        Thread dataGeneratorThread = new Thread(() -> {
            List<UserBehaviorData> userBehaviorDataList = generateUserBehaviorData(100);
        for (UserBehaviorData userBehaviorData : userBehaviorDataList) {
            try {
                int i = RandomUtil.getRandom().nextInt(2000);
                TimeUnit.MILLISECONDS.sleep(i);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            userBehaviorData.setTimestamp(Instant.now().toEpochMilli());
            System.out.println(DateUtil.date(userBehaviorData.getTimestamp()));
            KafkaUtil.sendToKafka(JSON.toJSONString(userBehaviorData));
        }
        });
        dataGeneratorThread.start();
    }
}


