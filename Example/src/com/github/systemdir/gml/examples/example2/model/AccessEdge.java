package com.github.systemdir.gml.examples.example2.model;

import org.jgrapht.graph.DefaultEdge;

/**
 * Created by hess on 9/24/2015.
 */
public class AccessEdge extends DefaultEdge {
    private final int accessQuantity;
    
    public AccessEdge(int accessQuantity) {
        this.accessQuantity = accessQuantity;
    }

    public int getAccessQuantity() {
        return accessQuantity;
    }
}
