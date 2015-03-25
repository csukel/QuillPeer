package core.People;

/**
 * This class represents a specific topic for the conference
 * Created on 05/03/2015.
 * @author Loucas Stylianou
 */
public class Topic {

    /**
     * Set of words in a string which represents the topic
     */
    private String title;

    /**
     * The weight of this topic
     */
    private double weight;

    /**
     * The custom Topic class's constructor
     * @param t The set of words that represent the topic
     * @param w The weight(scoring) of the topic
     */
    public Topic(String t,double w){
        t = t.replaceAll(" ",", ");
        this.title = t;
        this.weight = w;
    }

    /**
     * Get the title of the topic (set of words that represent the topic)
     * @return title
     */
    public String getTitle(){
        return this.title;
    }

    /**
     * Get the weight of the topic
     * @return weight
     */
    public double getWeight(){
        return this.weight;
    }

}
