package com.interview._多线程;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author HaosionChiang
 * @Date 2023/8/28
 **/
public class Print12 {

    //两个线程,交替打印12,打印10次,12121212121212121212
    //synchronized  使用一把锁和一个变量, 每次当前线程循环判断是否变量是自己的,不是就直接等待wait,释放CPU资源
    // 若是自己的,则打印1,修改变量=2,唤醒其他线程.notify
    //reentrantLock

    private static Integer integer = 1;
    private static Object object = new Object();

    public static void m1(String[] args) {
        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                synchronized (object) {
                    while (integer != 1) {
                        try {
                            object.wait();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    System.out.println("1");
                    integer = 2;
                    object.notify();

                }
            }
        }).start();

        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                synchronized (object) {
                    while (integer != 2) {
                        try {
                            object.wait();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    System.out.println("2");
                    integer = 1;
                    object.notify();
                }
            }
        }).start();

    }


    private ReentrantLock lock = new ReentrantLock();
    Condition condition=lock.newCondition();

    private int flag=1;
    public void m2(){
        new Thread(()->{
            for (int i = 0; i < 10; i++) {
                lock.lock();
                try {
                    while (flag!=1){
                        try {
                            condition.await();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    System.out.println(1);
                    flag=2;
                    condition.signal();
                }finally {
                    lock.unlock();
                }

            }
        }).start();

        new Thread(()->{
            for (int i = 0; i < 10; i++) {
                lock.lock();
                try {
                    while (flag!=2){
                        try {
                            condition.await();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    System.out.println(2);
                    flag=1;
                    condition.signal();
                }finally {
                    lock.unlock();
                }
            }
        }).start();


    }

    public static void main(String[] args) {
        Print12 mThread = new Print12();
        mThread.m2();
    }

}
