package com.od;


import java.util.Arrays;
import java.util.Scanner;

/**
 * 分苹果
 *
 * 1. 异或等分--》异或最后是0
 * 2. b取最大值，说明 给a 最小的， 且要满足上面的条件
 *
 * ；
 */
public class Main01 {
    public static void main(String[] args) {

        Scanner scanner=new Scanner(System.in);

        int m=scanner.nextInt();

        int [] arr=new int[m];

        for (int i = 0; i < m; i++) {
            arr[i]=scanner.nextInt();
        }

        Arrays.sort(arr);

        int min=arr[0];

        int sum=0;
        int yihuo=0;

        for (int i : arr) {
            sum+=i;
            yihuo^=i;
        }

        if (yihuo==0){
            System.out.println(sum-min);
        }else {
            System.out.println(-1);
        }
    }
}
