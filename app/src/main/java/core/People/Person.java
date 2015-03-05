package core.People;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by loucas on 23/11/2014.
 */
public class Person {
    //person's qualification level
    private String qualification;
    //prefix
    private String title;
    //first name
    private String name;
    //last name
    private String surname;
    //the university
    private String university;
    //the department for which a person works
    private String department;
    //check if this person is a speaker
    private boolean isSpeaker;
    //personal email
    private String email;
    //the abstract of this person
    private String paperAbstract;
    //the abstract title
    private String paperAbstractTitle;
    //profile picture
    private Bitmap profilePicture;
    //list of topics
    private List<Topic> topicList = new ArrayList<Topic>();


    //person class constructor
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

    public String getTitle(){
        return this.title;
    }

    public String getName(){
        return this.name;
    }

    public String getSurname(){
        return this.surname;
    }

    public String getUniversity(){
        return this.university;
    }

    public String getDepartment(){
        return this.department;
    }

    public String getQualification() {return this.qualification;}

    public boolean isSpeaker(){
        return this.isSpeaker;
    }

    public String getEmail(){
        return this.email;
    }

    public String getPaperAbstract(){return this.paperAbstract;}

    public void setPaperAbstract(String abstr){this.paperAbstract = abstr;}

    public void setPaperAbstractTitle(String title){
        this.paperAbstractTitle = title;
    }

    public String getPaperAbstractTitle(){
        return this.paperAbstractTitle;
    }
    public void setProfilePicture(Bitmap picture){
        this.profilePicture = picture;
    }

    public Bitmap getProfilePicture(){
        return this.profilePicture;
    }

    /*get the list with the topics related to this person*/
    public List<Topic> getTopicList(){
        return this.topicList;
    }

    /*save a topic list corresponding to this person*/
    public void setTopicList(List<Topic> list){
        this.topicList = list;
    }
    /*add a topic to the topic list for this person*/
    public void addTopicToList(Topic topic){
        this.topicList.add(topic);
    }

}
