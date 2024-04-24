package com.interview;

import java.io.File;
import java.util.Stack;

/**
 * 大厂面试1
 *
 * 给定一个有序数组arr，代表坐落在X轴上的点,给定一个正数K，代表绳子的长度,返回绳子最多压中几个点
 *
 *
 * @Author HaosionChiang
 * @Date 2023/10/8
 **/
public class DCMS_1 {


    public static void main(String[] args) {
    }

    public static int maxPoint2(int[] arr, int K) {
        int l=0;
        int r=0;
        int len=arr.length;

        int max=0;

        while (l<len){
            while (r<len&&(arr[r]-arr[l])<=K){
                r++;
            }
            max=Math.max(max,r-l);
            l++;
        }
        return max;
    }

}
