package com.temp;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author HaosionChiang
 * @Date 2024/3/20
 **/
public class CountMulThread {

    //多线程，相加，使用原子类

    public static final AtomicInteger m = new AtomicInteger(0);

    public static void main(String[] args) throws InterruptedException {
        Thread[] threads = new Thread[100];
        CountDownLatch countDownLatch = new CountDownLatch(100);

        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 1000; j++) {
                    m.getAndIncrement();
                }

                //一个线程结束，state--
                countDownLatch.countDown();
            });
        }


        for (Thread thread : threads) {
            thread.start();
        }
        //等待所有的线程结束
        countDownLatch.await();
        System.out.println(m);
    }


}
