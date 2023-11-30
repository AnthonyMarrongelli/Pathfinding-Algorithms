/*
 * Anthony Marrongelli
 * 
 * Implementation of Dijkstra's Greedy Pathfinding Algorithm
 * 
 *  Input files are formatted as follows:
 *      -First line is the number of vertices
 *      -Second line is the source vertex
 *      -Third line is the number of edges
 * 
 *      After that are the following edges formatted as (source, destination, cost) 
 * 
 *  Output files are formatted as follows:
 *      -First line is the number of vertices
 *      -The following n (num of vertices) lines are as follows:
 *          (destination, weight, penultimate node to destination in our path)
 * 
 *  To compile "javac dijkstra.java"
 *  To run "java dijkstra.java"
 * 
 *  Written in Java 20.0.2
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.Set;

public class dijkstra {
    
    private Map<Pair, Integer> adjacencyMatrix;
    private Set<Integer> vertices;

    /* variables for important numbers in our program needed for initialization */
    int numVertices, sourceVertex, numEdges;

    /* Dijkstra constructor to initialize our vertices hash map and our adjacency matrix hash map as well */
    public dijkstra () {
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

    /* Has edge function, takes the source vertex and the destination vertex as input, return true or false if there is a connection between
     * the two in our matrix
     */
    public boolean hasEdge(int source, int destination) {
        Pair toLookup = new Pair(source, destination);
        if(adjacencyMatrix.get(toLookup) != null) return true;
        return false;
    }

    /* Dijkstra's algorithm function, doesn't take any input as everything it needs is already enclosed in the class
     *  (That being the graph and edges), and it outputs the distances between the source vertex and the vertex in question
     *   with also giving the immediate parent along the path into an array and returns it
     */
    public int[][] dijkstraAlgo() {
        
        // create 2d array with as many indexes as vertices, each item has parent and distance, we will identify them by iteration (0 - vertices-1)
        int[][] trackingArray = new int[numVertices+1][2];
        /* array for visited nodes */
        ArrayList<Integer> visitedNodes = new ArrayList<>();
        ArrayList<Integer> unvisitedNodes = new ArrayList<>();


        /* Initializing distances and parents */
        for(int i = 1; i <= numVertices; i++) {
            /* Setting parent to -1 and distance to infinity */
            if(i == sourceVertex) {
                trackingArray[i][0] = -1;
                trackingArray[i][1] = -1;
            } else {
                trackingArray[i][0] = -1;
                trackingArray[i][1] = Integer.MAX_VALUE;
            }
        }

        /* populating unvisited nodes */
        for(int i = 1; i <= numVertices; i++) {
            unvisitedNodes.add(i);
        }

        /* vertex = current node */
        int vertex = sourceVertex;

        /* For iterating through all vertices */
        while(!unvisitedNodes.isEmpty()) {
        
            /* Going through all possible connections */
            for(int i = 1; i <= numVertices; i++) {
                /* Setting distances to all nodes connected to current node */
                if(this.hasEdge(vertex, i)) {

                    /* if its visited ignore */ 
                    if(visitedNodes.contains(i)) continue;
                       
                    /* current node to node */
                    Pair temp = new Pair(vertex, i);

                    /* Setting distance and parents*/ 
                    if(vertex == sourceVertex) {
                        /* Source node just sets connections to whatever the edge is and then gives the parent as source node */
                        trackingArray[i][1] = this.adjacencyMatrix.get(temp);
                        trackingArray[i][0] = vertex;
                    }
                    else {
                        /* If distance is shorter than current distance we update the parent and distance */
                        if(trackingArray[i][1] > trackingArray[vertex][1] + this.adjacencyMatrix.get(temp)) { 
                            trackingArray[i][1] = trackingArray[vertex][1] + this.adjacencyMatrix.get(temp);
                            trackingArray[i][0] = vertex;
                        }
                    }
                }
            }

            /* Adding/Removing the value to visited/unvisited node lists */
            visitedNodes.add(Integer.valueOf(vertex));
            unvisitedNodes.remove(Integer.valueOf(vertex));

            /* Finding next node to visit by finding lowest distance*/
            int nextVertex = -1;
            for(int j = 1; j <= numVertices; j++) {
                if(visitedNodes.contains(j)) continue;
                if(nextVertex == -1) nextVertex = j;
                if(trackingArray[j][1] < trackingArray[nextVertex][1]) nextVertex = j;
            }

            /* setting vertex to next node and if the distance is -1, we have accessed all nodes */
            vertex = nextVertex;
            if(nextVertex == -1) break;
            
        }

        /* returning the array of values we need from the algorithm */
        return  trackingArray;
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
   

    public static void main(String args[]) throws FileNotFoundException {

        /* instantiating the algo */
        dijkstra dijkstra = new dijkstra();

        /* Reading in the three important variables we need */
        Scanner in = new Scanner(new File("in3.txt"));
        dijkstra.numVertices = in.nextInt();
        dijkstra.sourceVertex = in.nextInt();
        dijkstra.numEdges = in.nextInt();
        
        /* Adding all vertices */
        for(int i = 0; i <= dijkstra.numVertices; i++) {
            dijkstra.addVertex(i);
        }

        /* Adding all edges through file input/output */
        for(int i = 0; i < dijkstra.numEdges; i++) {
            dijkstra.addEdge(in.nextInt(), in.nextInt(), in.nextInt());
        }

        /* Running the algo on given data set */
        int[][] results = dijkstra.dijkstraAlgo();

        try {
            FileWriter outputFile = new FileWriter("dijkstra3.txt");
            outputFile.write("" + Integer.valueOf(dijkstra.numVertices));
            for(int i = 1; i <= dijkstra.numVertices; i++) {
                outputFile.write("\n" + i + " " + results[i][1] + " " + results[i][0]);
            }
            outputFile.close();
        } catch (IOException e) {
            System.out.println("Error writing to file.");
            e.printStackTrace();
        }
    }

}
