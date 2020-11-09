package com.cy.servlet;

import com.cy.mincat.service.entity.Request;
import com.cy.mincat.service.entity.Response;
import com.cy.mincat.service.servlet.HttpServlet;
import com.cy.mincat.service.util.HttpPotocolUtil;

import java.io.IOException;

public class DemoTwoServlet extends HttpServlet {

    @Override
    public void doGet(Request request, Response response) {
        String context = "<h1>this Demo two</h1>";
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
