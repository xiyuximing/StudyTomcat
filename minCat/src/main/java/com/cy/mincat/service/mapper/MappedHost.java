package com.cy.mincat.service.mapper;

import com.cy.mincat.service.entity.Host;

import java.util.HashMap;
import java.util.Map;

public class MappedHost extends MapElement<Host> {

   private Map<String, MappedContext> contextMap = new HashMap<>(8);

    public MappedHost(String name, Host object) {
        super(name, object);
    }

    public Map<String, MappedContext> getContextMap() {
        return contextMap;
    }

    public void setContextMap(Map<String, MappedContext> contextMap) {
        this.contextMap = contextMap;
    }
}
