package com.github.systemdir.gml.model;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

/**
 * Created by hess on 9/23/2015.
 */
public abstract class GraphicDefinition {
    public static abstract class Builder<T> {
        private Color labelBackground;
        private Integer fontSize;
        private Color labelColour;
        private NodeGraphicDefinition.FontStyle fontStyle;
        private Color lineColor;
        private LineType lineType;
        private Integer lineWidth;

        protected T instance;
        
        public T setLabelBackground(Color labelBackground) {
            this.labelBackground = labelBackground;
            return instance;
        }

        public T setFontSize(Integer fontSize) {
            this.fontSize = fontSize;
            return instance;
        }

        public T setLabelColour(Color labelColour) {
            this.labelColour = labelColour;
            return instance;
        }

        public T setFontStyle(FontStyle fontStyle) {
            this.fontStyle = fontStyle;
            return instance;
        }

        public T setLineColor(Color lineColor) {
            this.lineColor = lineColor;
            return instance;
        }

        public T setLineType(LineType lineType) {
            this.lineType = lineType;
            return instance;
        }

        public T setLineWidth(Integer lineWidth) {
            this.lineWidth = lineWidth;
            return instance;
        }
    }

    public enum FontStyle {
        PLAIN, BOLD, ITALIC, BOLDITALIC
    }
    
    public enum LineType {
        NORMAL, DASHED, DOTTED, DASHED_DOTTED
    }
    
    @Nullable
    protected Color labelBackground;
    @Nullable
    protected Integer fontSize;
    @Nullable
    protected Color labelColour;
    @NotNull
    protected NodeGraphicDefinition.FontStyle fontStyle;
    @NotNull
    protected LineType lineType;
    @Nullable
    protected Integer lineWidth;
    @NotNull
    protected Color lineColor;

    protected GraphicDefinition(Builder builder) {
        this.lineColor=builder.lineColor;
        this.lineType = builder.lineType;
        this.lineWidth = builder.lineWidth;
        this.labelBackground = builder.labelBackground;
        this.fontSize = builder.fontSize;
        this.fontStyle = builder.fontStyle;
        this.labelColour = builder.labelColour;
    }
}
