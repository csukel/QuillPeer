package core.People;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

/**
 * This class depicts a person in a conference. This person can be a user or other participant
 * Created on 23/11/2014.
 * @author Loucas Stylianou
 */
public class Person {
    /**
     * person's qualification level
     */
    private String qualification;
    /**
     * Prefix for example Mr.,Mrs.,Dr.
     */
    private String title;
    /**
     * First name
     */
    private String name;
    /**
     * Last name
     */
    private String surname;
    /**
     * The university for which this person works
     */
    private String university;
    /**
     * The department for which a person works
     */
    private String department;
    /**
     * Boolean var to check if this person is a speaker for the conference
     */
    private boolean isSpeaker;
    /**
     * Email address
     */
    private String email;
    /**
     * The abstract which a person submits before going to a conference
     */
    private String paperAbstract;
    /**
     * The abstract's title
     */
    private String paperAbstractTitle;
    /**
     * The bitmap object which contains the profile picture of a person
     */
    private Bitmap profilePicture;
    /**
     * List of topic models
     */
    private List<Topic> topicList = new ArrayList<Topic>();

    /**
     * The constructor which is used to instantiate a person object
     * @param title The prefix
     * @param name The first name
     * @param surname The last name
     * @param university The university for which this person works
     * @param department The department for which this person is registered
     * @param email The email address of this person
     * @param isSpeaker Speaker state defines whether this person is a speaker to a conference or not
     * @param qualification The qualifiaction of this person for example Professor, Assistant, Associate, PostDoc,PhD student e.t.c
     */
    public Person (String title,String name,String surname,String university,String department,String email,boolean isSpeaker,String qualification){
        this.title = title;
        this.name = name;
        this.surname = surname;
        this.university = university;
        this.department = department;
        this.isSpeaker = isSpeaker;
        this.email = email;
        this.qualification = qualification;
        this.paperAbstract = null;
        this.paperAbstractTitle = null;
        this.profilePicture=null;
    }

    /**
     * Retrieve the prefix of a person
     * @return title
     */
    public String getTitle(){
        return this.title;
    }

    /**
     * Retrieve the first name
     * @return name
     */
    public String getName(){
        return this.name;
    }

    /**
     * Retrieve the last name of the person
     * @return surname
     */
    public String getSurname(){
        return this.surname;
    }

    /**
     * Retrieve the university for which this person works
     * @return university
     */
    public String getUniversity(){
        return this.university;
    }

    /**
     * Retrieve the department in which this person is registered
     * @return department
     */
    public String getDepartment(){
        return this.department;
    }

    /**
     * The academic qualification of this person
     * @return qualification e.g Professor,Assistant, Associate, PostDoc,PhD student,Other
     */
    public String getQualification() {return this.qualification;}

    /**
     * Retrieve Speaker state
     * @return isSpeaker True if the person is a speaker for the particular conference, otherwise false
     */
    public boolean isSpeaker(){
        return this.isSpeaker;
    }

    /**
     * Retrieve the email address of a person
     * @return email
     */
    public String getEmail(){
        return this.email;
    }

    /**
     * Retrieve the abstract that a person has submitted for the conference
     * @return paperAbstract
     */
    public String getPaperAbstract(){return this.paperAbstract;}

    /**
     * Set the paper abstract content
     * @param abstr The textual content of the abstract
     */
    public void setPaperAbstract(String abstr){this.paperAbstract = abstr;}

    /**
     * Set the abstract's title
     * @param title Abstract's title
     */
    public void setPaperAbstractTitle(String title){
        this.paperAbstractTitle = title;
    }

    /**
     * Retrieve the abstract's title
     * @return paperAbstractTitle
     */
    public String getPaperAbstractTitle(){
        return this.paperAbstractTitle;
    }

    /**
     * Save the profile picture in a bitmap object
     * @param picture A bitmap object that contains the profile picture of a person
     */
    public void setProfilePicture(Bitmap picture){
        this.profilePicture = picture;
    }

    /**
     * Retrieve the bitmap object where the profile picture is stored
     * @return profilePicture
     */
    public Bitmap getProfilePicture(){
        return this.profilePicture;
    }

    /**
     * Retrieve the list with the topics related to this person submitted abstract
     * @return topicsList List of topic models
     */
    public List<Topic> getTopicList(){
        return this.topicList;
    }

    /**
     * Save a list containing topic models related to a person's submitted abstract
     * @param list
     */
    public void setTopicList(List<Topic> list){
        this.topicList = list;
    }

    /**
     * Add a topic to the topic list for a person
     * @param topic A set of words that represent a topic
     */
    public void addTopicToList(Topic topic){
        this.topicList.add(topic);
    }

}
