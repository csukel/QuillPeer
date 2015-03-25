package core.People;

/**
 * This class is a singleton corresponding to the user of the app who in turn is also a participant of the conference
 * Created on 25/01/2015.
 * @author Loucas Stylianou
 */
public class User extends Person {
    /**
     * The signle instance of this class
     */
    private static User instance = null;

    /**
     * The custom User class constructor which is used to instantiate the single instance user of the app
     * @param title The prefix
     * @param name The fist name of the user
     * @param surname The last name of the user
     * @param university The university for which the user works
     * @param department The department in which the user is registered
     * @param email The email address of the user
     * @param isSpeaker Boolean var which determines whether the user is a speaker to the conference or not
     * @param qualifiction The user's academic qualification for example Professor, Assistant, Associate e.t.c
     */
    private User(String title, String name, String surname, String university, String department, String email, boolean isSpeaker,String qualifiction) {
        super(title, name, surname, university, department, email, isSpeaker,qualifiction);
    }


    /**
     * This method is used to instantiate the User object. It uses the singleton design pattern in order to adapt to the needs of this
     * application since one user will make use of it
     * @param title The prefix
     * @param name The first name of the user
     * @param surname The last name of the user
     * @param university The university for which the user works
     * @param department The department in which the user is registered
     * @param email The email address of the user
     * @param isSpeaker Boolean var which determines whether the user is a speaker to the conference or not
     * @param qualification The user's academic qualification for example Professor, Assistant, Associate e.t.c
     */
    public static void instantiate(String title, String name, String surname, String university,
                                   String department, String email, boolean isSpeaker,String qualification){
        instance = new User(title,name,surname,university,department,email,isSpeaker,qualification);
    }

    /**
     * Retrieve the single instance of the User class
     * @return instance
     */
    public static User getInstance(){

        return instance;
    }


}
