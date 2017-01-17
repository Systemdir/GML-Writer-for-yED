package com.github.systemdir.gml.examples.example2.model;

/**
 * Created by Systemdir on 16/01/2017.
 */
public class Method extends Element {
    private AccessType accessType;

    public Method(String name,AccessType type) {
        super(name);
        this.accessType =type;
    }

    public AccessType getAccessType() {
        return accessType;
    }
}
