package com.cy.mincat.service.util;

import java.text.MessageFormat;

/**
 * http协议工具类
 */
public class HttpPotocolUtil {

    private static final String RESPONSETXT_200 = "HTTP/1.1 200 OK \n" +
            "Content-Type: text/html \n" +
            "Content-Length: {0} \n" +
            "\r\n";

    /**
     * 200相应码
     * @param length
     * @return
     */
    public static String getHttpResponse200(Integer length) {
        return MessageFormat.format(RESPONSETXT_200, length);
    }

    /**
     * 404响应码
     * @return
     */
    public static String getHttpHeader404() {
        String str404 = "<h1>404 not found</h1>";
        return "HTTP/1.1 404 NOT Found \n" +
                "Content-Type: text/html \n" +
                "Content-Length: " + str404.length() + " \n" +
                "\r\n" + str404;
    }
}
