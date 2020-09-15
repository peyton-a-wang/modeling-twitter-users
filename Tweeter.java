/**
 * The Tweeter class represents a Twitter user's account that consists of a user ID, 
 * screen name, and number of tweets and stories.
 *
 * Tweeter.java
 * @author Alexandra Bullen-Smith
 * @author Peyton Wang
 * @version 12/9/19
 */

public class Tweeter {
    
    // instance variables
    private String id;
    private String screenName;
    private int tweetCount;
    private int storyCount;
    
    /**
     * Constructor creates a Tweeter object and initializes instance variables.
     * 
     * @param String id
     * @param String screen name
     * @param int number of tweets
     * @param int number of stories
     */
    public Tweeter(String id, String screen, int tweetCount, int storyCount) {
        this.id = id;
        this.screenName = screen;
        this.tweetCount = tweetCount;
        this.storyCount = storyCount;
    }
    
    /**
     * Getter method for obtaining the ID of the Twitter user.
     * 
     * @return String ID of the user
     */
    public String getId() {
        return this.id;
    }
    
    /**
     * Getter method for obtaining the name of the Twitter user.
     * 
     * @return String name of the user
     */
    public String getScreen() {
        return this.screenName;
    }
    
    /**
     * Getter method for obtaining the number of tweets that a Twitter user contributed to.
     * 
     * @return int total number of tweets
     */
    public int getTweetCount() {
        return this.tweetCount;
    }
    
    /**
     * Getter method for obtaining the number of stories that a Twitter user contributed to.
     * 
     * @return int total number of stories
     */
    public int getStoryCount() {
        return this.storyCount;
    }
}
