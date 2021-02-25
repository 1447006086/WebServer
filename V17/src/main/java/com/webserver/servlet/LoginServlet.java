package com.webserver.servlet;

import com.webserver.http.HttpRequest;
import com.webserver.http.HttpResponse;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class LoginServlet {
    public void service(HttpRequest request, HttpResponse response) {
        System.out.println("开始处理登录!");
        String name = request.getParanmeter("username").trim();
        String passworld = request.getParanmeter("passworld").trim();
        System.out.println(name + "," + passworld);
        try (RandomAccessFile raf = new RandomAccessFile("user.dat", "rw");) {
            byte[] data = new byte[32];
            for (int i = 0; i < raf.length() / 100; i++) {
                raf.seek(i * 100);
                raf.read(data);
                String username = new String(data, "utf-8").trim();
                raf.seek(i * 100 + 32);
                raf.read(data);
                String pass = new String(data, "utf-8").trim();
                if (username.equals(name) && pass.equals(passworld)) {
                    System.out.println("正确");
                    File file = new File("webapps/myweb/login_success.html");
                    response.setFile(file);
                    return;
                } else {
                    System.out.println("错误");
                    File file = new File("webapps/myweb/login_fail.html");
                    response.setFile(file);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("登录完毕");
    }
}
