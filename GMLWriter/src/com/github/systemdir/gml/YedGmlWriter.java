/* ==========================================
 * JGraphT : a free Java graph-theory library
 * ==========================================
 *
 * Project Info:  http://jgrapht.sourceforge.net/
 * Project Creator:  Barak Naveh (http://sourceforge.net/users/barak_naveh)
 *
 * (C) Copyright 2003-2008, by Barak Naveh and Contributors.
 *
 * This program and the accompanying materials are dual-licensed under
 * either
 *
 * (a) the terms of the GNU Lesser General Public License version 2.1
 * as published by the Free Software Foundation, or (at your option) any
 * later version.
 *
 * or (per the licensee's choosing)
 *
 * (b) the terms of the Eclipse Public License v1.0 as published by
 * the Eclipse Foundation.
 */
/* ------------------
 * GmlExporter.java
 * ------------------
 * (C) Copyright 2006, by Dimitrios Michail.
 *
 * Original Author:  Dimitrios Michail <dmichail@yahoo.com>
 *
 * $Id$
 *
 * Changes
 * -------
 * 15-Dec-2006 : Initial Version (DM);
 *
 */
package com.github.systemdir.gml;

import com.github.systemdir.gml.model.EdgeGraphicDefinition;
import com.github.systemdir.gml.model.NodeGraphicDefinition;
import com.github.systemdir.gml.model.UniqueIntIdFunction;
import com.github.systemdir.gml.model.YedGmlGraphicsProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jgrapht.Graph;

import java.io.PrintWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

import static com.github.systemdir.gml.YedGmlWriter.PrintLabels.*;


/**
 * Exports a graph into a GML file (Graph Modelling Language) for yED.
 * <p>For a description of the format see <a
 * href="http://www.infosun.fmi.uni-passau.de/Graphlet/GML/">
 * http://www.infosun.fmi.uni-passau.de/Graphlet/GML/</a>.</p>
 *
 * @author (GML writer) Dimitrios Michail
 * @author (yED extension) Hayato Hess
 */
public class YedGmlWriter<V, E, G> {

    public static class Builder<V1, E1, G1> {
        Function<G1, String> groupLabelProvider;
        Map<G1, ? extends Set<V1>> groupMapping;
        Function<Object, String> vertexIDProvider;
        Function<V1, String> vertexLabelProvider;
        Function<E1, String> edgeIDProvider;
        Function<E1, String> edgeLabelProvider;
        YedGmlGraphicsProvider<V1, E1, G1> graphicsProvider;

        EnumSet<PrintLabels> printLabels;

        public Builder(YedGmlGraphicsProvider<V1, E1, G1> graphProvider, PrintLabels... printLabels) {
            this.graphicsProvider = graphProvider;

            EnumSet<PrintLabels> temp = EnumSet.noneOf(PrintLabels.class); // make an empty enumset
            temp.addAll(Arrays.asList(printLabels));
            this.printLabels = temp;
        }

        /**
         * Group nodes together
         *
         * @param groupMapping Maps groups to the vertices they belong to. Map must not contain null values.
         * @param groupLabelProvider Optional label function for the group. When null, toString() is used for generating group labels.
         * @return this
         */
        public Builder<V1, E1, G1> setGroups(Map<G1, ? extends Set<V1>> groupMapping, Function<G1, String> groupLabelProvider) {
            this.groupMapping = groupMapping;
            this.groupLabelProvider = groupLabelProvider;
            return this;
        }

        /**
         * Sets a id provider for the vertexes.
         * <p>
         * This will default to {@link com.github.systemdir.gml.model.UniqueIntIdFunction} if not set.
         *
         * @param vertexIDProvider The function must not return null
         * @return this
         */
        public Builder<V1, E1, G1> setVertexIDProvider(Function<Object, String> vertexIDProvider) {
            this.vertexIDProvider = vertexIDProvider;
            return this;
        }

        /**
         * For generating vertex labels.
         * <p>
         * If null, vertex labels will be generated using the toString() method of the vertex object.
         *
         * @param vertexLabelProvider The function must not return null
         * @return this
         */
        public Builder<V1, E1, G1> setVertexLabelProvider(Function<V1, String> vertexLabelProvider) {
            this.vertexLabelProvider = vertexLabelProvider;
            return this;
        }

        /**
         * Sets a id provider for the vertexes.
         * <p>
         * This will default to {@link com.github.systemdir.gml.model.UniqueIntIdFunction} if not set.
         *
         * @param edgeIDProvider The function must not return null
         * @return this
         */
        public Builder<V1, E1, G1> setEdgeIDProvider(Function<E1, String> edgeIDProvider) {
            this.edgeIDProvider = edgeIDProvider;
            return this;
        }

        /**
         * For generating edge labels.
         * <p>
         * If null, edge labels will be generated using the toString() method of the edge object.
         *
         * @param edgeLabelProvider The function must not return null
         * @return this
         */
        public Builder<V1, E1, G1> setEdgeLabelProvider(Function<E1, String> edgeLabelProvider) {
            this.edgeLabelProvider = edgeLabelProvider;
            return this;
        }

        public YedGmlWriter<V1, E1, G1> build() {
            return new YedGmlWriter<>(this);
        }
    }


    private static final String creator = "JGraphT GML Exporter - modified by Hayato Hess, Andreas Hofstadler";
    private static final String version = "1";

    public static final String delim = " ";
    public static final String tab1 = "\t";
    public static final String tab2 = "\t\t";
    public static final String tab3 = "\t\t\t";

    public enum PrintLabels {
        PRINT_EDGE_LABELS,
        PRINT_VERTEX_LABELS,
        PRINT_GROUP_LABELS
    }

    public static final PrintLabels[] PRINT_LABELS
            = new PrintLabels[]{PRINT_EDGE_LABELS, PRINT_VERTEX_LABELS, PRINT_GROUP_LABELS};
    public static final PrintLabels[] PRINT_NO_LABELS = new PrintLabels[0];

    @Nullable
    private final Map<G, ? extends Set<V>> groupMapping;
    // intern - created by constructor from groupMapping
    @Nullable
    private final Map<V, G> reversedGroupMapping;

    @NotNull
    private final Function<Object, String> vertexIDProvider;
    @NotNull
    private final Function<V, String> vertexLabelProvider;
    @NotNull
    private final Function<? super E, String> edgeIDProvider;
    @NotNull
    private final Function<E, String> edgeLabelProvider;
    @NotNull
    private final YedGmlGraphicsProvider<V, E, G> graphProvider;
    @NotNull
    private final Function<G, String> groupLabelProvider;
    @NotNull
    private final Function<? super G, String> groupIdProvider;

    @NotNull
    private final EnumSet<PrintLabels> printLables;

    public YedGmlWriter(Builder<V, E, G> builder) {
        this.graphProvider = builder.graphicsProvider;
        this.printLables = builder.printLabels;

        this.groupMapping = builder.groupMapping;

        this.vertexLabelProvider = builder.vertexLabelProvider != null
                ? builder.vertexLabelProvider
                : Objects::toString;
        this.edgeLabelProvider = builder.edgeLabelProvider != null
                ? builder.edgeLabelProvider
                : Objects::toString;

        /*
         * The same id function is used for all objects. Simplest way to prevent
         * any duplicate ID problems.
         */
        UniqueIntIdFunction<Object> uniqueIdFunction = new UniqueIntIdFunction<>();

        if (builder.vertexIDProvider != null) {
            this.vertexIDProvider = builder.vertexIDProvider;
        } else {

            this.vertexIDProvider = uniqueIdFunction;
        }

        if (builder.edgeIDProvider != null) {
            this.edgeIDProvider = builder.edgeIDProvider;
        } else {
            this.edgeIDProvider = uniqueIdFunction;
        }

        if (groupMapping != null) {
            reversedGroupMapping = new HashMap<>();
            for (Map.Entry<G, ? extends Set<V>> group : groupMapping.entrySet()) {
                for (V grouped : group.getValue()) {
                    reversedGroupMapping.put(grouped, group.getKey());
                }
            }
            this.groupLabelProvider = builder.groupLabelProvider != null
                    ? builder.groupLabelProvider
                    : Objects::toString;
            groupIdProvider = uniqueIdFunction;
        } else {
            reversedGroupMapping = null;
            groupIdProvider = uniqueIdFunction;
            groupLabelProvider = Objects::toString;
        }
    }

    private String quoted(final String s) {
        StringBuilder sb = new StringBuilder(s.length() + 2);
        sb.append('"');
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            switch (ch) {
                case '"':
                    sb.append("&quot;");
                    break;
                case '&':
                    sb.append("&amp;");
                default:
                    sb.append(ch);
            }
        }
        sb.append('"');

        return sb.toString();
    }

    private void exportHeader(PrintWriter out) {
        out.println("Creator" + delim + quoted(creator));
        out.println("Version" + delim + version);
    }

    private void exportVertices(
            PrintWriter out,
            Graph<V, E> g) {
        for (V vertex : g.vertexSet()) {
            // dont print vertexes added as groups
            if (groupMapping != null && groupMapping.containsKey(vertex)) {
                continue;
            }

            out.println(tab1 + "node");
            out.println(tab1 + "[");
            out.println(
                    tab2 + "id" + delim + vertexIDProvider.apply(vertex));

            boolean printVertexLabels = printLables.contains(PRINT_VERTEX_LABELS);
            if (printVertexLabels) {
                String label = vertexLabelProvider.apply(vertex);
                out.println(tab2 + "label" + delim + quoted(label));
            }

            NodeGraphicDefinition definition = graphProvider.getVertexGraphics(vertex);
            if (definition != null)
                out.print(definition.toString(printVertexLabels));

            if (reversedGroupMapping != null) {
                if (reversedGroupMapping.containsKey(vertex)) {
                    out.println(tab2 + "gid" + delim + groupIdProvider.apply(reversedGroupMapping.get(vertex)));
                }
            }

            out.println(tab1 + "]");
        }
    }

    private void exportGroups(PrintWriter out) {
        if (groupMapping == null)
            return;

        for (Map.Entry<G, ? extends Set<V>> groupEntry : groupMapping.entrySet()) {
            G group = groupEntry.getKey();
            out.println(tab1 + "node");
            out.println(tab1 + "[");
            out.println(
                    tab2 + "id" + delim + groupIdProvider.apply(group));

            boolean printGroupLabels = printLables.contains(PRINT_GROUP_LABELS);
            if (printGroupLabels) {
                String label = groupLabelProvider.apply(group);
                out.println(tab2 + "label" + delim + quoted(label));
            }

            NodeGraphicDefinition definition = graphProvider.getGroupGraphics(group, groupEntry.getValue());
            if (definition != null)
                out.print(definition.toString(printGroupLabels));

            out.println(tab2 + "isGroup" + delim + "1");

            out.println(tab1 + "]");
        }
    }

    private void exportEdges(PrintWriter out, Graph<V, E> g) {
        for (E edge : g.edgeSet()) {
            out.println(tab1 + "edge");
            out.println(tab1 + "[");
            String id = edgeIDProvider.apply(edge);
            out.println(tab2 + "id" + delim + id);
            String s = vertexIDProvider.apply(g.getEdgeSource(edge));
            out.println(tab2 + "source" + delim + s);
            String t = vertexIDProvider.apply(g.getEdgeTarget(edge));
            out.println(tab2 + "target" + delim + t);


            boolean printEdgeLabels = printLables.contains(PRINT_EDGE_LABELS);

            if (printEdgeLabels) {
                String label = edgeLabelProvider.apply(edge);
                out.println(tab2 + "label" + delim + quoted(label));
            }

            EdgeGraphicDefinition definition = graphProvider.getEdgeGraphics(edge, g.getEdgeSource(edge), g.getEdgeTarget(edge));
            if (definition != null)
                out.print(definition.toString(printEdgeLabels));


            out.println(tab1 + "]");
        }
    }

    private void export(Writer output, Graph<V, E> g, boolean directed) {
        PrintWriter out = new PrintWriter(output);

        // assign ids in vertex set iteration order
        // the id provider hereby stores already "seen" objects
        g.vertexSet().forEach(vertexIDProvider::apply);

        // print gml header
        exportHeader(out);
        out.println("graph");
        out.println("[");

        // print (empty graph label)
        out.println(tab1 + "label" + delim + quoted(""));

        // print gml is directed graph?
        if (directed) {
            out.println(tab1 + "directed" + delim + "1");
        } else {
            out.println(tab1 + "directed" + delim + "0");
        }

        // export graph elements
        exportVertices(out, g);
        exportGroups(out);
        exportEdges(out, g);

        // finish output operations
        out.println("]");
        out.flush();
    }

    /**
     * Exports a graph into a PLAIN text file in GML format.
     *
     * @param output the writer to which the graph to be exported
     * @param g the graph to be exported
     */
    public void export(Writer output, Graph<V, E> g) {
        export(output, g, g.getType().isDirected());
    }
}
