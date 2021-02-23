package com.webserver.http;

import javax.sound.midi.Soundbank;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 请求对象
 * 该类的每一个实例用于表示客户端发送过来的一个http请求内容
 * 每个请求由三部分构成:
 * 请求行,消息头,消息正文
 */
public class HttpRequest {
    //请求行相关信息
    private String method;//请求方式
    private String uri;//抽象路径
    private String protocol;//协议版本

    private String requstURI;//存抽象路径中的请求部分,即:URI中?左侧的内容
    private String qustString;//存抽象路径中的参数部分,即:URI中?右侧的内容
    private Map<String, String> paranmeter = new HashMap<>();//存每一组参数
    //消息头相关信息
    private Map<String, String> headers = new HashMap<>();
    //消息正文相关信息

    private Socket socket;

    /**
     * HttpRequest的实例化过程就是解析请求的过程
     *
     * @param socket
     */
    public HttpRequest(Socket socket) throws EmptyRequestException {
        this.socket = socket;
        //1解析请求行
        parseRequestLine();

        //2解析消息头
        parseHanders();
        //解析消息正文
        parseContent();
    }

    //解析一个请求的三个步骤
    //1:解析请求行
    private void parseRequestLine() throws EmptyRequestException {
        System.out.println("HttpRequest:开始解析请求行...");
        try {
            //读取请求行
            String line = readLine();
            if (line.isEmpty()) {
                throw new EmptyRequestException();
            }
            //将请求行按照空格拆分为三部分,并分别赋值给上述变量
            String[] data = line.split(" ");
            System.out.println("请求行:" + line);
            method = data[0];
            uri = data[1];
            protocol = data[2];
            parserUri();
            System.out.println("method:" + method);
            System.out.println("url:" + uri);
            System.out.println("protocol:" + protocol);
            System.out.println("requstURI:" + requstURI);
            System.out.println("qustString:" + qustString);
            System.out.println("paranmeter" + paranmeter);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("HttpRequest:请求头解析完毕!");
    }

    //进一步解析uri
    private void parserUri() {
        /*
         * URI会存在两种情况:含有参数和不含有参数
         * 不含有参数的样子如:.myweb/index.html
         * 含有参数的样子如:/myweb.reqUser?username=xx&passworld=xxx......
         * 因此我们要对URI进一步拆分,需求如下:
         * 如果URI不含有参数,则不需要拆分,直接将URI的值赋值给requestURI即可
         *
         * 如果URI含有参数,则需要进行拆分:
         * 1:将URI按照"?"拆分为两部分,左侧赋值为requestURI,右侧赋值给quetyString
         * 2:将quetyString部分按照"&"拆分出每一组参数,然后每一组参数再次按照"="拆分为
         *   参数名和参数值,并将参数名作为key,参数值作为value保存到parameter这个Map中
         *   完成解析工作
         * */
        if (uri.contains("?")) {
            System.out.println("拆分了!");
            String[] data = uri.split("\\?");
            requstURI = data[0];
            qustString = data[1];
            data = qustString.split("&");
            //username=HelloWorld&passworld=121&nickname=java+No.1&age=22
            for (int i = 0; i < data.length; i++) {
                String[] user = data[i].split("=");
                if (user.length>1){
                    paranmeter.put(user[0],user[1]);
                }else {
                    paranmeter.put(user[0],null);
                }
            }
        } else {
            System.out.println("没有拆分");
            requstURI = uri;
        }
    }

    //2:解析消息头
    private void parseHanders() {
        System.out.println("HttpRequest:开始解析消息头..");
        try {
            String key;
            String value;
            //下面读取消息头后,将消息头的名字作为Key,消息头的值作为value保存到headers中
            while (true) {
                String line = readLine();
                //读取消息头时,如果只读取到了回车加换行符就应当停止读取
                if (line.isEmpty()) {//readLine单独读取CRLF返回值应当是空字符串
                    break;
                }
                System.out.println("消息头:" + line);
                String[] data = line.split(":");
                key = data[0];
                value = data[1];
                headers.put(key, value);
            }
            System.out.println(headers);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("HttpRequest:消息头解析完毕");
    }

    //3:解析消息正文
    private void parseContent() {
        System.out.println("HttpRequest:开始解析消息正文...");

        System.out.println("HttpRequest:消息正文解析完毕!");
    }

    private String readLine() throws IOException {
        /*
         * 当socket对象相同时,无论调用多少次getInputStream方法,获取回来的输入流
         * 总是同一个流,输出流也一样的
         * */
        InputStream in = socket.getInputStream();
        int d;
        char cur;
        char per = ' ';
        StringBuilder builder = new StringBuilder();
        while ((d = in.read()) != -1) {
            cur = (char) d;
            if (per == 13 && cur == 10) {
                break;
            }
            builder.append(cur);
            per = cur;
        }
        return builder.toString().trim();
    }

    public String getUrl() {
        return uri;
    }


    /**
     * 根据消息头的名字获取对应消息头的值
     *
     * @param name
     * @return
     */
    public String getHeader(String name) {
        return headers.get(name);
    }

    public String getRequstURI() {
        return requstURI;
    }

    public String getQustString() {
        return qustString;
    }

    /**
     * 根据参数名获取值
     * @param key
     * @return
     */
    public String getParanmeter(String key) {
        return paranmeter.get(key);
    }
}
