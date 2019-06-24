package com.zjrb.rm;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

public class GetUpComment {

	public static void getUpCommentData(String url) throws IOException {

		// 接收App软件用户评论的网址
		Document doc = Jsoup.connect(url).get();

		/*
		 * 分别解析App软件的id、名称、大小、版本号、下载次数和类别
		 */
		Elements eleHidAppId = doc.select("div#doc.software>div.yui3-g>div.yui3-u.content-main>div.app-intro>input");
		Elements eleHidAppName = doc.select(
				"div#doc.software>div.yui3-g>div.yui3-u.content-main>div.app-intro>div.intro-top>div.content-right>h1.app-name>span");
		Elements eleHidAppSize = doc.select(
				"div#doc.software>div.yui3-g>div.yui3-u.content-main>div.app-intro>div.intro-top>div.content-right>div.detail>span.size");
		Elements eleHidAppVersion = doc.select(
				"div#doc.software>div.yui3-g>div.yui3-u.content-main>div.app-intro>div.intro-top>div.content-right>div.detail>span.version");
		Elements eleHidAppDownloadNum = doc.select(
				"div#doc.software>div.yui3-g>div.yui3-u.content-main>div.app-intro>div.intro-top>div.content-right>div.detail>span.download-num");
		Elements eleHidAppClassification = doc.select("div#doc.software>div.app-nav>div.nav>span>a");

		// 去掉App软件的id、名称、大小、版本号、下载次数和类别的标签，取出相应的值
		String appId = eleHidAppId.val().toString();
		String appName = eleHidAppName.text().toString();
		String appSize = eleHidAppSize.text().toString();
		String appVersion = eleHidAppVersion.text().toString();
		String appDownloadNum = eleHidAppDownloadNum.text().toString();
		String appClassification = eleHidAppClassification.text().toString();

		System.out.println(appId + "  " + appName + "  " + appSize + "  " + appVersion + "  " + appClassification + "  "
				+ appDownloadNum);

		/*
		 * 解析用户评论：用户名称、用户评论信息、评论时间； 循环控制翻页
		 */
		for (int i = 1;; i++) {

			Document documentComment = Jsoup
					.connect("http://shouji.baidu.com/comment?action_type=getCommentList&groupid=" + appId + "&pn=" + i)
					.get();
			Elements eleuserComment = documentComment.select("body>ol.comment-list>li.comment");

			// 当页面信息为空时，不再进行翻页，循环结束
			if (eleuserComment.text().isEmpty()) {
				break;
			}

			String userComment = eleuserComment.text().toString();
			System.out.println(i + "----" + appId + ":" + userComment);
		}

	}

}
