package com.zbcm.first;

import org.junit.Test;

import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zbcm on 2017-09-21.
 */
public class TaskTest {

    @Test
    public void testadad() throws InterruptedException {
        Main.start("http://bbs.csdn.net/forums/Java", 5, 10, 1);
    }

    @Test
    public void m() throws InterruptedException {
        MyMainPageThread mainPageThread = new MyMainPageThread("http://bbs.csdn.net/forums/hadoop", new CountDownLatch(1), 1);
        Thread thread = new Thread(mainPageThread, "ABC");
        thread.start();
        while (thread.isAlive()) {
            continue;
        }
    }

    @Test
    public void hhhh() throws IOException, XPathExpressionException {
        SpiderSelector.getInstance().spiderMoreSubPage("http://bbs.csdn.net/forums/hadoop", "");
    }

    @Test
    public void testSt() {
        String templateFile = "　时间：11月21日9:00-11:00 " +
                "<!--修改时间处 begin--> " +
                "主持人" +
                "<!--修改嘉宾处 begin-->" +
                "刘正荣 国家互联网信息办公室政策法规局局长" +
                "嘉宾   <!--修改嘉宾处 begin-->" +
                "周汉华 中国社会科学院法学研究所所长助理、研究员" +
                "阿拉木斯 中国电子商务协会政策法律委员会副主任" +
                "王四新 中国传媒大学政治与法律学院副院长、教授" +
                "李欲晓 北京邮电大学互联网治理与法律研究中心主任、教授" +
                "<!--------------------------------------------视频end-------------------------------------------->\t\t<!-------------------访谈现场 begin---------------->\t\t\t现场图片\t\t\t\t\t%!__link_model_x(ftxc,1,4,1,/ch34/small_template/liaimgspanav.html)!%\t\t\t%!__link_model_x(ftxc,5,5,1,/ch34/small_template/liaimgspanarv.html)!%\t\t\t%!__link_model_x(ftxc,6,9,1,/ch34/small_template/liaimgspanav.html)!%\t\t\t%!__link_model_x(ftxc,10,10,1,/ch34/small_template/liaimgspanarv.html)!%\t\t\t\t<!-------------------访谈现场 end---------------->\t\t\t\t\t\t文字实录\t\t\t%!content()!%\t\t\t\t\t\t\t相关报道\t\t\t\t\t\t\t%!__latest_model_x(/ch34/sd/,1,14,/ch34/small_template/lia.html)!%\t\t\t\t\t\t<!-------------------footer begin----------------><!--#include virtual=\"/inc/wicfooter.html\"--><!-------------------footer end---------------->";
        Pattern pattern = Pattern.compile("%!(.*?)!%");
        Matcher matcher = pattern.matcher(templateFile);
        if (matcher.find()) {
            System.out.println(matcher.group(1));
            System.out.println("+++++++++++++++++++++++");
        }
    }

    @Test
    public void testyu(){
        String templateFile = "dhsaidhis";
        int indexOf = templateFile.lastIndexOf("/");
        int endIndex = templateFile.lastIndexOf(".");
        System.out.println(indexOf+ "::::" +endIndex);
        String template_file = templateFile.substring(indexOf + 1, endIndex);
        System.out.println(template_file);
    }



    @Test
    public void testyuo(){
        String templateFile = "150010000,150020000,150030000,150040000,150050000,150060000,150070000,150080000,150090000,150100000,150110000,150120000,150130000,150140000,150150000,150160000,150170000,150180000,150190000,150200000,150210000,150220000,150230000,150240000,150250000,150260000,150010100,150010200,150010300,150010400,150010500,150010600,150040100,150040200,150040300,150100100,150100200,150100300,150100400,150100500,150100600,150100700,150100800,150120100,150120200,150120300,150120400,150120500,150120600,150150100,150150200,150150300,150210100,150210200,150210300,150210400,150210500,150210600,150210700,150210800,150230100,150230200,150230300,150230400,150230500,150230600,150230700,150230800,150230900,150231000,150231100,150250100,150250200,150250300,150250400,150250500,150250600,150250700,150250800,150250900,150251000,150251100,150251200,150260100,150260200,150260300,150260400,150260500,150260600,150260700,150260800,150260900,150261000,150261100,150261200";
        String[] split = templateFile.split(",");
//        for (int i = 0; i < split.length; i++) {
//            System.out.print(split[i].trim()+",");
//        }
//        System.out.println();
        System.out.println(split.length);
    }
}
