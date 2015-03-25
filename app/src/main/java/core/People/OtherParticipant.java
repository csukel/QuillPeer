package core.People;

/**
 * This class will be used to define participants of the conference excluding the user
 * Created on 25/01/2015.
 * @author Loucas Stylianou
 */
public class OtherParticipant extends Person {


    /**
     * To check if this person instance is in user's favourite list of people
     */
    private boolean isFavourite;
    /**
     * the participant's id which indexes  it in the server side database
     */
    private String userId;
    /**
     * boolean var to check if a person is correlated to the user
     */
    private boolean isCorrelated;

    /**
     * Instantiate an other participant object
     * @param userId The user id as it is stored in the server side database
     * @param title The prefix for example Mr , Dr . etc
     * @param name The first name
     * @param surname The last name
     * @param university The university for which the person works
     * @param department The department in which the person is registered
     * @param email Person's email address
     * @param isSpeaker Boolean variable, true if the person is a speaker in the conference and false otherwise
     * @param isFavourite Boolean variable to determine if this person is in favourites list of the user
     * @param qualification The academic qualification of this person for example Professor, Assistant, Associate e.t.c
     */
    public OtherParticipant(String userId ,String title, String name, String surname, String university, String department, String email, boolean isSpeaker,boolean isFavourite,String qualification) {
        super(title, name, surname, university, department, email, isSpeaker,qualification);
        this.isFavourite = isFavourite;
        this.userId = userId;

    }

    /**
     * return if this participant instance is in user's favourites or not
     * @return true if this person is favourite otherwise false
     */
    public boolean isFavourite(){
        return this.isFavourite;
    }

    /**
     * Change the favourite boolean state from true to false and in vice versa direction
     */
    public void changeFavouriteStatus(){
        this.isFavourite = !isFavourite;
    }

    /**
     * Retrieve this person's user id
     * @return userId
     */
    public String getUserId(){
        return this.userId;
    }

    /**
     * set true or false if the person is correlated or not to the user
     * @param correlated Boolean value which defines whether this person is correlated to the user or not
     */
    public void setCorrelated(boolean correlated){
        this.isCorrelated=correlated;
    }
    /**
     * Get the correlated boolean value state
     * @return True if the person is correlated to the user, false otherwise
     */
    public boolean isCorrelated(){
        return this.isCorrelated;
    }

}
