package de.hess.yed.model;

import de.hess.yed.YedGmlWriter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

import static de.hess.yed.model.Tools.getHex;

/**
 * Created by hess on 9/23/2015.
 */
public class EdgeGraphicDefinition extends GraphicDefinition {

    public enum ArrowType {
        NONE(null), DELTA("delta"), WHITE_DELTA("white_delta"), DIAMOND("diamond"), WHITE_DIAMOND("white_diamond"), SHORT_ARROW("short"), PLAIN("PLAIN"), CIRCLE("circle"), TRANSPARENT_CIRCLE("transparent_circle");

        private String representedBy;

        ArrowType(String representedBy) {
            this.representedBy = representedBy;
        }

        public String getRepresentedBy() {
            return representedBy;
        }
    }

    private ArrowType sourceArrow;
    private ArrowType targetArrow;
    //@Nullable
    //private String lable;

    public static class Builder extends GraphicDefinition.Builder<Builder>{
        @NotNull private ArrowType sourceArrow;
        @NotNull private ArrowType targetArrow;
        //@Nullable private String label;
        @Nullable Integer lineWidth;

        public EdgeGraphicDefinition build(){
            return new EdgeGraphicDefinition(this);
        }

        /**
         * Straight line, no arrows
         */
        public Builder() {
            this(ArrowType.NONE,ArrowType.NONE,Color.black,LineType.NORMAL);
        }

        private Builder(@NotNull ArrowType sourceArrow, @NotNull ArrowType targetArrow, @NotNull Color edgeColour, @NotNull LineType lineType) {
            instance=this;
            
            this.sourceArrow = sourceArrow;
            this.targetArrow = targetArrow;
            setLineColor(edgeColour);
            setLineType(lineType);
        }
        
        public Builder setSourceArrow(@NotNull ArrowType sourceArrow) {
            this.sourceArrow = sourceArrow;
            return this;
        }

        public Builder setTargetArrow(@NotNull ArrowType targetArrow) {
            this.targetArrow = targetArrow;
            return this;
        }
        
        public Builder setLineColour(@NotNull Color lineColour) {
            setLineColor(lineColour);
            return this;
        }
        
        public Builder setLineWidth(@Nullable Integer lineWidth) {
            this.lineWidth = lineWidth;
            return this;
        }
    }

    public EdgeGraphicDefinition(Builder builder) {
        super(builder);
        this.sourceArrow = builder.sourceArrow;
        this.targetArrow = builder.targetArrow;
        //this.lable = builder.label;
    }

    /**
     * Create GML for the edge
     * @param printLabel add label formating informations
     * @return gml string
     */
    public String toString(boolean printLabel) {
        StringBuilder stringBuilder=new StringBuilder();
        
        if(printLabel) {
            stringBuilder.append(Tools.getLabelGraphics(labelColour,fontSize,fontStyle, labelBackground, null, true));
        }

        stringBuilder.append(YedGmlWriter.tab2).append("graphics\n").append(YedGmlWriter.tab2).append("[\n");

        stringBuilder.append(YedGmlWriter.tab3).append("fill").append(YedGmlWriter.tab1)
                .append("\"").append(getHex(lineColor)).append("\"")
                .append("\n");

        if (lineWidth!=null) {
            stringBuilder.append(YedGmlWriter.tab3).append("width").append(YedGmlWriter.tab1)
                    .append("\"").append(lineWidth).append("\"")
                    .append("\n");
        }

        if (lineType != LineType.NORMAL) {
            stringBuilder.append(YedGmlWriter.tab3).append("style").append(YedGmlWriter.tab1)
                    .append("\"").append(lineType.name()).append("\"")
                    .append("\n");
        }

        if (sourceArrow != ArrowType.NONE) {
            stringBuilder.append(YedGmlWriter.tab3).append("sourceArrow").append(YedGmlWriter.tab1)
                    .append("\"").append(sourceArrow.getRepresentedBy()).append("\"")
                    .append("\n");
        }

        if (targetArrow != ArrowType.NONE) {
            stringBuilder.append(YedGmlWriter.tab3).append("targetArrow").append(YedGmlWriter.tab1)
                    .append("\"").append(targetArrow.getRepresentedBy()).append("\"")
                    .append("\n");
        }

        stringBuilder.append(YedGmlWriter.tab2).append("]\n");
        return stringBuilder.toString();
    }


}
