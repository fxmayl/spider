package com.zbcm.first;

import org.apache.log4j.Logger;

import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * Created by zbcm on 2017-09-20.
 */
public class MyMainPageThread implements Runnable {
    Logger logger = Logger.getLogger(MyMainPageThread.class);
    private String url;
    private CountDownLatch end;
    private int mainThreadNum;

    public MyMainPageThread(String url, CountDownLatch end, int mainThreadNum) {
        this.url = url;
        this.end = end;
        this.mainThreadNum = mainThreadNum;
    }

    public void run() {
//        logger.info("Main >>>>> " + url + "  <<<<< " + Thread.currentThread().getName());
        try {
            SpiderSelector.getInstance().spiderMoreSubPage(url, Thread.currentThread().getName() + "::" + mainThreadNum);
        } catch (IOException e) {
            logger.error("MyMainPageThread >>>>> " + "IO异常!", e);
            e.printStackTrace();
        } catch (XPathExpressionException e) {
            logger.error("MyMainPageThread >>>>> " + "cssQuery匹配异常!", e);
            e.printStackTrace();
        }
        end.countDown();
    }
}
