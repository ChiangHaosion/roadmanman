package com.interview._多线程;

/**
 *
 * 三个线程，分别打印ABC，共打印10次
 * @Author HaosionChiang
 * @Date 2023/8/28
 **/
public class PrintABC {

    public static void main(String[] args) {

        Resource resource = new Resource();

        new Thread(resource::funA,"线程1").start();

        new Thread(resource::funB,"线程2").start();

        new Thread(resource::funC,"线程3").start();
    }

    public static class Resource{

        private int tag=1;

        private Object lock=new Object();


        private void funA(){
            synchronized (lock){
                for (int i = 0; i < 10; i++) {
                    while (tag!=1){
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    System.out.println(Thread.currentThread().getName()+":"+"A");
                    tag=2;
                    lock.notifyAll();
                }
            }
        }
        private void funB(){
            synchronized (lock){

                for (int i = 0; i < 10; i++) {
                    while (tag!=2){
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    System.out.println(Thread.currentThread().getName()+":"+"B");
                    tag=3;
                    lock.notifyAll();
                }
            }
        }
        private  void funC(){
            synchronized (lock){
                for (int i = 0; i < 10; i++) {
                    while (tag!=3){
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    System.out.println(Thread.currentThread().getName()+":"+"C");
                    tag=1;
                    lock.notifyAll();
                }
            }
        }
    }

}
