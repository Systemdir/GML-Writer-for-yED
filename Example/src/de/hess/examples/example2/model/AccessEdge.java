package de.hess.examples.example2.model;

import de.hess.examples.example2.model.AccessType;
import org.jetbrains.annotations.Nullable;
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
