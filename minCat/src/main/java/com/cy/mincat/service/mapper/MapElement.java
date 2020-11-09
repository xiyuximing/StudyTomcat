package com.cy.mincat.service.mapper;

public abstract class MapElement<T> {
    public final String name;
    public final T object;

    public MapElement(String name, T object) {
        this.name = name;
        this.object = object;
    }
}
