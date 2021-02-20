package com.webserver.http;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

/**
 * 响应对象,当前类的每一个实例用于表示给客户端发送的一个HTTP响应
 * 每个响应由三部分构成:
 * 状态行,响应头,响应正文(正文部分可以没有)
 */
public class HttpResponse {
    //状态行相关信息
    private int statusCode = 200;//状态代码默认值200,因为绝大多数请求实际应用中都能正确处理
    private String statusReason = "OK";

    //响应头相关信息

    //响应正文相关信息
    private File file;//响应正文对应的实体文件
    private Socket socket;

    public HttpResponse(Socket socket) {
        this.socket = socket;
    }

    /**
     * 将当前响应对象内容以标准的HTTP响应格式发送给客户端
     */
    public void flush() {
        /*
         * 发送一个响应时,按顺序发送状态行,响应头,响应正文
         * */
        System.out.println(file);
        sendStatusLine();
        sendHeaders();
        sendContent();
    }

    //发送一个响应的三个步骤:
    //1:发送状态行

        private void sendStatusLine() {
        System.out.println("HttpRespone:开始发送状态行...");
        try {
            OutputStream out = socket.getOutputStream();
            String line = "HTTP/1.1 " + statusCode + " " + statusReason;
            System.out.println("状态行:"+line);
            println(line);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("HttpRespone:状态行发送完毕!");
    }

    //发送响应头
    private void sendHeaders() {
        System.out.println("HttpRespone:开始发送响应头...");
        try {
            OutputStream out = socket.getOutputStream();
            String line = "Content-Type: text/html";
            println(line);

            line = "Content-Length: " + file.length();
            println(line);

            //单独发送CRLF表示响应头部分发送完毕!
            println("");
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("HttpRespone:响应头发送完毕!");
    }

    //发送响应正文
    private void sendContent() {
        System.out.println("HttpRespone:开始发送响应正文...");
        try (FileInputStream fis = new FileInputStream(file)) {
            OutputStream out = socket.getOutputStream();
            //创建文件输入流读取要发送的文件数据
            int len;
            byte[] buf = new byte[1024 * 10];
            while ((len = fis.read(buf)) != -1) {
                out.write(buf, 0, len);
            }
            System.out.println("响应完毕!");
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("HttpRespone:响应正文发送完毕!");
    }
    private void println(String line) throws IOException {
        OutputStream out = socket.getOutputStream();
        byte[] data=line.getBytes("ISO8859-1");
        out.write(data);
        out.write(13);//单独发送回车符
        out.write(10);//单独发送换行符
    }
    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusReason() {
        return statusReason;
    }

    public void setStatusReason(String statusReason) {
        this.statusReason = statusReason;
    }
}
