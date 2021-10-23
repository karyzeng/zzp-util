package com.zzp.threadpool.utils;

/**
 * 工作线程
 * 
 * @author karyzeng
 * @since 2018.11.19
 *
 */
public class WorkerThread implements Runnable {

	private String workName;
	
	public WorkerThread(String workName) {
		this.workName = workName;
	}
	
	@Override
	public void run() {
		try {
			Thread.currentThread().setName(workName);
			Thread.sleep(1000);
//			System.out.println(1 / 0);
			System.out.println(workName + "任务执行完了");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(workName + "任务执行异常");
		}
	}

}
