package com.od;


import java.util.Scanner;

/**
 * 阿里巴巴的黄金宝箱 1
 * <p>
 * <p>
 * 给定一个数据，  求一个下标索引， 使得 左边之和等于右边之和
 * <p>
 * <p>
 * <p>
 * ；
 */
public class Main03 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String str = scanner.nextLine();

        String[] split = str.split(",");

        int[] nums = new int[split.length];

        int i = 0;
        for (String s : split) {
            nums[i++] = Integer.parseInt(s);
        }

        int rightsum=0;
        int leftsum=0;

        for (int num : nums) {
            rightsum+=num;
        }

        int rp=-1;
        for (int j = 0; j < nums.length; j++) {
            rightsum-=nums[j];
            if (leftsum==rightsum){
               rp=j;break;
            }
            leftsum+=nums[j];
        }
        System.out.println(rp);


    }
}
