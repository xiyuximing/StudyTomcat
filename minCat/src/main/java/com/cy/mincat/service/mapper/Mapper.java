package com.cy.mincat.service.mapper;

import java.util.HashMap;
import java.util.Map;

public class Mapper {

    private Map<String, MappedHost> hosts = new HashMap<>(8);

    public Map<String, MappedHost> getHosts() {
        return hosts;
    }

    public void setHosts(Map<String, MappedHost> hosts) {
        this.hosts = hosts;
    }
}
