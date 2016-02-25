package edu.islam01sjsu.md.allmythings;

/**
 * Created by MD on 2/18/2016.
 */

public class User {
    private String firstName;
    private String lastName;
    private String userName;
    private String emailAddress;
    private String passWord;

//    public User(String firstName, String lastName, String userName, String emailAddress, String passWord){
//        this.firstName = firstName;
//        this.lastName = lastName;
//        this.userName = userName;
//        this.emailAddress = emailAddress;
//        this.passWord = passWord;
//    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }
}
