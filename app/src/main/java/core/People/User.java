package core.People;

/**
 * Created by loucas on 25/01/2015.
 * This class is a singleton corresponding to the user of the app who in turn is also a participant of the conference
 */
public class User extends Person {
    private static User instance = null;
    private static String password;
    private static String authToken;
    private User(String title, String name, String surname, String university, String department, String email, boolean isSpeaker,String qualifiction) {
        super(title, name, surname, university, department, email, isSpeaker,qualifiction);
    }


    public static void instantiate(String title, String name, String surname, String university,
                                   String department, String email, boolean isSpeaker,String qualification){
        instance = new User(title,name,surname,university,department,email,isSpeaker,qualification);
    }

    public static User getInstance(){

        return instance;
    }

    public static String getAuthToken(){
        return authToken;
    }

    public static void setAuthToken(String token){
        authToken = token;
    }

}
