package com.zbcm.first;

import com.zbcm.po.ThreadMsg;
import org.apache.log4j.Logger;

import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * Created by zbcm on 2017-09-22.
 */
public class MySubPageThread implements Runnable {
    Logger logger = Logger.getLogger(MySubPageThread.class);
    private String url;
    private String page;
    private int urlNum;
    private CountDownLatch end;
    private String mainThreadName;
    private ThreadMsg msg;

    public MySubPageThread(String url, String page, int urlNum, CountDownLatch end, String mainThreadName, ThreadMsg msg) {
        this.end = end;
        this.urlNum = urlNum;
        this.page = page;
        this.url = url;
        this.mainThreadName = mainThreadName;
        this.msg = msg;
    }

    @Override
    public void run() {
//        logger.info("Sub" + " >>>>>> " + url + " <<<<<< " + urlNum + " ======  " + Thread.currentThread().getName());
        try {
            SpiderSelector.getInstance().spiderOneSubPage(url, page, urlNum, mainThreadName, Thread.currentThread().getName() + urlNum, msg);
        } catch (IOException e) {
            logger.error("MySubPageThread >>>>> " + "IO异常!", e);
            e.printStackTrace();
        } catch (XPathExpressionException e) {
            logger.error("MySubPageThread >>>>> " + "cssQuery匹配异常!", e);
            e.printStackTrace();
        }
        end.countDown();
    }
}
