package core.People;

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

    public Person (String title,String name,String surname,String university,String department,String email,boolean isSpeaker){
        this.title = title;
        this.name = name;
        this.surname = surname;
        this.university = university;
        this.department = department;
        this.isSpeaker = isSpeaker;
        this.email = email;
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

}
