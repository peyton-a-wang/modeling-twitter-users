/**
 * InvestigateDriver is a driver class that creates a collection of Twitter accounts from data in the CSV
 * file and then investigates the activity of Russian bots.
 *
 * InvestigateDriver.java
 * @author Alexandra Bullen-Smith
 * @author Peyton Wang
 * @version 12/9/19
 */

public class InvestigateDriver {
    public static void main(String[] args) {
        System.out.println("***Test Russian Accounts CSV***\n");
        TweeterCollection tc = new TweeterCollection("All_Russian-Accounts-in-TT-stories.csv.tsv");
        
        tc.getGraph().saveToTGF("tweeterGraph.tgf");
        tc.calculateStoryStats();
        tc.calculateUserStats();
        
        System.out.println("Basic Graph Data:");
        System.out.println("Total vertices: " + tc.getGraph().getNumVertices());
        System.out.println("Total tweets: " + tc.getTotalTweets());
        System.out.println("Total stories: " + tc.getTotalStories());
        System.out.println("Total users: " + tc.getTotalUsers());
        System.out.println("Most active story user: " + tc.getMostActiveStoryUser());
        System.out.println("Most active Tweeter user: "+ tc.getMostActiveTweeterUser());
        
        System.out.println("\nGraph Data from DFS and BFS:");
        System.out.println("Largest component size: " + tc.findLCCSize());
        System.out.println("Shortest connected component size: " + tc.findShortestConnectedComponentSize());
        
        System.out.println("\nStory Trends to Analyze:");
        String mostPopStoryId = tc.getMostPopStory();
        String leastPopStoryId = tc.getLeastPopStory();
        System.out.println("ID of the most popular story: " + mostPopStoryId);
        System.out.println("Title of the most popular story: " + tc.getStoryTitle(mostPopStoryId));
        System.out.println("ID of least popular story: " + tc.PopStoryId);
        System.out.println("Title of the least popular story: " + tc.getStoryTitle(tc.getLeastPopStory()));
    }
}
