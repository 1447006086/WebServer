package com.webserver.http;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;

public class NotFount {
    private Socket socket;
    private File file;
    private String line;
    private String type;
    private String length;
    private NotFount(Socket socket, File file) throws IOException {
        this.socket=socket;
        this.file=file;
    }

    public String getling() throws IOException {
        OutputStream out = socket.getOutputStream();
        String line = "HTTP/1.1 200 OK";
        byte[] data = line.getBytes("ISO8859-1");
        out.write(data);
        out.write(13);//单独发送回车符
        out.write(10);//单独发送换行符
        return line;
    }

    public String getType() throws IOException {
        OutputStream out = socket.getOutputStream();
        type = "Content-Type: text/html";
        byte[] data = type.getBytes("ISO8859-1");
        out.write(data);
        out.write(13);//单独发送回车符
        out.write(10);//单独发送换行符
        return type;
    }
    public String getLength() throws IOException {
        OutputStream out = socket.getOutputStream();
        length = "Content-Length: " + file.length();
        byte[] data = length.getBytes("ISO8859-1");
        out.write(data);
        out.write(13);//单独发送回车符
        out.write(10);//单独发送换行符

        //单独发送CRLF表示响应头部分发送完毕!
        out.write(13);//单独发送回车符
        out.write(10);//单独发送换行符
        return length;
    }
}
