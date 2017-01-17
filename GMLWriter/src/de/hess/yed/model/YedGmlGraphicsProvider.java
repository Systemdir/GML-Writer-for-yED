package de.hess.yed.model;

import org.jetbrains.annotations.Nullable;

import java.util.Set;

/**
 * Created by hess on 9/23/2015.
 */
public interface YedGmlGraphicsProvider<V, E, G> {
    NodeGraphicDefinition getVertexGraphics(V vertex);
    EdgeGraphicDefinition getEdgeGraphics(E edge, V edgeSource, V edgeTarget);
    @Nullable NodeGraphicDefinition getGroupGraphics(G group, Set<V> groupElements);
}
