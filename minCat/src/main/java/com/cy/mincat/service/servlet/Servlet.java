package com.cy.mincat.service.servlet;

import com.cy.mincat.service.entity.Request;
import com.cy.mincat.service.entity.Response;

public interface Servlet {
    void init() throws Exception;

    void destory() throws Exception;

    void service(Request request, Response response) throws Exception;
}
