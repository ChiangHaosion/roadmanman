package com.hc.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author HaosionChiang
 * @Date 2021/6/20
 **/

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserBehavior {
    private String userId;
    private String productId;
    private String catalogId;
    private String actionType;
    private String timestamps;
}
