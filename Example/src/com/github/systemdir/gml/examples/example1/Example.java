package com.github.systemdir.gml.examples.example1;

import com.github.systemdir.gml.YedGmlWriter;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import java.io.*;
import java.util.Scanner;

/**
 * The first example allows to export a very simple graph entered by the user
 */
public class Example {
    public static void main(String[] args) throws IOException {
        // get graph from user
        SimpleGraph<String, DefaultEdge> toDraw = getSimpleGraphFromUser();

        // define the look and feel of the graph
        ExampleGraphicsProvider graphicsProvider = new ExampleGraphicsProvider(); 

        // get the gml writer
        YedGmlWriter<String, DefaultEdge, Object> writer
                = new YedGmlWriter.Builder<String, DefaultEdge, Object>(graphicsProvider, YedGmlWriter.PrintLabels.PRINT_VERTEX_LABELS).build();

        
        // write to file
        new File("gml-output").mkdir();//create folder
        File outputFile=new File("gml-output"+File.separator+"example1.gml");
        try (Writer output = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(outputFile), "utf-8"))) {
            writer.export(output, toDraw);
        }
        
        System.out.println("Exported to: "+outputFile.getAbsolutePath());
    }


    /**
     * Asks the user to input an graph
     *
     * @return graph inputted by the user
     */
    public static SimpleGraph<String, DefaultEdge> getSimpleGraphFromUser() {
        Scanner sc = new Scanner(System.in);
        SimpleGraph<String, DefaultEdge> graph = new SimpleGraph<String, DefaultEdge>(DefaultEdge.class);

        // input number of vertices
        int verticeAmount;
        do {
            System.out.print("Enter the number of vertices (more than one): ");
            verticeAmount = sc.nextInt();
        } while (verticeAmount <= 1);

        // input vertices
        for (int i = 1; i <= verticeAmount; i++) {
            System.out.print("Enter vertex name " + i + ": ");
            String input = sc.next();
            if (graph.vertexSet().contains(input)) {
                System.err.println("vertex with that name already exists");
                i--;
            } else {
                graph.addVertex(input);
            }
        }

        // input edges
        System.out.println("\nEnter edge (name => name)");
        String userWantsToContinue;
        do {
            String e1, e2;
            
            do {
                System.out.print("Edge from: ");
                e1 = sc.next();
            } while (!graph.vertexSet().contains(e1));
            do {
                System.out.print("Edge to: ");
                e2 = sc.next();
            } while (!graph.vertexSet().contains(e2));

            graph.addEdge(e1, e2);

            // add another edge?
            System.out.print("Add more edges? (y/n): ");
            userWantsToContinue = sc.next();
        }while (userWantsToContinue.equals("y") || userWantsToContinue.equals("yes") || userWantsToContinue.equals("1"));

        return graph;
    }
}
