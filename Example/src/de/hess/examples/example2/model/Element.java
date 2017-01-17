package de.hess.examples.example2.model;

/**
 * Created by Systemdir on 16/01/2017.
 */
public abstract class Element {
    private String name;

    public Element(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
