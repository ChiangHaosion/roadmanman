/**
 * Copyright 2022 bejson.com
 */
package com.hc.model;

import lombok.Data;

@Data
public class RatingAmazon {
    /**
     * 评论者ID
     */
    private String reviewerID;

    /**
     * 商品ID
     */
    private String asin;
    /**
     * 评论用户名
     */
    private String reviewerName;

    /**
     * 评分
     */
    private Double overall;

    /**
     * 评价文本
     */
    private String reviewText;
    /**
     * 评价总结
     */
    private String summary;

    /**
     * 评价时间戳
     */
    private Long unixReviewTime;

}
