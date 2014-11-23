package core;

/**
 * Created by loucas on 23/11/2014.
 */
public class Person {
    private String title;
    private String name;
    private String surname;
    private String university;
    private String department;

    public Person (String title,String name,String surname,String university,String department){
        this.title = title;
        this.name = name;
        this.surname = surname;
        this.university = university;
        this.department = department;
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

}
