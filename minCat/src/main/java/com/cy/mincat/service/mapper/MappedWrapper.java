package com.cy.mincat.service.mapper;

import com.cy.mincat.service.servlet.Servlet;

import java.util.HashMap;
import java.util.Map;

public class MappedWrapper extends MapElement<Void> {
    private Map<String, Servlet> servletMap = new HashMap<>(8);

    public Map<String, Servlet> getServletMap() {
        return servletMap;
    }

    public void setServletMap(Map<String, Servlet> servletMap) {
        this.servletMap = servletMap;
    }

    public MappedWrapper(String name, Void object) {
        super(name, object);
    }
}
