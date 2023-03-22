/**
 * Copyright 2022 bejson.com
 */
package com.hc.model;

import lombok.Data;

import java.util.List;

/**
 * Auto-generated: 2022-09-10 17:45:47
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
@Data
public class ProductAmazon {
    private String asin;
    private String title;
    private String brand;
    private List<String> imageURL;
    private List<String> imageURLHighRes;
}
