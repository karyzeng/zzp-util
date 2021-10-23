package com.zzp.threadpool.utils;

import java.util.concurrent.*;

public class WorkerThreadPool {

	//池中所保存的线程数，包括空闲线程。
	final static int corePoolSize = 5;
	//池中允许的最大线程数。
	final static int maximumPoolSize = 10;
	//当线程数大于核心线程时，此为终止前多余的空闲线程等待新任务的最长时间
	final static long keepAliveTime = 300;
	//执行前用于保持任务的队列5，即任务缓存队列
	final static ArrayBlockingQueue<Runnable> workQueue =new ArrayBlockingQueue<Runnable>(5);


	public static void main(String[] args) {

		ThreadPoolExecutor workPool = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.MINUTES, workQueue);

		while (true) {
			for (int i = 0; i < 5; i++) {
				WorkerThread workerThread = new WorkerThread("任务" + (i + 1));
				workPool.execute(workerThread);
				System.out.println("线程池中线程数目："+workPool.getPoolSize()+"，队列中等待执行的任务数目："+workPool.getQueue().size()+"，已执行玩别的任务数目："+workPool.getCompletedTaskCount());
			}
			System.out.println("-----------");
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

}
