package com.github.systemdir.gml.model;

import com.github.systemdir.gml.YedGmlWriter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

import static com.github.systemdir.gml.model.Tools.getHex;

/**
 * Created by hess on 9/23/2015.
 */
public class NodeGraphicDefinition extends GraphicDefinition {

    public enum Form {
        rectangle, roundrectangle, ellipse, triangle, diamond, octagon, parallelogram, hexagon, rectangle3d, trapezoid, trapezoid2
    }

    public enum LabelPlacement {
        center("c"),
        top("t"),
        bottom("b"),
        left("l"),
        right("r"),
        topLeft("tl"),
        topRight("tr"),
        bottomLeft("bl"),
        bottomRight("br");

        private final String placementString;

        LabelPlacement(String placementString) {
            this.placementString = placementString;
        }

        public String getPlacementString() {
            return placementString;
        }
    }
    
    public static class Builder extends GraphicDefinition.Builder<Builder>{
        private @NotNull Form form;
        private @NotNull Color fill;

        private @Nullable Rectangle position;
        private @Nullable Double topBorderInset;
        private @Nullable Double bottomBorderInset;
        private @Nullable Double leftBorderInset;
        private @Nullable Double rightBorderInset;
        private @Nullable LabelPlacement labelPlacement;

        public NodeGraphicDefinition build() {
            return new NodeGraphicDefinition(this);
        }

        /**
         * Rectangle node with NORMAL lines and black line colour and white background
         */
        public Builder() {
            this(Form.rectangle, Color.white, LineType.NORMAL, Color.black);
        }
        
        private Builder(@NotNull Form form, @NotNull Color fillColor, @NotNull LineType lineType, @NotNull Color lineColor) {
            super();
            instance=this;
            setLineColor(lineColor);
            setLineType(lineType);
            this.form = form;
            this.fill = fillColor;
        }

        public Builder setForm(@NotNull Form form) {
            this.form = form;
            return this;
        }
        
        public Builder setFill(@NotNull Color fill) {
            this.fill = fill;
            return this;
        }
        
        public Builder setPositionAndSize(@Nullable Rectangle position) {
            this.position = position;
            return this;
        }

        public Builder setTopBorderInset(@Nullable Double topBorderInset) {
            this.topBorderInset = topBorderInset;
            return this;
        }

        public Builder setBottomBorderInset(@Nullable Double bottomBorderInset) {
            this.bottomBorderInset = bottomBorderInset;
            return this;
        }

        public Builder setLeftBorderInset(@Nullable Double leftBorderInset) {
            this.leftBorderInset = leftBorderInset;
            return this;
        }

        public Builder setRightBorderInset(@Nullable Double rightBorderInset) {
            this.rightBorderInset = rightBorderInset;
            return this;
        }

        public void setLabelPlacement(@Nullable LabelPlacement labelPlacement) {
            this.labelPlacement = labelPlacement;
        }
    }

    private Rectangle posAndSize;
    private Form form;
    
    @Nullable
    private Double topBorderInset;
    @Nullable
    private Double bottomBorderInset;
    @Nullable
    private Double leftBorderInset;
    @Nullable
    private Double rightBorderInset;
    private Color fill;

    private final LabelPlacement lablePlacement;

    public NodeGraphicDefinition(Builder builder) {
        super(builder);
        this.fill = builder.fill;
        this.form = builder.form;
        
        
        this.lablePlacement = builder.labelPlacement;
        this.posAndSize = builder.position;
        this.topBorderInset = builder.topBorderInset;
        this.bottomBorderInset = builder.bottomBorderInset;
        this.leftBorderInset = builder.leftBorderInset;
        this.rightBorderInset = builder.rightBorderInset;
    }

    /**
     * Create GML for the node
     * @param printLabel add label formating informations
     * @return gml string
     */
    public String toString(boolean printLabel) {
        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append(YedGmlWriter.tab2).append("graphics\n").append(YedGmlWriter.tab2).append("[\n");

        if (posAndSize!=null) {
            stringBuilder.append(YedGmlWriter.tab3).append("x").append(YedGmlWriter.tab1).append(posAndSize.getX()).append("\n");
            stringBuilder.append(YedGmlWriter.tab3).append("y").append(YedGmlWriter.tab1).append(posAndSize.getY()).append("\n");
            stringBuilder.append(YedGmlWriter.tab3).append("w").append(YedGmlWriter.tab1).append(posAndSize.getWidth()).append("\n");
            stringBuilder.append(YedGmlWriter.tab3).append("h").append(YedGmlWriter.tab1).append(posAndSize.getHeight()).append("\n");
        }

        stringBuilder
                .append(YedGmlWriter.tab3)
                .append("type").append(YedGmlWriter.tab1).append("\"").append(form).append("\"").append("\n")
                .append(YedGmlWriter.tab3)
                .append("fill").append(YedGmlWriter.tab1).append("\"").append(getHex(fill)).append("\"").append("\n")
                .append(YedGmlWriter.tab3)
                .append("line").append(YedGmlWriter.tab1).append("\"").append(getHex(lineColor)).append("\"").append("\n");

        if (lineType != LineType.NORMAL) {
            stringBuilder.append(YedGmlWriter.tab3).append("outlineStyle").append(YedGmlWriter.tab1)
                    .append("\"").append(lineType.name()).append("\"")
                    .append("\n");
        }

        if (lineWidth!=null) {
            stringBuilder.append(YedGmlWriter.tab3).append("outlineWidth").append(YedGmlWriter.tab1).append("\"").append(lineWidth).append("\"").append("\n");
        }

        if (topBorderInset!=null) {
            stringBuilder.append(YedGmlWriter.tab3).append("topBorderInset").append(YedGmlWriter.tab1).append(topBorderInset).append("\n");
        }
        if (bottomBorderInset!=null) {
            stringBuilder.append(YedGmlWriter.tab3).append("bottomBorderInset").append(YedGmlWriter.tab1).append(bottomBorderInset).append("\n");
        }
        if (leftBorderInset!=null) {
            stringBuilder.append(YedGmlWriter.tab3).append("leftBorderInset").append(YedGmlWriter.tab1).append(leftBorderInset).append("\n");
        }
        if (rightBorderInset!=null) {
            stringBuilder.append(YedGmlWriter.tab3).append("rightBorderInset").append(YedGmlWriter.tab1).append(rightBorderInset).append("\n");
        }
        stringBuilder.append(YedGmlWriter.tab2).append("]\n");

        if(printLabel)
            stringBuilder.append(Tools.getLabelGraphics(labelColour,fontSize,fontStyle, labelBackground, lablePlacement, false));
        
        return stringBuilder.toString();
    }

}
