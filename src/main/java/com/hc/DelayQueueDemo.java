package com.hc;

import com.hc.util.MyDelay;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public class DelayQueueDemo {

	static BlockingQueue<Delayed> queue = new DelayQueue<>();

	public static void main(String[] args) throws InterruptedException {
		queue.add(new MyDelay<>(60000000*2, TimeUnit.SECONDS, "data1"));
//		queue.add(new MyDelay<>(3, TimeUnit.SECONDS, "data2"));
//		queue.add(new MyDelay<>(5, TimeUnit.SECONDS, "data3"));

		while(!queue.isEmpty()){
			// queue.take()从延迟队列中取出任务，如果任务指定的延迟时间还没有到，这里是取不出来的，线程将一直阻塞
			// 线程状态将处于java.lang.Thread.State: TIMED_WAITING (parking),会释放CPU,底层调用的是 UNSAFE.park方法。
			Delayed delayed = queue.take();
			System.out.println(delayed);
		}
	}
}
