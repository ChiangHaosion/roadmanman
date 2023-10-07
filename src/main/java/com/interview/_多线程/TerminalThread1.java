package com.interview._多线程;

/**
 *
 * 通过一个 可见的 变量，结束线程
 *
 * @Author HaosionChiang
 * @Date 2023/8/22
 **/
public class TerminalThread1 {


    private static volatile boolean flag = true;

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            while (flag) {
                // ....
            }
            System.out.println("t1线程结束");
        });

        t1.start();
        Thread.sleep(10);
        flag = false;
        System.out.println("主线程将flag改为false");
    }
}
