package com.zjrb.rm;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadsTest {

	public static void main(String[] args) throws InterruptedException,
			ExecutionException, IOException {

		String urls[] = { "http://shouji.baidu.com/software/22095760.html",
				"http://shouji.baidu.com/software/22056776.html",
				"http://shouji.baidu.com/software/22069029.html" };

		// 并发线程数量
		int threads = 2;
		// 创建线程池
		ExecutorService es = Executors.newFixedThreadPool(threads);

		for (String url : urls) {
			MyThread task = new MyThread(url);
			// 提交爬取任务
			es.submit(task);
		}

		es.shutdown();
	}

}
