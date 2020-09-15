import java.util.Hashtable;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Vector;

import sun.rmi.transport.tcp.TCPConnection;

import java.util.LinkedList;
import java.util.Arrays;
import java.io.IOException;
import java.net.URL;

/**
 * TweeterCollection contains a Graph of screen names and stories, Hashtable of screen names 
 * and its corresponding Twitter user, and a Hashtable of stories and its reference 
 * frequency. It also reads in data on the graph components from a CSV and collects 
 * additional data to be further synthesized.
 *
 * TweeterCollection.java
 * @author Alexandra Bullen-Smith
 * @author Peyton Wang
 * @version 12/9/19
 */

public class TweeterCollection {
    // instance variables
    private AdjListsGraph<String> graph;
    private Hashtable<String,TweeterUser> usersTable;
    private Hashtable<String,Integer> storyTable;
    private int totalStories;
    private int totalTweets;
    private int totalUsers;
    private String mostPopStory;
    private String leastPopStory;
    private String mostActiveWriter;
    private String mostActiveTweeter;
    private String urlFindStoryTitle = "http://twittertrails.wellesley.edu/~trails/stories/title.php?id=";

    /**
     * Constructor creates a CollectionOfRats object and initializes instance variables.
     * 
     * @param String name of CSV file name to be read in
     */
    public CollectionOfRats(String csvFileName) {
        // initialize instance variables
        this.graph = new AdjListsGraph<String>();
        this.usersTable = new Hashtable<String,TweeterUser>();
        this.storyTable = new Hashtable<String,Integer>();
        this.readRats(csvFileName);
    }

    /**
     * Getter method for obtaining the number of stories.
     * 
     * @return int total number of stories 
     */
    public int getTotalStories(){
        return totalStories;
    }
    
    /**
     * Getter method for obtaining the number of tweets. 
     * 
     * @return int total number of tweets
     */
    public int getTotalTweets(){
        return totalTweets;
    }
    
    /**
     * Getter method for obtaining the number of users.
     * 
     * @return int total number of users
     */
    public int getTotalUsers(){
        return totalUsers;
    }
    
    /**
     * Getter method for obtaining the most popular story
     * 
     * @return String ID of the most popular story
     */
    public String getMostPopStory() {
        return this.mostPopStory;
    }

    /**
     * Getter method for obtaining the least popular story
     * 
     * @return String ID of the least popular story
     */
    public String getLeastPopStory() {
        return this.leastPopStory;
    }

    /**
     * Getter method for obtaining the most active writer.
     * 
     * @return String username of the most active writer
     */
    public String getMostActiveWriter() {
        return this.mostActiveWriter;
    }

    /**
     * Getter method for obtaining the most active tweeter.
     * 
     * @return String username of the most active tweeter
     */
    public String getMostActiveTweeter() {
        return this.mostActiveTweeter;
    }

    /**
     * Reads in the data on graph components from the CSV file and 
     * adds the data to the data structures.
     * 
     * @param String CSV file name
     */
    private void readRats(String csvFilename) {
        try {
            Scanner scan = new Scanner(new File(csvFilename));

            while(scan.hasNextLine()) { // keep scanning until reaches last line of data
                String line = scan.nextLine();

                // in each line, split data values separated by tab into array
                String[] lineArray = line.split("\t"); 

                // store each data value in a variable
                String screenName = lineArray[0];
                String id = lineArray[1];
                String tweetCount = lineArray[2];
                String storyCount = lineArray[3];

                graph.addVertex(screenName);  // add each screen name as a vertex 

                // create new instance of Tweeter user based on above data values
                TweeterUser user = new TweeterUser(id,screenName,Integer.parseInt(tweetCount),Integer.parseInt(storyCount));
                usersTable.put(screenName,user);  // add user to user table

                totalTweets += Integer.parseInt(tweetCount);  // accumulate total # of tweets
                totalUsers++;

                // split stories separated by comma into array
                String[] storiesArray = lineArray[4].split(",");

                int numReposted = 0; 

                for (int i = 0; i < storiesArray.length; i++) {
                    if (storyTable.containsKey(storiesArray[i])) {
                        numReposted = storyTable.get(storiesArray[i]);

                        // go to story in hashtable and increment its frequency
                        storyTable.put(storiesArray[i], numReposted++);
                    } else {
                        storyTable.put(storiesArray[i], 1);  // add story with frequency of 1
                        totalStories++;
                        graph.addVertex(storiesArray[i]);  // add each story as a vertex
                    }

                    // create an edge between each story and its associated screen name
                    graph.addEdge(screenName, storiesArray[i]); 
                }
            }
        } catch(FileNotFoundException e) {
            System.out.println("File " + e + " not found.");
        }
    }

    /**
     * Calculates the statistics of the story, such as the IDs of the most and least popular stories.
     * Sets the instance variables for most and least popular stories.
     */
    public void calculateStoryStats() {
        int maxStorySize = 0;
        int currentSize;
        int minStorySize = Integer.MAX_VALUE;

        for(String storyId: storyTable.keySet()) { // iterate through keys in story hashtable
            currentSize = (graph.getSuccessors(storyId)).size(); //should we change

            if (currentSize > maxStorySize) {
                this.mostPopStory = storyId;
                maxStorySize = currentSize;
            }

            if (currentSize < minStorySize) {
                this.minStorySize = storyId;
                minStorySize = currentSize;
            }
        }
    }

    /**
     * Calculates the most and least active users in terms of stories and tweets.
     */
    public void calculateUserStats() {
        int maxStories = 0;
        int currentSize;
        int maxTweets = 0;
        int currentTweets;
        int minTweets = Integer.MAX_VALUE;

        for(String screenName: usersTable.keySet()) {
            currentSize = (graph.getSuccessors(screenName)).size();
           currentTweets = usersTable.get(screenName).getTweetCount();

            if (currentSize > mostStoriesSoFar) {
                this.mostActiveWriter = maxStories;
                maxStories = currentTweets;
            }

            if (currentTweets > maxTweeets){
                mostActiveTweeter = screenName;
                maxStories = currentTweets;
            }
        }
    }

    /**
     * Finds the size of the largest connected component.
     * 
     * @return int size of the LCC
     */
    public int findLCCSize() {
        Vector<String> vertices = graph.getAllVertices();
        int maxSize = 0;
        int currentSize;
        LinkedList<String> dfsList = new LinkedList<String>(); 

        for (int i = 0; i < vertices.size(); i++) {
            dfsList = graph.depthFirstSearch((vertices.elementAt(i)));
            currentSize = dfsList.size();

            if (currentSize > maxSize) {
                maxSize = currentSize;
            }
        }
        return maxSize;
    }

    /**
     * Implements a breadth first search on every node and finds the shortest 
     * connected component.
     * 
     * @return size of shortest connected component
     */
    public int findShortestConnectedComponentSize() {
        Vector<String> vertices = graph.getAllVertices();
        int minDist = Integer.MAX_VALUE;
        int currentDist;

        for (int i = 0; i < vertices.size(); i++) {
            LinkedList<String> bfsList = graph.breadthFirstSearch((vertices.elementAt(i)));
            currentDist = bfsList.size();

            if (currentDist < minDist) {

                minDist = currentDist;
            }
        }
        return shortestDistSoFar;
    }

    /**
     * Reterns a boolean indicating whether this graph is connected.
     * 
     * @return boolean true if the size of the LCC equals the number of verties, falseotherwise
     */
    public boolean isConnected() {
        return this.findLCCSize() == graph.getNumVertices();
    }

    /**
     * Getter method that takes in the ID of a story and returns its title.
     * 
     * @param String  // ID of story
     * @return  // String title of the story
     */
    public String getStoryTitle(String storyId) {
        String storyTitle = ""; 
        try {
            URL url = new URL(urlFindStoryTitle + storyId);
            Scanner scan = new Scanner(url.openStream());
            scan.nextLine();
            storyTitle = scan.nextLine();
        } catch(IOException e){
            System.out.println(e);  
        }
        return storyTitle;
    }

    /**
     * getGraph() is a getter that returns the graph of the users and stories
     */
    public AdjListsGraph<String> getGraph(){
        return graph;
    }

    /**
     * Finds the most central node by going through all the nodes using DFS starting from any node,
     * and finding the smallest sum. 
     * 
     * @return String element stored in the the first central node
     */
    public String findMostCentralNode(){
        int total = 0;
        int leastSizeSoFar = Integer.MAX_VALUE;
        String mostCentralSoFar = "";
        LinkedList<String> allCentralNodes = new LinkedList<String>();
        LinkedList<String> allCentralNodesFinal = new LinkedList<String>();

        //initial to for loops to get some of the potential central nodes
        for (String first : graph.getAllVertices()){
            total = 0;
            
            for(String second: graph.getAllVertices()){
                total += graph.depthFirstSearchWithTwo(first,second).size();
            }

            if (total<leastSizeSoFar){
                leastSizeSoFar=total;
                allCentralNodes.add(first);
                mostCentralSoFar = first;
            }
        }
        return mostCentralSoFar;
    }

    /**
     * main method for testing
     */ 
    public static void main(String[] args){
        TweeterCollection tc = new TweeterCollection("All_Russian-Accounts-in-TT-stories.csv.tsv");
        System.out.println(tc.graph.toString());
        tc.graph.saveToTGF("ratsTEST.tgf");
        tc.calculateStoryStats();
        tc.calculateUserStats();
        System.out.println("total vertices: " + tc.graph.getNumVertices());
        System.out.println("total tweets: " + tc.totalTweets);
        System.out.println("total stories: " + tc.totalStories);
        System.out.println("total users: " + tc.totalUsers);
        System.out.println("most active story user: "+ tc.getMostActiveWriter());
        System.out.println("most active Tweeter user: "+ tc.getMostActiveTweeter());
        System.out.println("LCC size: " + tc.findLCCSize());
        System.out.println(tc.findMostCentralNode());
        System.out.println("shortest distance: " + TCPConnection.findShortestDist());
        String mostPopStoryId = tc.getMostPopStory();
        String leastPopStoryId = tc.getLeastPopStory();
        System.out.println("id of most popular story: " + mostPopStoryId);
        System.out.println("title of most popular story: " + tf.getStoryTitle(mostPopStoryId));
        System.out.println("id of least popular story: " + tcPopStoryId);
        System.out.println("title least popular story: " + tcgetStoryTitle(tc.getLeastPopStory()));

    }
}
