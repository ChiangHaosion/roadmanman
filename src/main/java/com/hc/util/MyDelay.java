package com.hc.util;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

// 自己创建一个类MyDelay，实现Java提供的接口Delayed
public class MyDelay<T> implements Delayed {

	// 延迟时间,(时间单位会在计算剩余时间的方法getDelay里面,由你自己指定，一般来说都会使用毫秒,更精确一点。)
	long delayTime;

	// 过期时间,(时间单位会在计算剩余时间的方法getDelay里面,由你自己指定，一般来说都会使用毫秒,更精确一点。)
	long expire;

	// 你自己放进队列里的数据
	T data;

	public MyDelay(long delayTime, TimeUnit delayTimeUnit, T t) {
		// 将用户传进来的时间转换为毫秒
		this.delayTime = TimeUnit.MILLISECONDS.convert(delayTime, delayTimeUnit);
		// 过期时间 = 当前时间 + 延迟时间(时间单位会在计算剩余时间的方法getDelay里面,由你自己指定，一般来说都会使用毫秒,更精确一点。)
		// 当然你也可以使用别的时间,随意的
		this.expire = System.currentTimeMillis() + delayTime;
		data = t;
	}

	/**
	 * 剩余时间 = 过期时间 - 当前时间
	 *
	 */
	@Override
	public long getDelay(TimeUnit unit) {
		// 注意convert这个方法,第一个参数是一个long类型的数值,第二个参数的意思是告诉convert第一个long类型的值的单位是毫秒
		return unit.convert(this.expire - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
	}

	/**
	 * 优先级:俩个任务比较，时间短的优先执行
	 *
	 */
	@Override
	public int compareTo(Delayed o){
		long f = this.getDelay(TimeUnit.MILLISECONDS) - o.getDelay(TimeUnit.MILLISECONDS);
		return (int)f;
	}

	@Override
	public String toString() {
		// 这个toString()方法不是必须的，你可以不重写。写不写都无所谓，我这里为了测试，将数据打印出来了。
		return "delayTime=" + delayTime + ",expire=" + expire + ",data=" + data;
	}
}
