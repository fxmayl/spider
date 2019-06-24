package com.zbcm.first;

import org.junit.Test;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.util.List;

/**
 * Created by zbcm on 2017-09-12.
 */
public class spiderTest {
    @Test
    public void testSpiderHtml() throws ParserConfigurationException, XPathExpressionException, IOException {
//        spider.spiderHtml("http://tv.youku.com/?spm=a2hww.20023042.m_223471.5~5~H2~A","//*[@id='nav-second']/div/ul//li");
//        spider.spiderHtml("0http://blog.csdn.net/jmydream/article/details/864404","//*[@id='newcomments']/ul[2]/li/p");
//        spider.spiderHtml("http://blog.csdn.net/gamer_gyt/article/details/47418223","//*[@id='comment_list']");
//        spider.spiderDemo("http://blog.csdn.net/gamer_gyt/article/details/47418223","#body .comment_class #comment_list");
//        #post-391897968 > tbody > tr:nth-child(1) > td.wirter > dl > dd.nickname > span
//        spider.spiderDemo("http://bbs.csdn.net/topics/380190310","#post-391897968 > tbody > tr:nth-child(1) > td.post_info > div");
//        SpiderSelector.getInstance().spiderNameAndMessage("http://bbs.csdn.net/topics/391052450", "#topics-show > div.wraper > div.detailed > table");
//        String s = Spider.getInstance().spiderOneValue("http://blog.csdn.net/gamer_gyt/article/details/47418223", "#comment_item_7081642 > dt > span > a.username");
//        System.out.println(s);
//        String value = SpiderSelector.getInstance().spiderOneValue("http://blog.csdn.net/jmydream/comment/list/8644004?page=1", "body");
//        System.out.println(value);
        //http://bbs.csdn.net/forums/fusioncloud

        //#forums-show > div.wrap > div.content > table > tbody > tr:nth-child(2) > td:nth-child(6) > a

//        List<String> list = SpiderSelector.getInstance().spiderAttr("http://bbs.csdn.net/forums/fusioncloud", "#forums-show > div.wrap > div.content > table > tbody > tr:nth-child(2) > td.title > a", "href");
//        System.out.println(list.get(0));
//        for (int i = 0; i < list.size(); i++) {
//            System.out.println(list.get(i));
//        }///topics/391052450/close

        //#forums-show > div.wrap > div.content > table > tbody > tr:nth-child(2) > td:nth-child(6) > a

        //#forums-show > div.wrap > div.content > table > tbody > tr:nth-child(3) > td:nth-child(6) > a

//        http://bbs.csdn.net/topics/390771387
    }


}
