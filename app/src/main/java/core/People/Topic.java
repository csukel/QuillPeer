package core.People;

/**
 * Created by loucas on 05/03/2015.
 * This class represents a specific topic for the conference
 */
public class Topic {
    /*list of words in a string which represents the topic*/
    private String title;
    /*the weight of this topic*/
    private double weight;

    public Topic(String t,double w){
        this.title = t;
        this.weight = w;
    }
    /*get the title of the topic*/
    public String getTitle(){
        return this.title;
    }
    /*get the weight of the topic*/
    public double getWeight(){
        return this.weight;
    }

}
