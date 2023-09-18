/*
 * Anthony Marrongelli
 * 
 * Implementation of Bellman-Ford's pathfinding algorithm
 * as well as Floyd Warshall's pathfinding algorithm
 * 
 *  Input files are formatted as follows:
 *      -First line is the number of vertices
 *      -Second line is the source vertex
 *      -Third line is the number of edges
 * 
 *      After that are the following edges formatted as (source, destination, cost) 
 * 
 *  To compile "javac graph.java"
 *  To run "java graph.java"
 * 
 *  Written in Java 20.0.2
 */

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.Set;

public class graph {
    
    private Map<Pair, Integer> adjacencyMatrix;
    private Set<Integer> vertices;

    /* variables for important numbers in our program needed for initialization */
    int numVertices, sourceVertex, numEdges;

    /* Dijkstra constructor to initialize our vertices hash map and our adjacency matrix hash map as well */
    public graph () {
        vertices = new HashSet<>();
        adjacencyMatrix = new HashMap<>();
    }

    /* Add vertex function, takes number for labeling a vertex and labels a vertex and inputs it into our matrix */
    public void addVertex(int vertex) {
        vertices.add(vertex);
    }

    /* Add edge function, takes a source vertex, destination vertex, and edge weight as input. returns nothing, but it adds the edge to our matrix */
    public void addEdge(int source, int destination, int cost) {
        if(!vertices.contains(source) || !vertices.contains(destination)) throw new IllegalArgumentException("You failed here, try again!");
            
        Pair edge = new Pair(source, destination);
        adjacencyMatrix.put(edge, cost);

    }

    /* takes the source vertex and the destination vertex as input, return true or false if there is a connection between the two in our matrix */
    public boolean hasEdge(int source, int destination) {
        Pair toLookup = new Pair(source, destination);
        if(adjacencyMatrix.get(toLookup) != null) return true;
        return false;
    }

    /* Bellman Ford function, doesn't take any input as everything it needs is already enclosed in the class (That being the graph and edges), 
       and it outputs the distances between the source vertex and the vertex in question with also giving the immediate parent along the path into an array and returns it */
    public int[][] bellmanfordAlgo() throws Exception {
        
        // create 2d array with as many indexes as vertices, each item has parent and distance, we will identify them by iteration (0 - vertices-1)
        int[][] trackingArray = new int[numVertices+1][2];

        /* Initializing distances and parents */
        for(int i = 1; i <= numVertices; i++) {
            /* Setting parent to -1 and distance to infinity */
            if(i == sourceVertex) {
                trackingArray[i][0] = 0;
                trackingArray[i][1] = 0;
            } else {
                trackingArray[i][0] = -1;
                trackingArray[i][1] = Integer.MAX_VALUE / 2;
            }
        }

        /* Creating a list so we can tell what way to attack with the algortim (simply put to start from source node) */
        ArrayList<Integer> VertexList = new ArrayList<>();
        VertexList.add(sourceVertex);
        for(int i = 1; i < numVertices; i++) {
            if(i == sourceVertex) continue;
            VertexList.add(i);
        }

        /* Running algorithm for each vertex (except src) */
        for(int i = 0; i < numVertices-1; i++) {
            int u = VertexList.get(i);
            for(int j = 1; j <= numVertices; j++) {
                if(hasEdge(u, j)) {
                    Pair temp = new Pair(u, j);
                    /* if the distance of current node + weight is less than the distance of prospective node, change the prospective nodes distance and set parent */
                    if(trackingArray[u][1] + adjacencyMatrix.get(temp) < trackingArray[j][1]) {
                        trackingArray[j][1] = trackingArray[u][1] + adjacencyMatrix.get(temp);
                        trackingArray[j][0] = u;
                    }
                }
            }
        } 

        /* Checking for negative number cycle */
        for(int i=0; i < numVertices-1; i++) {
            int u = VertexList.get(i);
            for(int j = 1; j <= numVertices; j++) {
                if(hasEdge(u, j)) {
                     Pair temp = new Pair(u, j);
                    /* if the distance of current node + weight is less than the distance of prospective node, change the prospective nodes distance and set parent */
                    if(trackingArray[u][1] + adjacencyMatrix.get(temp) < trackingArray[j][1]) {
                        throw new Exception("Negative weight-cycle");
                    }
                }
            }
        }
        /* returning the array of values we need from the algorithm */
        return  trackingArray;
    }


    /* Floyd Warshall function, doesn't take any input as everything it needs is already enclosed in the class (That being the graph and edges),
       and it outputs the distances between the source vertex and the vertex in question with also giving the immediate parent along the path into an array and returns it */
    public int[][]  floydwarshallAlgo() {
        
        // create 2d array with as many indexes as vertices, each item has parent and distance, we will identify them by iteration (0 - vertices-1)
        int[][] returnArray = new int[numVertices][numVertices];

        /* Initializing return Array */
        for(int i = 0; i < numVertices; i++) {
            for(int j = 0; j < numVertices; j++) {
                returnArray[i][j] = Integer.MAX_VALUE / 2;
            }
        }


        /* Adding edges and 0's for node to same node */
        for(int i = 0; i < numVertices; i++) {
            for(int j = 0; j < numVertices; j++) {
                
                if(i == j) returnArray[i][j] = 0;
                else if(hasEdge(i+1, j+1)) {
                    Pair temp = new Pair(i+1, j+1);
                    returnArray[i][j] = adjacencyMatrix.get(temp);
                }
            }
        }

        /* iterating through each edge/vertice and finding shortest path */
        for(int i = 0; i < numVertices; i++) {
            for(int j = 0; j < numVertices; j++) {
                for(int k = 0; k < numVertices; k++) {
                    if(returnArray[i][j] > returnArray[i][k] + returnArray[k][j]) {
                        returnArray[i][j] = returnArray[i][k] + returnArray[k][j];
                    } 
                }
            }
        }

        return returnArray;
    }

    /* Pair class used to identify a pair within our adjacency matrix, has source and destination (or vice versa) along with the weight between them */
    private class Pair {
        private int source;
        private int destination;

        public Pair(int source, int destination) {
            this.source = source;
            this.destination = destination; 
        }

        @Override
        public boolean equals(Object obj) {
            if(this == obj) return true;
            if(obj == null || getClass() != obj.getClass()) return false;
            Pair other = (Pair) obj;
            return (source == other.source && destination == other.destination) || (source == other.destination && destination == other.source);
        }

        @Override
        public int hashCode() {
            return Objects.hash(source, destination) + Objects.hash(destination, source);
        }
    }
   
    public static void main(String args[]) throws Exception {

        /* instantiating the algo */
        graph graph = new graph();

        /* Reading in the three important variables we need */
        Scanner in = new Scanner(new File("in.txt"));
        graph.numVertices = in.nextInt();
        graph.sourceVertex = in.nextInt();
        graph.numEdges = in.nextInt();
        
        /* Adding all vertices */
        for(int i = 0; i <= graph.numVertices; i++) {
            graph.addVertex(i);
        }

        /* Adding all edges through file input/output */
        for(int i = 0; i < graph.numEdges; i++) {
            graph.addEdge(in.nextInt(), in.nextInt(), in.nextInt());
        }

        /* Running the algo on given data set */
        int[][] bellmanResults = graph.bellmanfordAlgo();
        int[][] floydResults = graph.floydwarshallAlgo();

        try {
            FileWriter outputFile = new FileWriter("bellman-ford.txt");

            outputFile.write("" + Integer.valueOf(graph.numVertices));
            for(int i = 1; i <= graph.numVertices; i++) {
                outputFile.write("\n" + i + " " + bellmanResults[i][1] + " " + bellmanResults[i][0]);
            }

            outputFile.close();
            FileWriter outputFile2 = new FileWriter("floyd-warshall.txt");
            outputFile2.write("" + Integer.valueOf(graph.numVertices));
            for(int i = 0; i < graph.numVertices; i++) {
                outputFile2.write("\n");
                for(int j = 0; j < graph.numVertices; j++) {
                    outputFile2.write(floydResults[i][j] + " ");
                }
            }

            outputFile2.close();
        } catch (IOException e) {
            System.out.println("Error writing to file.");
            e.printStackTrace();
        }
    }

}
