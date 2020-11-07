package com.cy.mincat.service;

import java.io.IOException;

public class MyServlet extends HttpServlet {
    @Override
    public void doGet(Request request, Response response) {
        String context = "<h1>hello MinCat servlet</h1>";
        try {
            response.output(HttpPotocolUtil.getHttpResponse200(context.length()));
            response.output(context);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void doPost(Request request, Response response) {
        doGet(request, response);
    }

    @Override
    public void init() throws Exception {

    }

    @Override
    public void destory() throws Exception {

    }
}
