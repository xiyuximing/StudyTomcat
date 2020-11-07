package com.cy.mincat.service;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import javax.xml.parsers.SAXParser;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public class Bootstrap {

    private static int port = 8080;

    private static Map<String, Servlet> map = new HashMap<>();

    public void start() throws IOException {
        //加载web.xml
        load();

        /*
            完成Minicat 1.0版本
            需求：浏览器请求http://localhost:8080,返回一个固定的字符串到页面"Hello Minicat!"
         */
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("=====>>>Minicat start on port：" + port);
//        while (true) {
//            Socket socket = serverSocket.accept();
//            OutputStream outputStream = socket.getOutputStream();
//            String responseText = "Hello minCat !";
//            outputStream.write((HttpPotocolUtil.getHttpResponse200(responseText.length()) + responseText).getBytes());
//            socket.close();
//        }

        /**
         * 2.0需求：封装Request和Response对象，返回html静态资源⽂件
         */
//        while (true) {
//            Socket socket = serverSocket.accept();
//            Request request = new Request(socket.getInputStream());
//            Response response = new Response(socket.getOutputStream());
//            response.outputHtml(request.getUrl());
//            socket.close();
//        }

        /**
         * 3.0需求：可以请求动态资源（Servlet）
         *
         */
//        while (true) {
//            Socket socket = serverSocket.accept();
//            Request request = new Request(socket.getInputStream());
//            Response response = new Response(socket.getOutputStream());
//            try {
//
//                if (map.containsKey(request.getUrl())) {
//                    map.get(request.getUrl()).service(request, response);
//                } else {
//                    response.outputHtml(request.getUrl());
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            socket.close();
//        }

        //多线程改造
        ThreadFactory factory = Executors.defaultThreadFactory();
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(10, 50, 100, TimeUnit.SECONDS, new ArrayBlockingQueue<>(50), factory, new ThreadPoolExecutor.AbortPolicy());
        while (true) {
            Socket socket = serverSocket.accept();
            RequestProcessor processor = new RequestProcessor(socket, map);
            threadPoolExecutor.execute(processor);
        }
    }

    private void load() {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("web.xml");
        SAXReader saxReader = new SAXReader();
        try {
            Document document = saxReader.read(is);
            Element rootElement = document.getRootElement();
            List<Element> list = rootElement.selectNodes("//servlet");
            for (int i = 0; i < list.size(); i++) {
                Element element = list.get(i);
                Element servletNameEle = (Element) element.selectSingleNode("servlet-name");
                String servletName = servletNameEle.getStringValue();
                Element servletClassEle = (Element) element.selectSingleNode("servlet-class");
                String servletClass = servletClassEle.getStringValue();
                Element servletMappingEle = (Element) rootElement.selectSingleNode("/web-app/servlet-mapping[servlet-name='" + servletName + "']");
                Element urlPatternEle = (Element) servletMappingEle.selectSingleNode("url-pattern");
                String urlPattern = urlPatternEle.getStringValue();
                map.put(urlPattern, (MyServlet)Class.forName(servletClass).newInstance());
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 程序启动
     * @param args
     */
    public static void main(String[] args) {
        Bootstrap bootstrap = new Bootstrap();
        try {
            bootstrap.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
