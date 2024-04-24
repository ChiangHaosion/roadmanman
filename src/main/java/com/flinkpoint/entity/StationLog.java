package com.flinkpoint.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author HaosionChiang
 * @Date 2024/4/19
 **/

@Data@NoArgsConstructor@AllArgsConstructor
public class StationLog {
    private String id;
    private String start;
    private String end;
    private String status;
    private Long delay;
    private Long time;
}
