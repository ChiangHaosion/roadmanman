package com.temp;

/**
 * @Author HaosionChiang
 * @Date 2023/9/19
 **/
public class Temp {

    public static void main(String[] args) {
        printBin(8);
        printBin(-8);
        //一个数的相反数，就是 取反+1
        printBin(Integer.MAX_VALUE);  // 2^31-1
        printBin(Integer.MIN_VALUE);  //-2^31
        //Java 中最高位是符号位

        printBin(8<<1);//左移一位
        printBin(-8>>>1);//无符号右移一位
        printBin(-8>>1);//右移一位         （有无符号右移，区别在于 >>> 无符号右移在最左边补充0 ， 而有>> 是在最左边补1）

    }

    /**
     * 给一个int 类型 打印它的二进制
     * @param n
     */
    public static void printBin(int n) {
        for (int i = 31; i >= 0; i--) {
            System.out.print((n & (1 << i)) == 0 ? "0" : "1");
        }
        System.out.println();
    }
}
