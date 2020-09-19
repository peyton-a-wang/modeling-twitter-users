import java.util.Vector;
import java.util.LinkedList;
import java.util.Iterator;
import javafoundations.ArrayStack;
import java.util.Arrays;
import java.io.PrintWriter;
import java.io.File;
import java.io.IOException;

/**
 * AdjListsGraph implements the Graph interface. It contains a Vector that stores vertices and a Vector of LinkedLists that 
 * each store vertices to create arcs. It also adds/removes graph components, finds the predecessors/successors 
 * of a vertex, performs depth-first and breadth-first searches, and writes out the graph components to a TGF file. 
 *
 * AdjListsGraph.java
 * @author Alexandra Bullen-Smith
 * @author Peyton Wang
 * @version 12/9/19
 */

public class AdjListsGraph<T> implements Graph<T> {

    // instance variables
    private Vector<T> vertices;
    private Vector<LinkedList<T>> arcs;

    /**
     * Constructor creates a AdjListsGraph object and initializes instance variables.
     */
    public AdjListsGraph(){
        this.vertices = new Vector<T>();
        this.arcs = new Vector<LinkedList<T>>();
    }

    /** 
     * Getter method for obtaining all the vertices in this graph.
     * 
     * @return Vector of all vertices
     */
    public Vector<T> getAllVertices(){
        return this.vertices;
    }

    /** 
     * Getter method for obtaining the number of vertices in this graph. 
     * 
     * @return int total number of vertices
     */
    public int getNumVertices() {
        return this.vertices.size();
    }

    /** 
     * Getter method for obtaining the number of arcs in this graph.
     * 
     * @return int total number of arcs
     */
    public int getNumArcs() {
        int numArcs = 0;
        for (int i = 0; i < this.arcs.size(); i++) {
            numArcs += this.arcs.elementAt(i).size();
        }
        return numArcs;
    }

    /** 
     * Returns all the vertices succeeding the given vertex.
     * @param T given vertex
     * @return LinkedList containing all the successors of the given vertex.
     */
    public LinkedList<T> getSuccessors(T vertex) {
        int i = this.vertices.indexOf(vertex);
        return this.arcs.get(i);
    }

    /** 
     * Return all the vertices preceding a given vertex.
     * 
     * @param T given vertex
     * @return LinkedList containing all the predecessors of the given vertex.
     */
    public LinkedList<T> getPredecessors(T vertex) {
        LinkedList<T> newList = new LinkedList<T>();

        for (int i = 0; i < this.vertices.size(); i++) {
            LinkedList<T> sucessorList = this.getSuccessors(this.vertices.get(i));
            if (sucessorList.contains(vertex)) {
                newList.add(this.vertices.get(i));
            }            
        }

        return newList;
    }

    /** 
     * Returns a boolean indicating whether this graph contains vertices and edges.
     * 
     * @return boolean true if this graph is empty, false otherwise
     */
    public boolean isEmpty() {
        return this.vertices.size() == 0;
    }

    /** 
     * Returns a boolean indicating whether there is a direct connection between the given vertices.
     * 
     * @param T first vertex
     * @param T second vertex
     * @return boolean true if an arc exists from the first vertex to the second, false otherwise
     */
    public boolean isArc (T vertex1, T vertex2) {
        int index = vertices.indexOf(vertex1);
        return this.arcs.elementAt(index).contains(vertex2);
    }

    /** 
     * Returns a boolean indicating whether there is an arc between the given vertices in both directions.
     *  
     * @param T first vertex
     * @param T second vertex
     * @return boolean true if an edge exists between first vertex to the second, false otherwise
     */
    public boolean isEdge (T vertex1, T vertex2) {
        return this.isArc(vertex1,vertex2) && this.isArc(vertex2, vertex1);
    }

    /** 
     * Returns a boolean indicating whether a pair of nodes i,j for which there is an arc, the opposite arc
     * is also present in the graph. 
     * 
     * @return boolean true if the graph is undirected, false otherwise
     */
    public boolean isUndirected() {
        for (int i = 0; i < this.arcs.size(); i++) {
            LinkedList<T> currentArc = this.arcs.elementAt(i); // get the arc in this graph's arcs

            for(int j = 0; j < currentArc.size(); j++) {
                int currentVertex = this.vertices.indexOf(currentArc.get(j));
                if (!isEdge(this.vertices.get(i), this.vertices.get(currentVertex))){
                    return false;
                }
            }

        }
        return true;
    }

    /** 
     * Adds the given vertex to this graph.
     * If the given vertex already exists, the graph does not change.
     * 
     * @param T vertex to be added
     */
    public void addVertex (T vertex) {
        if (!this.vertices.contains(vertex)){
            this.vertices.add(vertex);
            this.arcs.add(new LinkedList<T>());
        }
    }

    /** 
     * Removes the given vertex from this graph.
     * If the given vertex does not exist, the graph does not change.
     * 
     * @param T vertex to be removed 
     */
    public void removeVertex (T vertex) {
        if (this.vertices.contains(vertex)) {
            int index = this.vertices.indexOf(vertex);
            this.vertices.remove(vertex);
            this.arcs.remove(this.arcs.elementAt(index));

            for (int i = 0; i < this.arcs.size(); i++) {
                this.arcs.get(i).remove(vertex);
            }
        }
    }

    /** 
     * Inserts an arc between two given vertices of this graph. 
     * If at least one of the vertices does not exist, the graph is not changed.
     * 
     * @param T start vertex of the arc
     * @param T end vertex of the arc
     */
    public void addArc (T vertex1, T vertex2) {
        if (this.vertices.contains(vertex1) && this.vertices.contains(vertex2)){
            int index = this.vertices.indexOf(vertex1);
            this.arcs.elementAt(index).add(vertex2);
        }
    }

    /** 
     * Removes the arc between two given vertices of this graph. 
     * If one of the two vertices does not exist in the graph, the graph does not change.
     * 
     * @param T start vertex of the arc
     * @param T end vertex of the arc
     */
    public void removeArc (T vertex1, T vertex2) {
        if (this.vertices.contains(vertex1) && this.vertices.contains(vertex2)){
            int index = this.vertices.indexOf(vertex1);
            this.arcs.elementAt(index).remove(vertex2);
        }
    }

    /** 
     * Inserts an edge between the two given vertices of this graph.
     * If both vertices exist, the graph is not changed.
     * 
     * @param T start vertex of the arc
     * @param T end vertex of the arc
     */
    public void addEdge (T vertex1, T vertex2) {
        if (this.vertices.contains(vertex1) && this.vertices.contains(vertex2)) {
            int index1 = this.vertices.indexOf(vertex1);
            this.arcs.elementAt(index1).add(vertex2);
            int index2 = this.vertices.indexOf(vertex2);
            this.arcs.elementAt(index2).addFirst(vertex1);
        }
    }

    /** 
     * Removes the edge between the two given vertices of this graph.
     * If both vertices exist, the graph is not changed.
     * 
     * @param T start vertex of the edge
     * @param T end vertex of the edge
     */
    public void removeEdge (T vertex1, T vertex2) {
        if (this.vertices.contains(vertex1) && this.vertices.contains(vertex2)) {
            int index1 = this.vertices.indexOf(vertex1);
            this.arcs.elementAt(index1).remove(vertex2);
            int index2 = this.vertices.indexOf(vertex2);
            this.arcs.elementAt(index2).remove(vertex1);
        }
    }

    /** 
     * Returns a string representation of this graph.
     * 
     * @return String formatted respresentation of the graph components.
     */
    public String toString() {
        String s = "***Vertices***\n";
        s += this.vertices.toString();
        s += "\n***Edges***\n";
        for (int i = 0; i < this.arcs.size(); i++) {
            s += "from " + this.vertices.elementAt(i) + ": " + this.arcs.elementAt(i) + "\n";
        }

        return s;
    }

    /** 
     * Writes this graph to a file in TGF format.
     * 
     * @param String name of the file to be written out
     */
    public void saveToTGF(String tgfFileName) {
        try {
            PrintWriter writer = new PrintWriter(new File(tgfFileName));

            for (int i = 1; i <= this.vertices.size(); i++) {
                writer.println(i + " " + this.vertices.get(i - 1));
            }
            writer.println("#");
            for (int i = 0; i < this.vertices.size(); i++) {
                T vertex = this.vertices.get(i);
                LinkedList<T> list = this.arcs.get(i);
                for (int j = 0; j < list.size(); j++) {
                    T vertexIndex = list.get(j);
                    writer.println((this.vertices.indexOf(vertex)+1) + " " + 
                        (this.vertices.indexOf(vertexIndex)+1));
                }
            }
            writer.close();
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    /** 
     * Does a depth first search of the entire graph starting at the given point with no specific end point.
     * 
     * @param T starting vertex 
     * @return LinkedList<T> of the searched vertices
     */
    public LinkedList<T> depthFirstSearch(T vertex) {
        ArrayStack<T> stk = new ArrayStack<T>();
        LinkedList<T> result = new LinkedList<T>();
        boolean[] marked = new boolean[this.vertices.size()];

        for (int v = 0; v < this.vertices.size(); v++){  // mark each vertex as unvisited
            marked[v] = false;
        }
        
        T currentNode;
        int currentIndex;
        
        // push/add starting vertex into stack, then mark it as visisted
        stk.push(vertex); 
        result.add(vertex);
        marked[this.vertices.indexOf(vertex)] = true;

        while(!stk.isEmpty()) {
            currentNode = stk.peek();
            currentIndex = this.vertices.indexOf(currentNode);
            LinkedList<T> trackedArcs = this.arcs.get(currentIndex);
            int i = 0;
            
            while(i <= trackedArcs.size()) {
                if (i == trackedArcs.size()) {
                    stk.pop();
                }
                // push vertex into stack and add to result list if it hasn't been visited
                else if (marked[this.vertices.indexOf(trackedArcs.get(ii))] == false) {
                    stk.push(trackedArcs.get(i)); 
                    result.add(ctrackedArcs.get(i));
                    marked[this.vertices.indexOf(trackedArcs.get(i))] = true;

                    i = trackedArcs.size() + 1; 
                } 

                i++;
            }
        }
        return result;
    }
    
    /** 
     * Does a depth first search of the graph with a given second vertex.
     * 
     * @param T starting vertex 
     * @return LinkedList<T> containing the vertices that are the outcome of the depth first search
     */
    public LinkedList<T> depthFirstSearch2(T vertex1, T vertex2) {
        ArrayStack<T> stk = new ArrayStack<T>();
        LinkedList<T> result = new LinkedList<T>();
        boolean[] marked = new boolean[this.vertices.size()];

        for (int v = 0; v < this.vertices.size(); v++){  // mark each vertex as unvisited
            marked[v] = false;
        }
        
        T currentNode;
        int currentIndex;
        boolean isFound = false;

        // push and add starting vertex into stack, then mark it as visited
        stk.push(vertex1);  
        result.add(vertex1);
        marked[this.vertices.indexOf(vertex1)] = true;

        while((!stk.isEmpty())&(isFound== false)) {
            currentNode = stk.peek();
            currentIndex = this.vertices.indexOf(currentNode);
            LinkedList<T> trackedArcs = this.arcs.get(currentIndex);
            int i = 0;
            
            while(i <= trackedArcs.size()) {
                if (indexOfList == trackedArcs.size()) {
                    stk.pop();
                }
                else if (marked[this.vertices.indexOf(trackedArcs.get(i))] == false) {
                    if (trackedArcs.get(i) == vertex2) {  // push vertex and add to list if it hasn't been visited
                        isFound = true;
                    }
                    stk.push(trackedArcs.get(i)); 
                    result.add(trackedArcs.get(i));
                    marked[this.vertices.indexOf(trackedArcs.get(i))] = true;

                    i = trackedArcs.size() + 1; 
                } 
                i++;
            }
        }
        return isFound ? result : new LinkedList<T>();
    }
    
    /** 
     * Does a breadth first search of the graph.
     * 
     * @param T starting vertex 
     * @return LinkedList<T> containing the vertices that are the outcome of the breadth first search
     */
    public LinkedList<T> breadthFirstSearch(T vertex) {
        LinkedQueue<T> q = new LinkedQueue<T>();       
        LinkedList<T> iterator = new LinkedList<T>();
        int count = 0;

        boolean[] marked = new boolean[this.vertices.size()];

        for (int v = 0; v < this.vertices.size(); v++){ // mark each vertex as unvisited
            marked[v] = false;
        }

        q.enqueue(vertex);
        marked[(this.vertices.indexOf(vertex))] = true;
        T currentVertex;
        int currentIndex;
        LinkedList<T> currentList;

        while (!q.isEmpty()) {
            currentVertex = q.dequeue();
            currentIndex = this.vertices.indexOf(currentVertex);

            iterator.add(current);
            count++;
            currentList = this.arcs.get(currentIndex);
            for(int i = 0; i < currentList.size(); i++) {
                T currentNode = currentList.get(i);

                // enqueue vertex if it hasn't been visited and if arcs exist at from vertex (not null)
                if (!marked[this.vertices.indexOf(currentNode)] && !(currentNode ==null)) {
                    q.enqueue(currentNodeInList);
                    marked[this.vertices.indexOf(currentNode)] = true;
                }
            }
        }
        return iterator;
    }

    /**
     * Main method for testing.
     */ 
    public static void main(String[] args) {
        System.out.println("***Test Graph of Strings***");
        AdjListsGraph<String> g1 = new AdjListsGraph<String>();
        g1.addVertex("a");
        g1.addVertex("b");
        g1.addVertex("c");
        g1.addVertex("d");
        g1.addVertex("e");
        g1.addArc("a", "b");
        g1.addEdge("a", "c");
        g1.removeVertex("e");
        System.out.println(g1.toString());
        System.out.println("isArc(): \nExpected: true \nGot:" + g1.isArc("a", "b"));
        System.out.println("\nisEdge(): \nExpected: false \nGot:" + g1.isEdge("a", "b"));
        System.out.println(g1.depthFirstSearch("a"));
        System.out.println((g1.breadthFirstSearch("a")));
        // g1.saveToTGF("string_graph.txt");

        System.out.println("\n***Test Tree of Strings***");
        AdjListsGraph<String> tree = new AdjListsGraph<String>();
        tree.addVertex("a");
        tree.addVertex("b");
        tree.addVertex("c");
        tree.addVertex("d");
        tree.addVertex("e");
        tree.addVertex("f");
        tree.addVertex("g");
        tree.addVertex("h");
        tree.addVertex("i");
        tree.addVertex("j");
        tree.addEdge("a","b");
        tree.addEdge("a","c");
        tree.addEdge("b","d");
        tree.addEdge("b","e");
        tree.addEdge("c","f");
        tree.addEdge("c","g");
        tree.addEdge("d","h");
        tree.addEdge("d","i");
        tree.addEdge("e","j");
        System.out.println(tree.toString());
        System.out.println("breadthFirstSearch(): \nExpected: {a, b, c, d, e, f, g, h, i, j} \nGot:");
        System.out.println((tree.breadthFirstSearch("a")));
        System.out.println("\ndepthFirstSearch(): \nExpected: {a, b, c, d, e, f, g, h, i, j} \nGot:");
        System.out.println((tree.depthFirstSearch("a")));
        System.out.println(tree.toString());
        // tree.saveToTGF("Tree.tgf");

        System.out.println("\n***Test Cycle of Strings***");
        AdjListsGraph<String> cycle = new AdjListsGraph<String>();
        cycle.addVertex("1");
        cycle.addVertex("2");
        cycle.addVertex("3");
        cycle.addVertex("4");
        cycle.addVertex("5");
        cycle.addEdge("1","2");
        cycle.addEdge("2","3");
        cycle.addEdge("3","4");
        cycle.addEdge("4","5");
        cycle.addEdge("5","1");
        System.out.println(cycle.toString());
        System.out.println("breadthFirstSearch(): \nExpected: {1, 2, 3, 4, 5} \nGot:");
        System.out.println((cycle.breadthFirstSearch("1")));
        System.out.println("\ndepthFirstSearch(): \nExpected: {1, 2, 3, 4, 5} \nGot:");
        System.out.println((cycle.depthFirstSearch("1")));
        System.out.println(cycle.toString());
        // cycle.saveToTGF("Cycle.tgf");

        System.out.println("\n***Test Disconnected Graph***");
        AdjListsGraph<String> disconnected = new AdjListsGraph<String>();
        disconnected.addVertex("1");
        disconnected.addVertex("2");
        disconnected.addVertex("3");
        disconnected.addVertex("4");
        disconnected.addVertex("5");
        disconnected.addVertex("2");
        disconnected.addEdge("1","2");
        disconnected.addArc("2","3");
        disconnected.addEdge("3","4");
        System.out.println(disconnected.toString());
        System.out.println("breadthFirstSearch(): \nExpected: {1, 2, 3, 4} \nGot:");
        System.out.println((disconnected.breadthFirstSearch("1")));
        System.out.println("depthFirstSearch(): \nExpected: {1, 2, 3, 4} \nGot:");
        System.out.println((disconnected.depthFirstSearch("1")));
        System.out.println("depthFirstSearch2(): \nExpected: {1, 2, 3, 4, 5} \nGot:");
        System.out.println(disconnected.depthFirstSearch2("1", "5"));
        System.out.println(disconnected.toString());
        // disconnected.saveToTGF("Disconnected.tgf");
    }
}