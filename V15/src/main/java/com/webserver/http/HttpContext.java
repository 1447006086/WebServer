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
        try {
            SAXReader reader = new SAXReader();
            Document document = reader.read("config/web.xml");
            Element element = document.getRootElement();
            List<Element> list = element.elements("mime-mapping");
            list.forEach(s -> {
                String key = s.elementText("extension");
                String value = s.elementText("mime-type");
                mimeMapping.put(key, value);
            });
            System.out.println(mimeMapping.size());
        } catch (DocumentException e) {
            e.printStackTrace();
        }
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
