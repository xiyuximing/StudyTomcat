package com.cy.mincat.service.entity;

import com.cy.mincat.service.servlet.Servlet;

import java.net.Socket;
import java.util.Map;

public class RequestProcessor implements Runnable{

    private Socket socket;

    private Map<String, Servlet> servletMap;

    public RequestProcessor(Socket socket, Map<String, Servlet> servletMap) {
        this.socket = socket;
        this.servletMap = servletMap;
    }

    @Override
    public void run() {
            try {
                Request request = new Request(socket.getInputStream());
                Response response = new Response(socket.getOutputStream());
                try {

                    if (servletMap.containsKey(request.getUrl())) {
                        servletMap.get(request.getUrl()).service(request, response);
                    } else {
                        response.outputHtml(request.getUrl());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
    }
}

