package com.od;


/**
 * 统计监控 需要打开多少监控器
 *
 * 题目描述
 *
 * 某长方形停车场，每个车位上方都有对应监控器,当且仅当在当前车位或者前后左右四个方向任意一个车位范围停车时，监控器才需要打开；
 *
 * 给出某一时刻停车场的停车分布，请统计最少需要打开多少个监控器；
 *
 * 输入描述
 *
 * 第一行输入m,n表示长宽，满足1<m,n<=20;后面输入m行，每行有n个0或1的整数，整数间使用一个空格隔开，表示该行已停车情况，其中0表示空位，1表示已停；
 *
 * 输出描述
 *
 * 最少需要打开监控器的数量；
 */
public class Main00 {
    public static void main(String[] args) {
        int[][] arr = {{0, 0, 0}, {0, 1, 0}, {0, 0, 0}};
        int result = getResult(arr);
        System.out.println(result);
    }

    public static int getResult(int[][] arr) {
        int cnt = 0;
        int m = arr.length;
        int n = arr[0].length;


        int[][] directs = {
                {1, 0},
                {-1, 0},
                {0, 1},
                {0, -1}
        };

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (arr[i][j] == 1) {
                    cnt++;
                    continue;
                }

                // 只要一个方向上为1，就要打开监控。当前位置上下左右遍历一遍
                for (int[] direct : directs) {
                    int newX = i + direct[0];
                    int newY = j + direct[1];

                    //新的位置不能越界 且 为1
                    if (newX >= 0 && newX < m
                            && newY >= 0 && newY < n
                            && arr[newX][newY] == 1) {
                        cnt++;
                        break;
                    }
                }
            }
        }

        return cnt;
    }
}
