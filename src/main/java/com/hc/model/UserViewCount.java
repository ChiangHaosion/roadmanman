package com.hc.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public
class UserViewCount {
    private String userName;
    private long windowEnd;
    private long viewCount;

}
