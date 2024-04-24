package com.temp;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author HaosionChiang
 * @Date 2024/3/20
 **/
public class CountMulThread2 {

    //两个线程，完成容器的add 和 size 操作 ，一个add 10次，另外一个判断size，当size>5的时候，输出over
    public static void main(String[] args) throws InterruptedException {


        new Thread(()->{
            ResourceList resourceList = new ResourceList();
            resourceList.size();
        }).start();

        new Thread(()->{
            ResourceList resourceList = new ResourceList();
            resourceList.add();
        }).start();
    }

    public static class ResourceList{
        private volatile List<String> list = new ArrayList<>();

        public void add(){
            synchronized (this) {
                for (int i = 0; i < 10; i++) {
                    list.add(i + "");
                    System.out.println(Thread.currentThread().getName()+"\tadd element "+ i);
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        public void size(){
            synchronized (this) {
                while (true) {
                    if (list.size()>5){
                        System.out.println(Thread.currentThread().getName()+"\terror");
                        break;
                    }
                }
            }
        }
    }
    public static class MyThread1 extends Thread {
        @Override
        public void run() {

        }
    }

    public static class MyThread2 extends Thread {
        @Override
        public void run() {

        }
    }
}
