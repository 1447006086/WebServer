package com.webserver.core;

import com.webserver.http.EmptyRequestException;
import com.webserver.http.HttpRequest;
import com.webserver.http.HttpResponse;

import java.io.*;
import java.net.Socket;


/**
 * 负责指定客户端进行HTTP交互
 * HTTP协议要求与客户端的交互规则采取一问一答方式.因此,处理客户端交互以3步形式完成:
 * 1:解析请求(一问)
 * 2:处理请求
 * 3:发送响应(一答)
 */
public class ClientHandler implements Runnable {
    private Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            //1解析请求
            HttpRequest httpRequest = new HttpRequest(socket);
            HttpResponse httpResponse = new HttpResponse(socket);

            //2处理请求
            String uri = httpRequest.getUrl();
            //TeduStore
            //
            File file = new File("webapps" + uri);

            //3发送响应
            //先发送一个固定的页面测试一个浏览器是否可以正常接收
            if (file.exists() && file.isFile()) {
                //1:发送状态行
                httpResponse.setFile(file);

                //2:发送响应头
            } else {
                System.out.println("资源不存在");
                file = new File("./webapps/root/404.html");
                httpResponse.setStatusCode(404);
                httpResponse.setStatusReason("NotFoud");
                httpResponse.setFile(file);
            }
            System.out.println("响应完毕!");
            httpResponse.flush();

            //3:发送响应正文(文件内容)

        } catch (EmptyRequestException e) {
            //什么都不用做,上面抛出该异常就是为了忽略处理和响应操作
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //处理完毕后与客户端断开连接
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
