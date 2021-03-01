package com.webserver.servlet;

import com.webserver.http.HttpRequest;
import com.webserver.http.HttpResponse;
import com.webserver.vo.User;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.FileTemplateResolver;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

public class UpdateServlet {
    private List<User> list = new ArrayList<>();
    public void service(HttpRequest request, HttpResponse response) {
        System.out.println("开始处理修改业务...");
        String user = request.getParanmeter("username");

        try (RandomAccessFile raf = new RandomAccessFile("user.dat", "rw")) {
            byte[] data = new byte[32];
            for (int i = 0; i < raf.length(); i++) {
                raf.seek(i * 100);
                raf.read(data);
                String username = new String(data, "utf-8").trim();
                if (user.equals(username)) {
                    System.out.println("找到了");
                    raf.read(data);
                    String passworld = new String(data, "utf-8").trim();
                    raf.read(data);
                    String nickname = new String(data, "utf-8").trim();
                    int age = raf.readInt();
                    System.out.println(user + "," + passworld + "," + nickname + "," + age);
                    list.add(new User(user, passworld, nickname, age));

                    //2.1:创建Context实例,thymeleaf提供的,用于保存所有在页面上要显示的数据
                    Context context = new Context();
                    //将存放所有用户信息的List集合存入Context
                    context.setVariable("list", list);
                    //2.2:初始化thymeleaf模板引擎
                    //模板解释器,用来告知模板引擎模板的相关情况(模板就是要结合的静态页面)
                    FileTemplateResolver resolver = new FileTemplateResolver();
                    resolver.setTemplateMode("html");
                    resolver.setCharacterEncoding("utf-8");
                    //实例化模板引擎
                    TemplateEngine te = new TemplateEngine();
                    te.setTemplateResolver(resolver);

                    String html=te.process("./webapps/myweb/update.html", context);
                    PrintWriter pw=response.getWriter();
                    System.out.println(list);
                    pw.println(html);
                    response.setContentType("text/html");
                    return;
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
