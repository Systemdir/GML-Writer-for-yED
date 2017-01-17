package com.github.systemdir.gml.examples.example1;

import de.hess.yed.model.EdgeGraphicDefinition;
import de.hess.yed.model.GraphicDefinition;
import de.hess.yed.model.NodeGraphicDefinition;
import de.hess.yed.model.YedGmlGraphicsProvider;
import org.jetbrains.annotations.Nullable;
import org.jgrapht.graph.DefaultEdge;

import java.awt.*;
import java.util.Set;

/**
 * Defines the look and feel of the graph in the first example
 */
public class ExampleGraphicsProvider implements YedGmlGraphicsProvider<String, DefaultEdge, Object>{

    @Override
    public NodeGraphicDefinition getVertexGraphics(String vertex) {
        return new NodeGraphicDefinition.Builder()
                .setFill(Color.LIGHT_GRAY)
                .setLineColor(Color.black)
                .setFontStyle(GraphicDefinition.FontStyle.ITALIC)
                .build();
    }

    @Override
    public EdgeGraphicDefinition getEdgeGraphics(DefaultEdge edge, String edgeSource, String edgeTarget) {
        return new EdgeGraphicDefinition.Builder()
                .setTargetArrow(EdgeGraphicDefinition.ArrowType.SHORT_ARROW)
                .setLineType(GraphicDefinition.LineType.DASHED)
                .build();
    }

    // we have no groups in this example
    @Nullable
    @Override
    public NodeGraphicDefinition getGroupGraphics(Object group, Set<String> groupElements) {
        return null;
    }

}
