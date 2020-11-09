package com.cy.mincat.service.entity;

public class Host {
    private String name;

    private String appBase;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAppBase() {
        return appBase;
    }

    public void setAppBase(String appBase) {
        this.appBase = appBase;
    }

    public Host(String name, String appBase) {
        this.name = name;
        this.appBase = appBase;
    }
}
