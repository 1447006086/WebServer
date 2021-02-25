package com.webserver.servlet;

import com.webserver.http.HttpRequest;
import com.webserver.http.HttpResponse;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;

/**
 * Servlet是JavaEE标准中的一个接口,意思是运行在服务端的小程序
 * 我们用它来处理某个具体的请求
 * <p>
 * 当前Servlet用于处理用户注册业务
 */
public class RegServlet {

    public void service(HttpRequest request, HttpResponse response) {
        System.out.println("RegServket:开始处理用户注册...");
        /*
         * 1:通过request获取用户在注册页面上输入的注册信息(表单上的信息)
         * 2:将用户的注册信息写入文件user.dat中
         * 3:设置response给客户端响应注册结果页面
         * */
        String username = request.getParanmeter("username");
        String passworld = request.getParanmeter("passworld");
        String nickname = request.getParanmeter("nickname");
        String ageStr = request.getParanmeter("age");
        /*
         * 必要的验证工作,如果上述四项有空的,或者年龄不是一个数字时,直接响应给客户端一个注册错误
         * 的提示页面:reg_info_error.html,里面居中显示一行字:注册信息输入有误,请重新注册
         * :注该页面也放在webapps/myweb这个网络应用中
         * */
        if (username == null || passworld == null || nickname == null || ageStr == null || !ageStr.matches("[0-9]+")) {
            File file = new File("./webapps/myweb/reg_info_error.html");
            response.setFile(file);
            System.out.println("注册失败");
        } else {
            int age = Integer.parseInt(ageStr);
            System.out.println(username + "," + passworld + "," + nickname + "," + age);
            /*
             * 2
             * 每条用户信息占用100字节,其中用户名,密码,昵称为字符串各占32字节,年龄为int值占4字节
             * */
            try (RandomAccessFile ref = new RandomAccessFile("user.dat", "rw")) {
                /*
                * 验证是否为重复用户
                * 先读取user.dat文件中现有的所有用户的名字,并与本次注册的用户名对比,如果存在则
                * 直接响应页面:hava_user.html,居中显示一行字:该用户已存在,请重新注册
                * 否则才进行注册操作
                * */
                for (int i = 0; i < ref.length()/100; i++) {
                    ref.seek(i*100);
                    byte[] data=new byte[32];
                    ref.read(data);
                    String usre=new String(data,"utf-8").trim();
                    if (usre.equals(username)){
                        System.out.println("用户名重复!"+usre+","+username);
                        File file=new File("./webapps/myweb/hava_user.html");
                        response.setFile(file);
                        return;
                    }
                }
                ref.seek(ref.length());
                byte[] data = username.getBytes("UTF-8");
                data = Arrays.copyOf(data, 32);
                ref.write(data);

                data = passworld.getBytes("utf-8");
                data = Arrays.copyOf(data, 32);
                ref.write(data);

                data = nickname.getBytes("utf-8");
                data = Arrays.copyOf(data, 32);
                ref.write(data);

                ref.writeInt(age);
                System.out.println(ref.length());
                System.out.println("注册成功!");
                File file = new File("./webapps/myweb/reg_success.html");
                response.setFile(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("RegServket:用户注册处理完毕!");
    }

}
