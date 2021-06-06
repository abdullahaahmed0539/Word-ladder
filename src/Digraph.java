/******************************************************************************
 *  Directed graph data type implemented using a symbol table
 *  whose keys are strings and whose values are sets of strings.
 ******************************************************************************/
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Digraph {

    // symbol table of linked lists
    private TreeMap<String, TreeSet<String> > st;

    // create an empty digraph
    public Digraph() {
        st = new TreeMap<String, TreeSet<String>>();
    }

    // add v to w's list of neighbors; self-loops allowed
    public void addEdge(String v, String w) {
        if (!st.containsKey(v)) addVertex(v);
        if (!st.containsKey(w)) addVertex(w);
        st.get(v).add(w);
    }

    public int V() {
        return st.size();
    }


    // add a new vertex v with no neighbors if vertex does not yet exist
    public void addVertex(String v) {
        if (!st.containsKey(v)) st.put(v, new TreeSet<String>());
    }

    // return the degree of vertex v
    public int degree(String v) {
        if (!st.containsKey(v)) return 0;
        else                 return st.get(v).size();
    }

    // return the array of vertices incident to v
    public Iterable<String> adjacentTo(String v) {
        if (!st.containsKey(v)) return new TreeSet<String>();
        else                 return st.get(v);
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        for (String v : st.keySet()) {
            s.append(v + ": ");
            for (String w : st.get(v)) {
                s.append(w + " ");
            }
            s.append("\n");
        }
        return s.toString();
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


    public boolean CanAnEdgeExist (String word1, String word2, int numberOfLetters){

        int numberOfSameLetters = 0;
        boolean accountedFor [] = new boolean[word2.length()];
        char [] lastFour = new char[numberOfLetters];

        for(int i = 0 ; i < word1.length()-1 ; i++){
            lastFour[i] = word1.charAt(word1.length()-i-1) ;
        }

        for (int i = 0; i < lastFour.length; i++) {
            for (int j = 0; j < word2.length(); j++) {
                if(lastFour[i] == word2.charAt(j) && !accountedFor[j]){  //new
                    numberOfSameLetters++;
                    accountedFor[j] = true; //new
                    break;
                }
            }
        }

        if( numberOfSameLetters >= 4){
            return true;
        }

        return false;
    }

    public void reverseGraph(){
        String [] s = st.keySet().toArray(new String[0]);
        for (int i = 0; i < s.length; i++) {
           // System.out.println(i + " " +V());
            String [] e = st.get(s[i]).toArray(new String[0]);
            for (int j = 0; j < e.length; j++) {
                addEdge(e[j],s[i]);
                st.get(s[i]).remove(e[j]);
            }
        }

    }

    void scc ( Digraph G2){
       ArrayList<String> s;
        int largest = -1;
        int p = -1;
        String v1 [ ] = st.keySet().toArray(new String [0]);

        G2.reverseGraph();
        while(G2.V()!=0) {
            String x = G2.indexOfhighestPostnumberTHroughDF();
           // System.out.println(x);
            for (int i = 0; i < V(); i++) {
                if(v1[i].equals(x)){
                    p = i;
                }
            }
            s = DFS(p);
            System.out.print("Component[");
            for (int i = 0; i < s.size(); i++) {
                System.out.print(s.get(i) + "   ");
            }
            System.out.println("]");
//            System.out.println("size:" + s.size());
            if (s.size() > largest) {
                largest = s.size();
            }




            String[] s1 = G2.st.keySet().toArray(new String[0]);  //deletion from vertices
            for (int i = 0; i < G2.V(); i++) {
                String[] q = G2.st.get(s1[i]).toArray(new String[0]);
                for (int j = 0; j < q.length; j++) {
                    if (s.contains(q[j])) {
                        G2.st.get(q[j]).remove(s1[i]);
                        G2.st.get(s1[i]).remove(q[j]);
                    }
                }
            }


            for (int i = 0; i < s.size(); i++) {
                G2.st.keySet().remove(s.get(i));
            }



//            if(s.get(0).equals("brews")){
//                System.out.println(G2.toString());
//            }
            s.clear();
        }

        System.out.println(largest);

    }

    ArrayList<String> DFS (int index){
        boolean visited [] = new boolean[V()];
        String visitStack [][] = new String[V()][2];
        String [] v = st.keySet().toArray(new String[0]);
        int indexInVerticesArray, c;
        indexInVerticesArray=c = 0;
        ArrayList<String> s = new ArrayList<>();



        for (int i = 0; i < V() ; i++) {
            if(visitStack[i][0]==null){
                for (int k = 0; k < V(); k++) {
                    if (visited[k] == false){
                        visited[k] = true;
                        visitStack[c][0] = v[k];
                        c++;
                        break;
                    }
                }
            }
            String [] e = st.get(visitStack[i][0]).toArray(new String[0]);
            for (int k = 0; k < e.length; k++) { //loop to iterate through edges

                for(int l = 0 ; l<V(); l++){  //loop to find index of edge in vertice arr
                    if (e[k].equals(v[l])){
                        indexInVerticesArray = l;
                        break;
                    }
                }

                if(visited[indexInVerticesArray] == false){
                    visited[indexInVerticesArray] = true;
                    visitStack[c][0] = e[k];
                    c++;
                    visitStack[indexInVerticesArray][1] = visitStack[i][0];
                }
            }

        }




        String current = "";
        boolean isfound = false;
        boolean visit [] = new boolean[visitStack.length];



        current = v[index];

        if(!s.contains(current)){
            s.add(current);

        }

        for (int i = 0; i < visitStack.length; i++) {
            if(current.equals(visitStack[i][0])){
                visitStack[i][1] = null;
            }
        }

            while(current!=null){

                for (int j = 0; j < visitStack.length; j++) {
                    if (current.equals(visitStack[j][1]) && !visit[j]){
                        current = visitStack[j][0];
                        if(current!=null){

                            if(!s.contains(current)){

                                s.add(current);
                            }
                        }
                        isfound = true;
                        break;
                    }
                }

                if(!isfound ){
                    for (int j = 0; j < visitStack.length ; j++) {
                        if (visitStack[j][0].equals(current)){
                            visit[j] = true;
                            current = visitStack[j][1];
                            if(current!=null){

                                if(!s.contains(current)){

                                    s.add(current);
                                }
                            }
                            break;
                        }
                    }
                }
                isfound = false;
            }

        return s;
    }

    String indexOfhighestPostnumberTHroughDF(){
        boolean visited [] = new boolean[V()];
        int prepostnumbers [][] = new int[V()][2];
        String visitStack [][] = new String[V()][2];
        String [] v = st.keySet().toArray(new String[0]);
        int pcount,count,indexInVerticesArray, c;
        pcount = count = indexInVerticesArray=c = 0;
        String highest = null;


        for (int i = 0; i < V() ; i++) {
            if(visitStack[i][0]==null){
                for (int k = 0; k < V(); k++) {
                    if (visited[k] == false){
                        visited[k] = true;
                        visitStack[c][0] = v[k];
                        c++;
                        break;
                    }
                }
            }
            String [] e = st.get(visitStack[i][0]).toArray(new String[0]);
            for (int k = 0; k < e.length; k++) { //loop to iterate through edges

                for(int l = 0 ; l<V(); l++){  //loop to find index of edge in vertice arr
                    if (e[k].equals(v[l])){
                        indexInVerticesArray = l;
                        break;
                    }
                }

                if(visited[indexInVerticesArray] == false){
                    visited[indexInVerticesArray] = true;
                    visitStack[c][0] = e[k];
                    c++;
                    visitStack[indexInVerticesArray][1] = visitStack[i][0];
                }
            }

        }



        String current = "";
        boolean isfound = false;
        boolean visit [] = new boolean[visitStack.length];

        while(count < visitStack.length){
            for (int j = count; j < visitStack.length ; j++) {
                if (visitStack[j][1]==null){
                    current = visitStack [j][0];
                    count =j+1;
                    pcount++;
                    prepostnumbers[j][0] = pcount;
                    break;
                }
                if(j==visitStack.length -1){
                    count = j+1;
                }
            }

            while(current!=null){

                for (int j = 0; j < visitStack.length; j++) {
                    if (current.equals(visitStack[j][1]) && !visit[j]){
                        current = visitStack[j][0];
                        pcount++;
                        prepostnumbers[j][0] = pcount;
                        isfound = true;
                        break;
                    }
                }

                if(!isfound ){
                    for (int j = 0; j < visitStack.length ; j++) {
                        if (visitStack[j][0].equals(current)){
                            visit[j] = true;
                            current = visitStack[j][1];
                            pcount++;
                            prepostnumbers[j][1] = pcount;
                            break;
                        }
                    }
                }
                isfound = false;
            }

        }
        int max, indexofhighest;
        max = indexofhighest = 0;
        for (int i = 0; i <visitStack.length ; i++) {
            if(prepostnumbers[i][1]>max){
                max = prepostnumbers[i][1];
                indexofhighest = i;
            }
        }

        for (int i = 0; i < v.length; i++) {
            if(v[i].equals(visitStack[indexInVerticesArray][0])){
                indexofhighest = i;
            }
        }

     //   System.out.println(v[indexofhighest]);
        highest = v[indexofhighest];

       return highest;
    }

    void creatGraph(String fileName, Digraph G) {
        ArrayList<String> dump = new ArrayList<>();
        try {
            Scanner in = new Scanner(new File(fileName));
            while (in.hasNextLine()) {
                String line = in.nextLine();
                String[] names = line.split("\\s+");

                dump.add(names[0]);
            }
        }
        catch(FileNotFoundException ex) {
            System.err.println(fileName + " not found! Returning empty graph");
        }

        for (int i = 0; i < dump.size(); i++) {
            for (int j = 0; j < dump.size(); j++) {
                if(i!=j){
                    if(CanAnEdgeExist(dump.get(i), dump.get(j), 4)) {
                        G.addEdge(dump.get(i), dump.get(j));
                    }
                }
            }
        }

    }


    public static void main(String[] args) {
        Digraph G = new Digraph();
        Digraph G2 = new Digraph();
        String fileName;
        String word1, word2;
        fileName = "sgb-words.txt";

        Scanner sc = new Scanner(System.in);

        G.creatGraph(fileName, G);
        G2.creatGraph(fileName, G2);

        System.out.println("******************************************QUESTION 2***************************************************");
        System.out.print("Do you want to see the graph? (y/n): ");
        word1 = sc.nextLine();
        System.out.println();
        System.out.println();
        System.out.println();



        if (word1.equals("Y") || word1.equals("y")) {
            // print out graph
            System.out.println("************************PRINTING GRAPH*************************");
            System.out.println(G);
            System.out.println();
            System.out.println();
            System.out.println();
        }



        System.out.println("************************USER INPUT FOR WORDS*************************");
        System.out.println("Enter two 5-letter words (length != 5 to end) :- ");
        System.out.print("Enter Word1 : ");
        word1 = sc.nextLine();
        System.out.print("Enter Word2 : ");
        word2 = sc.nextLine();
        System.out.println();


        if (word1.length()==5 && word2.length()==5) {
            System.out.println();
            System.out.println("************************SHORTEST PATH*************************");
            System.out.println(G.BFS(word1, word2));





        } else {
            System.out.println();
            System.out.println();
            System.out.println();
            System.out.println("************************EXITING*************************");
            System.out.println("Thank you for visiting. ");
            System.out.println("Assignment done by Abdullah Ahmed Khan ERP: 18092");
            System.err.println("Exiting program......");
        }

        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println("******************************************QUESTION 3***************************************************");
        System.err.println("This will take some time but i am telling in advance max size is 5294 :). Enjoy while it takes time to find out.");
        G.scc(G2);


//        System.out.println();
//        System.out.println();
//        System.out.println();
//        System.out.println("******************************************QUESTION 4***************************************************");
//        System.out.println("Enter two 5-letter words (length != 5 to end) :- ");
//        System.out.print("Enter Word1 : ");
//        word1 = sc.nextLine();

    }




}

