package com.webserver.core;

import com.webserver.servlet.*;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 当前类用于保存服务端重用的一些内容
 */
public class ServerContent {
    private static Map<String, HttpServlet>servletMapping=new HashMap<>();
    static {
        try {
            initservletMapping();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
    private static void initservletMapping() throws ClassNotFoundException, NoSuchMethodException {
        /*
        * 解析config/servlets.xml文件,将根标签下所有名为<servlet>的标签获取到,并将其中属性
        * path的值作为key
        * className的值利用反射实例化对应的类并作为value
        * 保存到servletMapping这个Map完成初始化操作
        * */
        SAXReader reader=new SAXReader();
        try {
            Document document = reader.read("config/servlets.xml");
            Element element = document.getRootElement();
            List<Element>list=element.elements("servlet");
            list.forEach(e->{
                String key = e.attributeValue("path");
                String value = e.attributeValue("className");
                try {
                    Class cls = Class.forName(value);
                    Object o = cls.newInstance();
                    HttpServlet servlet=(HttpServlet)o;
                    servletMapping.put(key,servlet);
                } catch (ClassNotFoundException classNotFoundException) {
                    classNotFoundException.printStackTrace();
                } catch (IllegalAccessException illegalAccessException) {
                    illegalAccessException.printStackTrace();
                } catch (InstantiationException instantiationException) {
                    instantiationException.printStackTrace();
                }
            });
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    public static HttpServlet  getServlet(String key) {
        System.out.println(servletMapping);
        return servletMapping.get(key);
    }

}
