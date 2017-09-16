package com.github.systemdir.gml.model;

import com.github.systemdir.gml.YedGmlWriter;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

/**
 * Created by Systemdir on 16/01/2017.
 */
class Tools {
    /**
     * Create GML for formatting labels
     * @return gml string
     */
    static String getLabelGraphics(@Nullable Color labelColour, @Nullable Integer fontSize, @Nullable GraphicDefinition.FontStyle fontStyle, @Nullable Color labelBackground, @Nullable NodeGraphicDefinition.LabelPlacement labelPlacement, boolean center) {
        if (labelColour != null || fontSize != null || (fontStyle != null && fontStyle != GraphicDefinition.FontStyle.PLAIN) || labelPlacement != null || center) {
            StringBuilder stringBuilder=new StringBuilder();
            
            stringBuilder.append(YedGmlWriter.tab2).append("LabelGraphics\n").append(
                    YedGmlWriter.tab2).append("[\n");

            if (labelColour != null) {
                stringBuilder.append(YedGmlWriter.tab3).append("color").append(YedGmlWriter.tab1)
                        .append("\"").append(getHex(labelColour)).append("\"").append("\n");
            }

            if (labelBackground != null) {
                stringBuilder.append(YedGmlWriter.tab3).append("fill").append(YedGmlWriter.tab1)
                        .append("\"").append(getHex(labelBackground)).append("\"").append("\n");
            }

            if (fontSize != null) {
                stringBuilder.append(YedGmlWriter.tab3).append("fontSize").append(YedGmlWriter.tab1)
                        .append(fontSize).append("\n");
            }

            if (fontStyle != null && fontStyle != GraphicDefinition.FontStyle.PLAIN) {
                stringBuilder.append(YedGmlWriter.tab3).append("fontStyle").append(YedGmlWriter.tab1)
                        .append("\"").append(fontStyle.name()).append("\"").append("\n");
            }

            // used for labels on vertices
            if (labelPlacement != null) {
                stringBuilder.append(YedGmlWriter.tab3).append("anchor").append(YedGmlWriter.tab1)
                        .append("\"").append(labelPlacement.getPlacementString()).append("\"").append("\n");
            }

            // used for labels on edges
            if (center) {
                stringBuilder.append(YedGmlWriter.tab3).append("model").append(YedGmlWriter.tab1).append("\"centered\"\n");
                stringBuilder.append(YedGmlWriter.tab3).append("position").append(YedGmlWriter.tab1).append("\"center\"\n");
            }
            
            stringBuilder.append(YedGmlWriter.tab2).append("]\n");
            return stringBuilder.toString();
        }else{
            return "";
        }
    }

    /**
     * create rgba hex from a java color
     */
    static String getHex(Color color) {
        return String.format("#%02x%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    }
}
