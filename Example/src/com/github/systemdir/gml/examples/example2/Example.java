package com.github.systemdir.gml.examples.example2;

import com.github.systemdir.gml.YedGmlWriter;
import com.github.systemdir.gml.examples.example2.model.*;
import org.jgrapht.graph.SimpleGraph;

import java.io.*;
import java.util.*;

import static com.github.systemdir.gml.YedGmlWriter.PRINT_LABELS;


/**
 * Defines a simple graph with a data vertex and four method vertexes which are grouped together
 */
public class Example {
    public static void main(String[] args) throws IOException {
        // create data vertex
        Data data=new Data("test data");

        // create method vertexes
        Method create = new Method("create test data", AccessType.create);
        Method read = new Method("read test data", AccessType.read);
        Method update = new Method("update test data", AccessType.update);
        Method delete = new Method("delete test data",AccessType.delete);

        // create a set of all methods
        Set<Element> methods = new HashSet<Element>(Arrays.asList(new Element[]{create,read,update,delete}));
        
        // create graph
        SimpleGraph<Element,AccessEdge> toDraw=new SimpleGraph<Element,AccessEdge>(AccessEdge.class);
        toDraw.addVertex(data);

        // create edges
        for(Element method:methods){
            toDraw.addVertex(method);
            toDraw.addEdge(method, data, new AccessEdge(1)); 
        }

        // create groups
        MethodGroup group = new MethodGroup("My Method Group");
        Map<MethodGroup, Set<Element>> groups = new HashMap<>();
        groups.put(group, methods);

        //note that group elements must also be added if you want to draw edges between groups
        //graph.addVertex(group);
        
        
        // define the look and feel of the graph
        ExampleGraphicsProvider graphicsProvider = new ExampleGraphicsProvider();

        // get the gml writer
        YedGmlWriter<Element,AccessEdge,MethodGroup> writer 
                = new YedGmlWriter.Builder<>(graphicsProvider, PRINT_LABELS)
                .setGroups(groups, Element::getName)
                .setEdgeLabelProvider(edge -> "q:"+edge.getAccessQuantity())
                .setVertexLabelProvider(Element::getName)
                .build();

        
        // write to file
        new File("gml-output").mkdir();//create folder
        File outputFile=new File("gml-output"+File.separator+"example2.gml");
        try (Writer output = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(outputFile), "utf-8"))) {
            writer.export(output, toDraw);
        }
        
        System.out.println("Exported to: "+outputFile.getAbsolutePath());
    }
}
