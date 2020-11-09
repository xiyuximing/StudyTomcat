package com.cy.mincat.service;

import com.cy.mincat.service.entity.Context;
import com.cy.mincat.service.entity.Host;
import com.cy.mincat.service.entity.RequestProcessor;
import com.cy.mincat.service.mapper.MappedContext;
import com.cy.mincat.service.mapper.MappedHost;
import com.cy.mincat.service.servlet.MyServlet;
import com.cy.mincat.service.servlet.Servlet;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public class Bootstrap {

    private static int port = 8080;

    private static Map<String, Servlet> map = new HashMap<>();

    private static Map<String, MappedHost> hostMap = new HashMap<>(8);

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

    private void load(InputStream is, MappedContext mappedContext) {
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
                mappedContext.getWrapperMap().put(urlPattern, (MyServlet)Class.forName(servletClass).newInstance());
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
     * 加载service.xml配置文件
     */
    private void loadServer() {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("server.xml");
        SAXReader saxReader = new SAXReader();
        try {
            Document document = saxReader.read(is);
            Element rootElement = document.getRootElement();
            List<Element> list = rootElement.selectNodes("//service");
            for (int i = 0; i < list.size(); i++) {
                Element element = list.get(i);
                Element connectorEle = (Element) element.selectSingleNode("Connector");
                port = Integer.parseInt(connectorEle.attributeValue("port"));
                Element engineEle = (Element) element.selectSingleNode("Engine");
                List<Element> hostEles = engineEle.selectNodes("Host");
                for (Element hostEle : hostEles) {
                    String name = hostEle.attributeValue("name");
                    String appBase = hostEle.attributeValue("appBase");
                    Host host = new Host(name, appBase);
                    MappedHost mappedHost = new MappedHost(name, host);
                    List<Element> contextEles = hostEle.selectNodes("Context");
                    for (Element contextEle : contextEles) {
                        loadContext(contextEle, mappedHost);
                    }
                    hostMap.put(name, mappedHost);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static final String WEBINF = "WEB-INF" + File.separator + "web.xml";

    /**
     * 解析context标签内容
     * @param contextEle
     * @param mappedHost
     */
    private void loadContext(Element contextEle, MappedHost mappedHost) throws Exception{
        String docBase = contextEle.attributeValue("docBase");
        String path = contextEle.attributeValue("path");
        Context context = new Context(docBase, path);
        MappedContext mappedContext = new MappedContext(path, context);
        //解析各项目下web.xml,位置在WEB-INF目录下
        File file = new File(docBase + File.separator + WEBINF);
        load(new FileInputStream(file), mappedContext);
        mappedHost.getContextMap().put(path, mappedContext);
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
