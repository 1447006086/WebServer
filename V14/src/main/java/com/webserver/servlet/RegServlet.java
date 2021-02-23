package com.webserver.servlet;

import com.webserver.http.HttpRequest;
import com.webserver.http.HttpResponse;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;

/**
 * Servlet是JavaEE标准中的一个接口,意思是运行在服务端的小程序
 * 我们用它来处理某个具体的请求
 *
 * 当前Servlet用于处理用户注册业务
 */
public class RegServlet {

    public void service(HttpRequest request, HttpResponse response){
        System.out.println("RegServket:开始处理用户注册...");
        /*
        * 1:通过request获取用户在注册页面上输入的注册信息(表单上的信息)
        * 2:将用户的注册信息写入文件user.dat中
        * 3:设置response给客户端响应注册结果页面
        * */
        String username=request.getParanmeter("username");
        String passworld=request.getParanmeter("passworld");
        String nickname=request.getParanmeter("nickname");
        String ageStr=request.getParanmeter("age");
        int age=Integer.parseInt(ageStr);
        System.out.println(username+","+passworld+","+nickname+","+age);
        /*
        * 2
        * 每条用户信息占用100字节,其中用户名,密码,昵称为字符串各占32字节,年龄为int值占4字节
        * */
        try(RandomAccessFile ref=new RandomAccessFile("user.dat","rw")){
            ref.seek(ref.length());

            byte[] data=username.getBytes("UTF-8");
            data= Arrays.copyOf(data,32);
            ref.write(data);

            data=passworld.getBytes("utf-8");
            data=Arrays.copyOf(data,32);
            ref.write(data);

            data=nickname.getBytes("utf-8");
            data=Arrays.copyOf(data,32);
            ref.write(data);

            ref.writeInt(age);

        }catch (IOException e){
            e.printStackTrace();
        }
        System.out.println("RegServket:用户注册处理完毕!");
    }

}
