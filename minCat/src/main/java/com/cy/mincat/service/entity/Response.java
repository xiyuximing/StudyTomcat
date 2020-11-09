package com.cy.mincat.service.entity;

import com.cy.mincat.service.util.FileUtil;
import com.cy.mincat.service.util.HttpPotocolUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

public class Response {

    private OutputStream outputStream;

    public OutputStream getOutputStream() {
        return outputStream;
    }

    public void setOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public Response(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    /**
     * 输出指定内容
     * @param context
     */
    public void output(String context) throws IOException {
        outputStream.write(context.getBytes());
    }

    /**
     * 输出静态文件
     * @param path
     */
    public void outputHtml(String path) throws IOException {
        String absulotPath = FileUtil.getAbsulotFilePath(path);
        File file = new File(absulotPath);
        //如果文件不存在或者不是个文件，输出404
        if(!file.exists() || !file.isFile()) {
            output(HttpPotocolUtil.getHttpHeader404());
        } else {
            FileUtil.output(new FileInputStream(file), outputStream);
        }

    }
}
