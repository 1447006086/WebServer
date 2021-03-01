package com.webserver.servlet;

import com.webserver.http.HttpRequest;
import com.webserver.http.HttpResponse;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class LoginServlet extends HttpServlet {
    private static Logger log=Logger.getLogger(LoginServlet.class);
    public void doPost(HttpRequest request, HttpResponse response) {
        //info是用来记录一般信息
        log.info("开始处理登录!");
        String name = request.getParanmeter("username").trim();
        String passworld = request.getParanmeter("passworld").trim();
        System.out.println(name + "," + passworld);
        try (RandomAccessFile raf = new RandomAccessFile("user.dat", "rw");) {
            for (int i = 0; i < raf.length() / 100; i++) {
                byte[] data = new byte[32];
                raf.seek(i * 100);
                raf.read(data);
                String username = new String(data, "utf-8").trim();
                raf.seek(i * 100 + 32);
                raf.read(data);
                String pass = new String(data, "utf-8").trim();
                System.out.println(username+"\t"+pass);
                if (username.equals(name) && pass.equals(passworld)) {
                    File file = new File("webapps/myweb/login_success.html");
                    response.setFile(file);
                    return;
                } else {
                    File file = new File("webapps/myweb/login_fail.html");
                    response.setFile(file);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            //error用来记录错误
            log.error(e.getMessage(),e);
        }
        System.out.println("登录完毕");
    }

    @Override
    public void doGet(HttpRequest request, HttpResponse response) {

    }

}
