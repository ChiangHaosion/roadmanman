package com.interview;

import java.util.concurrent.TimeUnit;

/**
 * @Author HaosionChiang
 * @Date 2023/4/25
 **/
public class Inter_01 {


    public static void main(String[] args) throws InterruptedException {
        Thread a_start = new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("A start");
        });
        Thread b_start = new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("B start");
        });

        a_start.start();
        b_start.start();

        //当前调用者等待A线程结束
        b_start.join();
        System.out.println("主线程start");
    }
}
