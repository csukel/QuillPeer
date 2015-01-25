package core.People;

/**
 * Created by loucas on 25/01/2015.
 * This class will be used to define participants of the conference excluding the user
 */
public class OtherParticipant extends Person {

    //to check if this person instance is in user's favourite list of people
    private boolean isFavourite;

    public OtherParticipant(String title, String name, String surname, String university, String department, String email, boolean isSpeaker,boolean isFavourite) {
        super(title, name, surname, university, department, email, isSpeaker);
        this.isFavourite = isFavourite;
    }

    //return if this participant instance is in user's favourites or not
    public boolean isFavourite(){
        return this.isFavourite;
    }

    //change favourite status for this instance
    public void changeFavouriteStatus(){
        this.isFavourite = !isFavourite;
    }
}
