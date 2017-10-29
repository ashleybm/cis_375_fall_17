package edu.umich.cliqus;

/**
 * Created by somcom3x on 10/28/17.
 */

public class UserInformation {
    private String firstName;
    private String lastName;
    private String age;
    private String gender;
    private String address;
    private String phone;

    public UserInformation() {
        //do nothing
    }
    public UserInformation(String firstName, String lastName, String age,
                           String gender, String address, String phone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.gender = gender;
        this.address = address;
        this.phone = phone;
    }
}