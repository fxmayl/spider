package com.zjrb.rm;

import java.io.IOException;
import java.util.concurrent.Callable;

public class MyThread implements Callable<String> {

	// 待爬取的URL
	private String url;

	public MyThread(String u) {
		url = u;
	}

	@Override
	public String call() {

		String content = null;
		// 爬取网页信息，即调用网页解析方法
		try {
			GetUpComment.getUpCommentData(url);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return content;
	}

}
