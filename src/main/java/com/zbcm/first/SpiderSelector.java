package com.zbcm.first;

import com.zbcm.po.ThreadMsg;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by zbcm on 2017-09-12.
 */
public class SpiderSelector {
    private static Logger logger = Logger.getLogger(SpiderSelector.class);
    private static String mainPageUrlPrefix = "http://bbs.csdn.net/forums/Java";//主页面第一页地址
    private static String subPageUrlPrefix = "http://bbs.csdn.net";//子页面地址前缀
    private static String cssMainPageUrlSum = "#forums-show > div.wrap > div.content > table > tbody > tr";//匹配主页面中url总数
    private static String cssMainPageQueryPrefix = "#forums-show > div.wrap > div.content > table > tbody > tr:nth-child(";//匹配主页面中，子页面链接url的cssQuery前缀
    private static String cssMainPageQuerySuffix = ") > td.title > a";//匹配主页面中，子页面链接url的cssQuery后缀
    private static String cssSubPageQuery = "#topics-show > div.wraper > div.detailed > table";//匹配子页面用户和其回答时的id属性值的cssQuery
    private static String cssQueryId = "#";//匹配id属性的前缀符号
    private static String cssSubPageNameQuerySuffix = " > tbody > tr:nth-child(1) > td.wirter > dl > dd.nickname > span";//获取子页面中用户名称所用到的cssQuery后缀
    private static String cssSubPageAnsQuerySuffix = " > tbody > tr:nth-child(1) > td.post_info > div";//获取子页面中用户所对应回答的cssQuery后缀
    private static String pageSign = "?page=";//主页面页码符号
    private static int sumMainPageNum = 10;//主页面总数
    private static volatile SpiderSelector instance = null;

    private SpiderSelector() {
    }

    public static synchronized SpiderSelector getInstance() {
        if (instance == null) {
            instance = new SpiderSelector();
        }
        return instance;
    }

    /**
     * 解析html，获取document
     *
     * @param url 网页地址
     * @return 返回html解析后的document
     */
    private Document getDoc(String url) {
        try {
            CloseableHttpClient httpclient = HttpClients.createDefault(); // 创建httpclient实例
            HttpGet httpget = new HttpGet(url);

            CloseableHttpResponse response = httpclient.execute(httpget); // 执行get请求
            HttpEntity entity = response.getEntity(); // 获取返回实体
            String content = EntityUtils.toString(entity, "utf-8");
            response.close();

            Document doc = Jsoup.parse(content);//解析html
            return doc;
        } catch (IOException e) {
            logger.error("初始化document时出错", e);
            return null;
        }
    }

    /**
     * 获取节点属性值
     *
     * @param doc      网页解析后的document
     * @param cssQuery css匹配表达式
     * @param attr     属性名称
     * @return 返回节点属性值集合
     */
    private List<String> spiderAttr(Document doc, String cssQuery, String attr) {
        List<String> list = new ArrayList<String>();
        Elements elements = doc.select(cssQuery);
        if (elements.size() >= 1 && elements != null) {
            for (int i = 0; i < elements.size(); i++) {//遍历全部节点，从而获取相应节点对应的属性值
                Element element = elements.get(i);
                String value = element.attr(attr).trim();
                if (value != null && !value.equals("")) {
                    list.add(value);
                }
            }
        }
        return list;
    }

    /**
     * 获取节点中全部文本
     *
     * @param doc      网页解析后的document
     * @param cssQuery css匹配表达式
     * @return 返回全部节点中文本内容集合
     */
    private List<String> spiderValue(Document doc, String cssQuery) {
        if (doc == null) return null;
        List<String> list = new ArrayList<String>();
        Elements elements = doc.select(cssQuery);
        if (elements.size() >= 1 && elements != null) {
            for (int i = 0; i < elements.size(); i++) {//遍历所有节点中的文本内容
                Element element = elements.get(i);
                String text = element.text().trim();
                if (text != null && !text.equals("")) {//如果为null就不添加
                    list.add(text);
                }
            }
        }
        return list;
    }

    /**
     * 获取单个文本（标签中的文本）
     *
     * @param doc      网页解析后的document
     * @param cssQuery css匹配表达式
     * @return 返回节点中文本内容
     */
    private String spiderOneValue(Document doc, String cssQuery) {
        String text = "";
        String img = cssQuery + " > img";
        Elements elements = doc.select(img);
        if (elements != null && elements.size() >= 1) {//当评论中有图片时，获取节点中的图片
            for (int i = 0; i < elements.size(); i++) {
                Element element = elements.get(i);
                text = element.attr("src");
                break;
            }
        }
        elements = doc.select(cssQuery);
        if (elements != null && elements.size() >= 1) {
            for (int i = 0; i < elements.size(); i++) {//遍历节点中的文本内容
                Element element = elements.get(i);
                text = element.text() + text;
                text = text.replace("更多 分享到：", "");
                break;
            }
        }
        return text;
    }

    /**
     * 抓取名称和对应的回答
     *
     * @param mainPage 主页面页码
     * @param subPage  主页面中子页面位于第几条
     * @param url      网页地址
     * @param cssQuery css匹配表达式
     * @return 返回爬取结果，用户id和对应用户的回复内容
     * @throws IOException
     * @throws XPathExpressionException
     */
    private void spiderNameAndMessage(String mainPage, int subPage, String url, String cssQuery, String mainThreadName, String subThreadName, ThreadMsg msg) throws IOException, XPathExpressionException {
        Document doc = getDoc(url);
        if (doc != null) {
            List<String> idList = spiderAttr(doc, cssQuery, "id");//获取id的属性值
            if (idList != null && idList.size() >= 1) {
                String key = ">>【第" + mainPage + "页】 【第" + subPage + "条】>>> " + url;
//                System.out.println(key);
                for (int i = 0; i < idList.size(); i++) {
                    String s = idList.get(i);
                    //根据id属性值，获取用户名
                    String name = spiderOneValue(doc, cssQueryId + s + cssSubPageNameQuerySuffix);
                    //根据id属性值，获取对应用户的回复内容
                    String comment = spiderOneValue(doc, cssQueryId + s + cssSubPageAnsQuerySuffix);
                    int length = comment.length() > 128 ? 128 : comment.length();
                    System.out.println(key + "\n >>>>>>>>>>>>  " + name + " --->>>>> " + comment.substring(0, length));
                }
                synchronized (msg) {
                    int num = msg.getCompleteTaskNum();
                    msg.setCompleteTaskNum(++num);
                    logger.info("+++++++++++++++" + msg.getCompleteTaskNum() + ":::" + num);
                    if (msg.getCompleteTaskNum() == msg.getTaskCount()) {
                        logger.info("主线程为" + mainThreadName + " >> 子线程已经将" + num + "个任务执行完成!");
                    } else {
                        long unCompleteTaskNum = msg.getTaskCount() - num * 1L;
                        logger.error("主线程为" + mainThreadName + " >> 子线程已执行完" + num + "个任务!"
                                + "还有" + unCompleteTaskNum + "个任务未完成!");
                    }
                }
            }
        }
    }

    /**
     * 获取一个主页面中一个子页面用户名和用户对应的回答，以及当前主页面页码数和子页面地址
     *
     * @param url    主页面地址
     * @param page   页数
     * @param urlNum 匹配主页面中第几个子页面url地址
     * @return 返回子页面信息集合
     */
    public void spiderOneSubPage(String url, String page, int urlNum, String mainThreadName, String subThreadName, ThreadMsg msg) throws IOException, XPathExpressionException {
        if (urlNum <= 1) urlNum = 2;
        Document doc = getDoc(url);
        if (doc != null) {
            List<String> strHref = spiderAttr(doc, cssMainPageQueryPrefix + urlNum + cssMainPageQuerySuffix, "href");//传入的cssQuery可以唯一确定一个节点，因此获取的属性值只有一个
            if (strHref != null && strHref.size() >= 1) {
                String subPageUrl = subPageUrlPrefix + strHref.get(0);
                spiderNameAndMessage(page, (urlNum - 1), subPageUrl, cssSubPageQuery, mainThreadName, subThreadName, msg);
            }
        }
    }

    /**
     * 获取一个主页面中子页面的相关信息
     *
     * @param url            主页面地址
     * @param mainThreadName 主线程名称
     * @return 返回主页面中子页面的相关信息集合
     * @throws IOException
     * @throws XPathExpressionException
     */
    public void spiderMoreSubPage(String url, String mainThreadName) throws IOException, XPathExpressionException {
        Integer sumUrl = countSumUrl(url);
//        logger.info("总数为:" + sumUrl);
        if (sumUrl != null && url != null && !url.trim().equals("")) {
            String page = url.replace(mainPageUrlPrefix, "").trim();
            if (page == null || page.equals("")) {
                page = "1";
            }
            page = page.replace(pageSign, "").trim();//获取链接地址中的分页数
            ThreadPoolExecutor executor =
                    new ThreadPoolExecutor(5, 10, 0L, TimeUnit.MICROSECONDS, new ArrayBlockingQueue<Runnable>(20));
            int customizeUrlNum = sumUrl / 2;
            CountDownLatch end = new CountDownLatch(customizeUrlNum - 1);
            ThreadMsg msg = new ThreadMsg();
            msg.setCompleteTaskNum(0);
            for (int i = 2; i <= customizeUrlNum; i++) {
                MySubPageThread subPageThread = new MySubPageThread(url, page, i, end, mainThreadName, msg);
                executor.submit(subPageThread);
                logger.info("主线程为" + mainThreadName + " >> 子线程 -->>> " + "队列中等待执行的任务数目：" +
                        executor.getQueue().size());
            }
            int activeCount = executor.getActiveCount();
            long taskCount = executor.getTaskCount();
            msg.setActiveCount(activeCount);
            msg.setTaskCount(taskCount);
            logger.info("主线程为" + mainThreadName + " >> 子线程：" + msg.toString());
            try {
                end.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取主页面对应的子页面的相关信息
     *
     * @param url 第一个主页面地址
     * @return 返回子页面相关信息集合
     * @throws IOException
     * @throws XPathExpressionException
     */
    public void spiderMoreMainAndSubPage(String url) throws IOException, XPathExpressionException {
        String tempUrl = url;
        spiderMoreSubPage(url, "");
        for (int i = 2; i <= sumMainPageNum; i++) {
            url = tempUrl;
            url = url + pageSign + i;
            spiderMoreSubPage(url, "");
        }
    }

    /**
     * 计算页面中指定部位子url个数
     *
     * @return 返回个数
     */
    private Integer countSumUrl(String url) {
        Document doc = getDoc(url);
        Elements elements = null;
        if (doc != null) {
            elements = doc.select(cssMainPageUrlSum);
        } else {
            logger.error("url个数计算失败!");
            return null;
        }
        if (elements != null && elements.size() >= 1) {
            return elements.size() - 2;//去掉开始和结尾处的元素，因为其是标题部分，不包含url
        }
        return null;
    }
}
