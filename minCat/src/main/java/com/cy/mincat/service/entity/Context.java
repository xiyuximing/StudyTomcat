package com.cy.mincat.service.entity;

public class Context {
    private String docBase;
    private String path;

    public String getDocBase() {
        return docBase;
    }

    public void setDocBase(String docBase) {
        this.docBase = docBase;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Context(String docBase, String path) {
        this.docBase = docBase;
        this.path = path;
    }
}
