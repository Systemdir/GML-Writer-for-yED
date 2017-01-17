package de.hess.examples.example2;

import de.hess.examples.example2.model.*;
import de.hess.yed.model.EdgeGraphicDefinition;
import de.hess.yed.model.GraphicDefinition;
import de.hess.yed.model.NodeGraphicDefinition;
import de.hess.yed.model.YedGmlGraphicsProvider;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.Set;

/**
 * Defines the look and feel of the graph in the second example
 */
public class ExampleGraphicsProvider implements YedGmlGraphicsProvider<Element, AccessEdge, MethodGroup> {
    private final NodeGraphicDefinition dataDefinition;
    private final NodeGraphicDefinition methodDefinition;
    private final NodeGraphicDefinition groupDefinition;

    public ExampleGraphicsProvider() {
        // create reusable definitions
        dataDefinition = new NodeGraphicDefinition.Builder()
                .setFill(Color.white)
                .setForm(NodeGraphicDefinition.Form.diamond)
                .build();
        methodDefinition = new NodeGraphicDefinition.Builder()
                .setFill(Color.yellow)
                .setForm(NodeGraphicDefinition.Form.rectangle)
                .setFontStyle(GraphicDefinition.FontStyle.ITALIC)
                .build();
        groupDefinition = new NodeGraphicDefinition.Builder()
                .setFill(Color.gray)
                .setLineType(GraphicDefinition.LineType.DASHED)
                .build();
    }

    @Override
    public NodeGraphicDefinition getVertexGraphics(Element vertex) {
        if(vertex instanceof Data){
            return dataDefinition;
        }else{
            return methodDefinition;
        }
    }

    @Override
    public EdgeGraphicDefinition getEdgeGraphics(AccessEdge edge, Element edgeSource, Element edgeTarget) {
        if(edgeSource instanceof Method && edgeTarget instanceof Data){
            Method method = (Method)edgeSource;

            // define the look of the edges from a method to a data element
            GraphicDefinition.LineType lineType;
            EdgeGraphicDefinition.ArrowType arrowType;
            Color lineColor;
            switch (method.getAccessType()){
                default:
                case read:
                    lineType = GraphicDefinition.LineType.DASHED;
                    arrowType = EdgeGraphicDefinition.ArrowType.PLAIN;
                    lineColor = Color.black;
                    break;
                case update:
                    lineType = GraphicDefinition.LineType.NORMAL;
                    arrowType = EdgeGraphicDefinition.ArrowType.DIAMOND;
                    lineColor = Color.black;
                    break;
                case create:
                    lineType = GraphicDefinition.LineType.NORMAL;
                    arrowType = EdgeGraphicDefinition.ArrowType.PLAIN;
                    lineColor = Color.green;
                    break;
                case delete:
                    lineType = GraphicDefinition.LineType.NORMAL;
                    arrowType = EdgeGraphicDefinition.ArrowType.PLAIN;
                    lineColor = Color.red;
                    break;
            }
            
            return new EdgeGraphicDefinition.Builder()
                    .setTargetArrow(arrowType)
                    .setLineType(lineType)
                    .setLineColour(lineColor)
                    .build();
        }else{
            // default edge
            return new EdgeGraphicDefinition.Builder().build();
        }
    }

    @Nullable
    @Override
    public NodeGraphicDefinition getGroupGraphics(MethodGroup group, Set<Element> groupElements) {
        return groupDefinition;
    }
}
