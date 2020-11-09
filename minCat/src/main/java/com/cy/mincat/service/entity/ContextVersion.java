package com.cy.mincat.service.entity;

import com.cy.mincat.service.mapper.MappedWrapper;

public class ContextVersion {

    private String path;

    private MappedWrapper[] mappedWrappers = new MappedWrapper[0];

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public MappedWrapper[] getMappedWrappers() {
        return mappedWrappers;
    }

    public void setMappedWrappers(MappedWrapper[] mappedWrappers) {
        this.mappedWrappers = mappedWrappers;
    }
}
