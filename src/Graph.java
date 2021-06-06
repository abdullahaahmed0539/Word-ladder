/******************************************************************************
 *Name: Abdullah Ahmed Khan
 *ERP: 18092
 ******************************************************************************/

import jdk.swing.interop.SwingInterOpUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.*;

 public class Graph {

    // symbol table: key = string vertex, value = set of neighboring vertices
    private TreeMap<String, TreeSet<String> > st;

    // number of edges
    private int E;

   /**
     * Initializes an empty graph with no vertices or edges.
     */
    public Graph() {
        st = new TreeMap<String, TreeSet<String>>();
    }

   /**
     * Initializes a graph from the specified file, using the specified delimiter.
     *
     * @param filename the name of the file
     * @param delimiter the delimiter
     */
    public Graph(String filename, String delimiter) {
        st = new TreeMap<String, TreeSet<String>>();
        try {
            Scanner in = new Scanner(new File(filename));
            while (in.hasNextLine()) {
                String line = in.nextLine();
                String[] names = line.split(delimiter);
                addVertex(names[0]);
                String [] s = st.keySet().toArray(new String[0]);
                for (int i = 1; i < V(); i++) {
                    if(wordDifferMoreThanOne(names[0], s[i]) == true) {
                        addEdge(names[0], s[i]);
                    }
                }
            }
        }
        catch(FileNotFoundException ex) {
            System.err.println(filename + " not found! Returning empty graph");
        }
    }

    public boolean wordDifferMoreThanOne(String word1, String word2){
        int numberOfDifferentLetters = 0;
        for (int i = 0 ; i < word1.length() ; i++){
            if (word1.charAt(i) == word2.charAt(i)){
                continue;
            }else{
                numberOfDifferentLetters++;
            }
        }

        if(numberOfDifferentLetters == 1){
            return true;
        }
        return false;
    }
   /**
     * Returns the number of vertices in this graph.
     *
     * @return the number of vertices in this graph
     */
    public int V() {
        return st.size();
    }

   /**
     * Returns the number of edges in this graph.
     *
     * @return the number of edges in this graph
     */
    public int E() {
        return E;
    }

    // throw an exception if v is not a vertex
    private void validateVertex(String v) {
        if (!hasVertex(v)) throw new IllegalArgumentException(v + " is not a vertex");
    }

   /**
     * Returns the degree of vertex v in this graph.
     *
     * @param  v the vertex
     * @return the degree of {@code v} in this graph
     * @throws IllegalArgumentException if {@code v} is not a vertex in this graph
     */
    public int degree(String v) {
        validateVertex(v);
        return st.get(v).size();
    }

   /**
     * Adds the edge v-w to this graph (if it is not already an edge).
     *
     * @param  v one vertex in the edge
     * @param  w the other vertex in the edge
     */
    public void addEdge(String v, String w) {
        if (!hasVertex(v)) addVertex(v);
        if (!hasVertex(w)) addVertex(w);
        if (!hasEdge(v, w)) E++;
        st.get(v).add(w);
        st.get(w).add(v);
    }

   /**
     * Adds vertex v to this graph (if it is not already a vertex).
     *
     * @param  v the vertex
     */
    public void addVertex(String v) {
        if (!hasVertex(v)) st.put(v, new TreeSet<String>());
    }


   /**
     * Returns the vertices in this graph.
     *
     * @return the set of vertices in this graph
     */
    public Iterable<String> vertices() {
        return st.keySet();
    }

   /**
     * Returns the set of vertices adjacent to v in this graph.
     *
     * @param  v the vertex
     * @return the set of vertices adjacent to vertex {@code v} in this graph
     * @throws IllegalArgumentException if {@code v} is not a vertex in this graph
     */
    public Iterable<String> adjacentTo(String v) {
        validateVertex(v);
        return st.get(v);
    }

   /**
     * Returns true if v is a vertex in this graph.
     *
     * @param  v the vertex
     * @return {@code true} if {@code v} is a vertex in this graph,
     *         {@code false} otherwise
     */
    public boolean hasVertex(String v) {
        return st.containsKey(v);
    }

   /**
     * Returns true if v-w is an edge in this graph.
     *
     * @param  v one vertex in the edge
     * @param  w the other vertex in the edge
     * @return {@code true} if {@code v-w} is a vertex in this graph,
     *         {@code false} otherwise
     * @throws IllegalArgumentException if either {@code v} or {@code w}
     *         is not a vertex in this graph
     */
    public boolean hasEdge(String v, String w) {
        validateVertex(v);
        validateVertex(w);
        return st.get(v).contains(w);
    }


     public String BFS(String startWord, String endWord)
     {

        // Create a queue for BFS
         LinkedList<String> queue = new LinkedList<String>();
         ArrayList<String> wordsAlreadyUsed = new ArrayList <String>() ;
         String arr [][] = new String[V()][2];

         String [] s = st.keySet().toArray(new String[0]);
         int indexInVerticesArray = 0;
         Boolean isFound = false;
         String temp;
         int j = 0;

         for(int i = 0 ; i<V(); i++){
             if (s[i].equals(startWord)){
                 indexInVerticesArray = i;
                 wordsAlreadyUsed.add(s[indexInVerticesArray]);
                 break;
             }
         }

         queue.add(s[indexInVerticesArray]);

         while(!isFound){
             temp = queue.poll();
             if(temp!=null){
                 String [] edges = st.get(temp).toArray(new String[0]);
                 for(int i = 0 ; i < edges.length ; i++){
                     if(!wordsAlreadyUsed.contains(edges[i])){
                         arr[j][0] = edges[i];
                         arr[j][1] = temp;
                         j++;
                         String pass = edges[i];
                         wordsAlreadyUsed.add(pass);
                         queue.add(edges[i]);
                         if (arr[j-1][0].equals(endWord)){
                             isFound = true;
                             break;
                         }
                     }
                 }
             }
             else{
                 isFound = true;
             }
         }

        isFound = false;
        temp = endWord;
        int tempIndex = 0;
        String path= "";

        while(!temp.equals(startWord) && !temp.equals("no path")){
            for(int i = 0; i < arr.length ; i++){
                if(temp.equals(arr[i][0])){
                    tempIndex = i;
                    isFound = true;
                }
            }

            if(isFound){
                if(path.equals("")){
                    path = temp ;
                }
                else{
                    path = temp + " ---> " +path ;
                }
                temp = arr[tempIndex][1];
            }else{
                System.out.println("there exist no path from "+ startWord +" to "+endWord);
                temp = "no path";
            }
        }

        if(isFound && !temp.equals("no path")){
            path= temp + " ---> "+path;
        }

        return path;
     }

   /**
     * Returns a string representation of this graph.
     *
     * @return string representation of this graph
     */
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (String v : st.keySet()) {
            s.append(v + ": ");
            for (String w : st.get(v)) {
                s.append(w + " ");
            }
            s.append('\n');
        }
        return s.toString();
    }

   /**
     * Unit tests the {@code Graph} data type.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        // create graph
        Scanner sc = new Scanner(System.in);
        String word1, word2;
        Graph graph = new Graph("sgb-words.txt", "\\s+");

        System.out.println();
        System.out.println("******************************************QUESTION 1***************************************************");
        System.out.print("Do you want to see the graph? (y/n): ");
        word1 = sc.nextLine();
        System.out.println();
        System.out.println();
        System.out.println();

        if (word1.equals("Y") || word1.equals("y")) {
            // print out graph
            System.out.println("************************PRINTING GRAPH*************************");
            System.out.println(graph);
            System.out.println();
            System.out.println();
            System.out.println();


            // print number of vertices
            System.out.println("************************INFO ON GRAPH*************************");
            System.out.println("n = |V| = " + graph.V());
            // print number of edges
            System.out.println("m = |E| = " + graph.E());
            System.out.println();
            System.out.println();
            System.out.println();
        }
            //Enter words
            System.out.println("************************USER INPUT FOR WORDS*************************");

            System.out.println("Enter two 5-letter words (length != 5 to end) :- ");
            System.out.print("Enter Word1 : ");
            word1 = sc.nextLine();
            System.out.print("Enter Word2 : ");
            word2 = sc.nextLine();

            if (word1.length()==5 && word2.length()==5) {
                System.out.println();
                System.out.println("************************SHORTEST PATH*************************");
                System.out.println(graph.BFS(word1, word2));
            } else {
                System.out.println();
                System.out.println();
                System.out.println();
                System.out.println("************************EXITING*************************");
                System.out.println("Thank you for visiting. ");
                System.out.println("Assignment done by Abdullah Ahmed Khan ERP: 18092");
                System.err.println("Exiting program......");
            }
        }


}
