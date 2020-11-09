package com.cy.mincat.service.mapper;

import com.cy.mincat.service.entity.Context;
import com.cy.mincat.service.servlet.Servlet;

import java.util.HashMap;
import java.util.Map;

public class MappedContext extends MapElement<Context>{

    private Map<String, Servlet> wrapperMap = new HashMap<>(8);

    public Map<String, Servlet> getWrapperMap() {
        return wrapperMap;
    }

    public void setWrapperMap(Map<String, Servlet> wrapperMap) {
        this.wrapperMap = wrapperMap;
    }

    public MappedContext(String name, Context object) {
        super(name, object);
    }
}
