package com.cy.mincat.service;

import java.io.IOException;
import java.io.InputStream;

public class Request {

    /**
     * 请求路径
     */
    private String url;

    /**
     * 请求方法
     */
    private String method;

    /**
     * 输入流
     */
    private InputStream inputStream;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public Request(InputStream inputStream) throws IOException {
        this.inputStream = inputStream;
        init();
    }

    private void init() throws IOException {
        int count = 0;
        while (count == 0) {
            count = inputStream.available();
        }
        byte[] bytes = new byte[count];
        inputStream.read(bytes);
        String requestStr = new String(bytes);
        // 获取第一行请求头信息
        String firstLineStr = requestStr.split("\\n")[0];  // GET / HTTP/1.1
        String[] strings = firstLineStr.split(" ");
        this.method = strings[0];
        this.url = strings[1];
    }

}
