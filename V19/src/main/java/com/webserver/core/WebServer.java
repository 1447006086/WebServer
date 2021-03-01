package com.webserver.core;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 小鸟WebServer
 * 实现Tomcat的基础功能的一个web容器
 * web容器的作用:
 * 1:web容器是一个Web服务器端程序,负责与客户端(通常是浏览器)进行交互
 * 2:完成与客户端的TCP连接及数据交互.
 * 3:基于HTTP协议与客户端进行应用交互,使得浏览器可以访问Web容器中部署的不同网络应用(Webapp)
 * 的页面,资源,功能等.
 * 4:可以管理部署多个不同的网络应用
 */
public class WebServer {
    private ServerSocket serverSocket;
    private ExecutorService threadPool;
    public WebServer() {
        try {
            System.out.println("正在启动服务器!");
            serverSocket = new ServerSocket(8088);
            threadPool= Executors.newFixedThreadPool(50);
            System.out.println("服务端启动完毕!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        try {
            System.out.println("等待客户端连接...");
            while(true){
            Socket socket = serverSocket.accept();
            System.out.println("一个客户端连接了!");
                //启动一个线程与该客户端交互
                ClientHandler clientHandler=new ClientHandler(socket);
                threadPool.execute(clientHandler);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        WebServer webServer = new WebServer();
        webServer.start();
    }
}
