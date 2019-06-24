package com.zbcm.first;

import org.apache.log4j.Logger;
import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.DomSerializer;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zbcm on 2017-09-13.
 */
public class SpiderXpath {
    private static Logger logger = Logger.getLogger(SpiderXpath.class);
    private static volatile SpiderXpath instance = null;
    private static Document dom = null;

    private SpiderXpath() {
    }

    public static SpiderXpath getInstance() {
        if (instance == null) {
            synchronized (SpiderXpath.class) {
                if (instance == null) {
                    instance = new SpiderXpath();
                }
            }
        }
        return instance;
    }

    private void init(String url) {
        Connection connect = Jsoup.connect(url);
        String html = null;
        try {
            html = connect.get().body().html();
        } catch (IOException e) {
            logger.error("创建连接出错!", e);
            return;
        }
        HtmlCleaner cleaner = new HtmlCleaner();
        TagNode tagNode = cleaner.clean(html);
        try {
            dom = new DomSerializer(new CleanerProperties()).createDOM(tagNode);
        } catch (ParserConfigurationException e) {
            logger.error("获取dom出错!", e);
            return;
        }
    }

    public NodeList getNode(String url, String xPathStr) {
        if (dom == null) {
            init(url);
        }
        XPath xPath = XPathFactory.newInstance().newXPath();
        NodeList nodes = null;
        try {
            nodes = (NodeList) xPath.evaluate(xPathStr, dom, XPathConstants.NODESET);
        } catch (XPathExpressionException e) {
            logger.error("xPath匹配出错!", e);
            return null;
        }
        return nodes;
    }

    public List<String> getTextContent(String url, String xPathStr) {
        List<String> list = new ArrayList<String>();
        NodeList nodes = getNode(url, xPathStr);
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            String content = node.getTextContent();
            if (content != null && !content.equals("")) {
                list.add(content);
            }
        }
        return list;
    }

    public List<String> getAttrs(String url, String xPathStr, String attr) {
        List<String> list = new ArrayList<String>();
        NodeList nodes = getNode(url, xPathStr);
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            String content = node.getAttributes().getNamedItem(attr).getTextContent();
            if (content != null && !content.equals("")) {
                list.add(content);
            }
        }
        return list;
    }

    public String getOneTextContent(String url, String xPathStr) {
        String text = getTextContent(url, xPathStr).get(0);
        return text;
    }

    public String getOneAttr(String url, String xPathStr, String attr) {
        String text = getAttrs(url, xPathStr, attr).get(0);
        return text;
    }
}
