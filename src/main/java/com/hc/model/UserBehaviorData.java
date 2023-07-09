package com.hc.model;

import com.hc.core.UserBehaviorType;
import lombok.Data;


@Data
public class UserBehaviorData {
    private String userId;
    private String productId;
    private UserBehaviorType behaviorType;
    private long timestamp;

    public UserBehaviorData(String userId, String productId, UserBehaviorType behaviorType, long timestamp) {
        this.userId = userId;
        this.productId = productId;
        this.behaviorType = behaviorType;
        this.timestamp = timestamp;
    }
}
