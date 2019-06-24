package com.zbcm.first;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by zbcm on 2017-09-22.
 */
public class Main {
    /**
     * 启动主线程
     *
     * @param url           主页面url
     * @param corePoolSize  线程池核心线程大小
     * @param blockCapacity 阻塞队列长度
     * @param pageNum       主页面页数
     * @throws InterruptedException
     */
    public static void start(String url, int corePoolSize, int blockCapacity, int pageNum) throws InterruptedException {
        ThreadPoolExecutor executor =
                new ThreadPoolExecutor(corePoolSize, corePoolSize * 2, 0L, TimeUnit.MICROSECONDS, new ArrayBlockingQueue<Runnable>(blockCapacity));
        String tempUrl = url;
        CountDownLatch end = new CountDownLatch(pageNum);
        for (int i = 1; i <= pageNum; i++) {
            MyMainPageThread mainPageThread = new MyMainPageThread(url, end, i);
            executor.submit(mainPageThread);
            url = tempUrl;
            int page = i + 1;
            url = url + "?page=" + page;
        }
        end.await();
    }
}
