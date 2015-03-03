package core.People;

/**
 * Created by loucas on 25/01/2015.
 * This class will be used to define participants of the conference excluding the user
 */
public class OtherParticipant extends Person {

    //to check if this person instance is in user's favourite list of people
    private boolean isFavourite;
    //the participant's id which indexes  it in the server side database
    private String userId;
    /*boolean var to check if a person is correlated to the user*/
    private boolean isCorrelated;

    /*instantiate an other participant object */
    public OtherParticipant(String userId ,String title, String name, String surname, String university, String department, String email, boolean isSpeaker,boolean isFavourite,String qualification) {
        super(title, name, surname, university, department, email, isSpeaker,qualification);
        this.isFavourite = isFavourite;
        this.userId = userId;

    }

    //return if this participant instance is in user's favourites or not
    public boolean isFavourite(){
        return this.isFavourite;
    }

    //change favourite status for this instance
    public void changeFavouriteStatus(){
        this.isFavourite = !isFavourite;
    }

    //return the participant's user id
    public String getUserId(){
        return this.userId;
    }

    /*set true or false if the person is correlated or not to the user*/
    public void setCorrelated(boolean correlated){
        this.isCorrelated=correlated;
    }
    /*return true if this person is correlated to the user*/
    public boolean isCorrelated(){
        return this.isCorrelated;
    }

}
