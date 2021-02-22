package com.webserver.http;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
/**
 * 当前类用于保存所有与HTTP协议相关的规定内容以便重用
 */
public class HttpContext {
    /**
     * 资源后缀名与响应头Content-Type值的对应关系
     * Key:资源后缀名
     * value:Content-Type对应的值
     */
    private static Map<String, String> mimeMapping = new HashMap<>();

    static {
        try {
            initMimeMapping();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    private static void initMimeMapping() throws DocumentException {
        mimeMapping.put("png", "image/png");
        mimeMapping.put("jpg", "image/jpeg");
        mimeMapping.put("gif", "image/gif");
        mimeMapping.put("html", "text/html");
        mimeMapping.put("css", "text/css");
        mimeMapping.put("js", "application/javascript");
    }

    /**
     * 根据给定的资源后缀名获取到对应的Content的值
     *
     * @param ext
     * @return
     */
    public static String getMimeType(String ext) {
        return mimeMapping.get(ext);
    }
}
