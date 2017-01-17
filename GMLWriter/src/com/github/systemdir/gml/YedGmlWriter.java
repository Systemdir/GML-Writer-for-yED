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
import com.github.systemdir.gml.model.YedGmlGraphicsProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jgrapht.DirectedGraph;
import org.jgrapht.Graph;
import org.jgrapht.UndirectedGraph;
import org.jgrapht.ext.EdgeNameProvider;
import org.jgrapht.ext.IntegerEdgeNameProvider;
import org.jgrapht.ext.IntegerNameProvider;
import org.jgrapht.ext.VertexNameProvider;

import java.io.PrintWriter;
import java.io.Writer;
import java.util.*;

import static com.github.systemdir.gml.YedGmlWriter.PrintLabels.PRINT_EDGE_LABELS;
import static com.github.systemdir.gml.YedGmlWriter.PrintLabels.PRINT_GROUP_LABELS;
import static com.github.systemdir.gml.YedGmlWriter.PrintLabels.PRINT_VERTEX_LABELS;


/**
 * Exports a graph into a GML file (Graph Modelling Language) for yED.
 * <p>
 * <p>For a description of the format see <a
 * href="http://www.infosun.fmi.uni-passau.de/Graphlet/GML/">
 * http://www.infosun.fmi.uni-passau.de/Graphlet/GML/</a>.</p>
 * <p>
 *
 * @author (GML writer) Dimitrios Michail
 * @author (yED extension) Hayato Hess
 */
public class YedGmlWriter<V, E, G> {

    public static class Builder<V1, E1, G1> {
        VertexNameProvider<G1> groupLabelProvider;
        Map<G1, ? extends Set<V1>> groupMapping;
        VertexNameProvider vertexIDProvider;
        VertexNameProvider<V1> vertexLabelProvider;
        EdgeNameProvider<E1> edgeIDProvider;
        EdgeNameProvider<E1> edgeLabelProvider;
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
         * @param groupMapping
         * @param groupLabelProvider
         */
        public Builder<V1, E1, G1> setGroups(Map<G1, ? extends Set<V1>> groupMapping, VertexNameProvider<G1> groupLabelProvider) {
            this.groupMapping = groupMapping;
            this.groupLabelProvider = groupLabelProvider;
            return this;
        }

        /**
         * Sets a id provider for the vertexes. This will default to {@link org.jgrapht.ext.IntegerNameProvider} if not set.
         *
         * @param vertexIDProvider id provider for the vertexes to use.
         */
        public Builder<V1, E1, G1> setVertexIDProvider(VertexNameProvider vertexIDProvider) {
            this.vertexIDProvider = vertexIDProvider;
            return this;
        }

        /**
         * For generating vertex labels. If null, vertex labels will be generated using the toString() method of the vertex object.
         *
         * @param vertexLabelProvider
         */
        public Builder<V1, E1, G1> setVertexLabelProvider(VertexNameProvider<V1> vertexLabelProvider) {
            this.vertexLabelProvider = vertexLabelProvider;
            return this;
        }

        /**
         * Sets a id provider for the vertexes. This will default to {@link org.jgrapht.ext.IntegerEdgeNameProvider} if not set.
         *
         * @param edgeIDProvider id provider for the vertexes to use.
         */
        public Builder<V1, E1, G1> setEdgeIDProvider(EdgeNameProvider<E1> edgeIDProvider) {
            this.edgeIDProvider = edgeIDProvider;
            return this;
        }

        /**
         * For generating edge labels. If null, edge labels will be generated using the toString() method of the edge object.
         *
         * @param edgeLabelProvider
         */
        public Builder<V1, E1, G1> setEdgeLabelProvider(EdgeNameProvider<E1> edgeLabelProvider) {
            this.edgeLabelProvider = edgeLabelProvider;
            return this;
        }

        public YedGmlWriter<V1, E1, G1> build() {
            return new YedGmlWriter<V1, E1, G1>(this);
        }
    }


    private static final String creator = "JGraphT GML Exporter - modified by Hayato Hess";
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
    private Map<G, ? extends Set<V>> groupMapping;
    // intern - created by constructor from groupMapping
    private Map<V, G> reversedGroupMapping;

    @NotNull
    private VertexNameProvider vertexIDProvider;
    @Nullable
    private VertexNameProvider<V> vertexLabelProvider;
    @NotNull
    private EdgeNameProvider<E> edgeIDProvider;
    @Nullable
    private EdgeNameProvider<E> edgeLabelProvider;
    @NotNull
    private YedGmlGraphicsProvider<V, E, G> graphProvider;
    @Nullable
    private VertexNameProvider<G> groupLableProvider;
    @NotNull
    private final EnumSet<PrintLabels> printLables;

    public YedGmlWriter(Builder<V, E, G> builder) {
        this.graphProvider = builder.graphicsProvider;
        this.printLables = builder.printLabels;

        this.groupLableProvider = builder.groupLabelProvider;
        this.groupMapping = builder.groupMapping;

        this.vertexLabelProvider = builder.vertexLabelProvider;
        this.edgeLabelProvider = builder.edgeLabelProvider;

        if (builder.vertexIDProvider != null) {
            this.vertexIDProvider = builder.vertexIDProvider;
        } else {
            this.vertexIDProvider = new IntegerNameProvider<>();
        }

        if (builder.edgeIDProvider != null) {
            this.edgeIDProvider = builder.edgeIDProvider;
        } else {
            this.edgeIDProvider = new IntegerEdgeNameProvider<E>();
        }

        if (groupMapping != null) {
            reversedGroupMapping = new HashMap<>();
            for (G group : groupMapping.keySet()) {
                for (V value : groupMapping.get(group)) {
                    reversedGroupMapping.put(value, group);
                }
            }
        }
    }

    private String quoted(final String s) {
        return "\"" + s + "\"";
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
                    tab2 + "id" + delim + vertexIDProvider.getVertexName(vertex));
            
            boolean printVertexLabels=printLables.contains(PRINT_VERTEX_LABELS);
            if (printVertexLabels) {
                String label =
                        (vertexLabelProvider == null) ? vertex.toString()
                                : vertexLabelProvider.getVertexName(vertex);
                out.println(tab2 + "label" + delim + quoted(label));
            }
            
            NodeGraphicDefinition definition = graphProvider.getVertexGraphics(vertex);
            if (definition != null)
                out.print(definition.toString(printVertexLabels));
            
            if (groupMapping != null) {
                if (reversedGroupMapping.containsKey(vertex)) {
                    out.println(tab2 + "gid" + delim + vertexIDProvider.getVertexName(reversedGroupMapping.get(vertex)));
                }
            }

            out.println(tab1 + "]");
        }
    }

    private void exportGroups(PrintWriter out) {
        if (groupMapping == null)
            return;

        for (G group : groupMapping.keySet()) {
            out.println(tab1 + "node");
            out.println(tab1 + "[");
            out.println(
                    tab2 + "id" + delim + vertexIDProvider.getVertexName(group));

            boolean printGroupLabels = printLables.contains(PRINT_GROUP_LABELS);
            if (printGroupLabels) {
                String label =
                        (groupLableProvider == null) ? group.toString()
                                : groupLableProvider.getVertexName(group);
                out.println(tab2 + "label" + delim + quoted(label));
            }

            NodeGraphicDefinition definition = graphProvider.getGroupGraphics(group, groupMapping.get(group));
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
            String id = edgeIDProvider.getEdgeName(edge);
            out.println(tab2 + "id" + delim + id);
            String s = vertexIDProvider.getVertexName(g.getEdgeSource(edge));
            out.println(tab2 + "source" + delim + s);
            String t = vertexIDProvider.getVertexName(g.getEdgeTarget(edge));
            out.println(tab2 + "target" + delim + t);


            boolean printEdgeLabels = printLables.contains(PRINT_EDGE_LABELS);

            if (printEdgeLabels) {
                String label =
                        (edgeLabelProvider == null) ? edge.toString()
                                : edgeLabelProvider.getEdgeName(edge);
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

        for (V from : g.vertexSet()) {
            // assign ids in vertex set iteration order
            // the id provider hereby stores already "seen" objects
            vertexIDProvider.getVertexName(from);
        }

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
     * Exports an undirected graph into a PLAIN text file in GML format.
     *
     * @param output the writer to which the graph to be exported
     * @param g      the undirected graph to be exported
     */
    public void export(Writer output, UndirectedGraph<V, E> g) {
        export(output, g, false);
    }

    /**
     * Exports a directed graph into a PLAIN text file in GML format.
     *
     * @param output the writer to which the graph to be exported
     * @param g      the directed graph to be exported
     */
    public void export(Writer output, DirectedGraph<V, E> g) {
        export(output, g, true);
    }

}