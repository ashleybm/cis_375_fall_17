package edu.umich.cliqus.profile;

import android.nfc.Tag;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;

/**
 * Created by somcom3x on 11/3/17.
 */

public class Profile implements Serializable{

    private String firstName;
    private String lastName;
    private String gender;
    private String dob;
    private String email;
    private String phone;
    private boolean coverPhotoSet;
    private boolean profilePhotoSet;
    private String desiredEventTags;

    public Profile() {
        this.firstName = null;
        this.lastName = null;
        this.gender = null;
        this.dob = null;
        this.email = null;
        this.phone = null;
        coverPhotoSet = false;
        profilePhotoSet = false;
        desiredEventTags = "";
    }

    public Profile(String firstName, String lastName, String gender, String dob, String email,
                   String phone, String desiredEventTags) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.dob = dob;
        this.email = email;
        this.phone = phone;
        this.desiredEventTags = desiredEventTags;
    }


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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isCoverPhotoSet() {
        return coverPhotoSet;
    }

    public void setCoverPhotoSet(boolean coverPhotoSet) {
        this.coverPhotoSet = coverPhotoSet;
    }

    public boolean isProfilePhotoSet() {
        return profilePhotoSet;
    }

    public String getDesiredEventTags() { return desiredEventTags; }

    public void setProfilePhotoSet(boolean profilePhotoSet) {
        this.profilePhotoSet = profilePhotoSet;
    }


    public void addDesiredEvenasdtTag(String tag) {
        desiredEventTags += tag + ",";
    }

    public boolean checkData() {
        if(firstName.isEmpty()) {
            Log.w("cliqus", "First name is empty");
            return false;
        }
        else if(lastName.isEmpty()) {
            Log.w("cliqus", "Last name is empty");
            return false;
        }

        else if(gender.isEmpty())  {
            Log.w("cliqus", "Gender is empty");
            return false;
        }

        else if(dob.isEmpty()) {
            Log.w("cliqus", "DOB is empty");
            return false;
        }

        else if(email.isEmpty()) {
            Log.w("cliqus", "Email is empty");
            return false;
        }

        else if(phone.isEmpty()) {
            Log.w("cliqus", "phone # is empty");
            return false;
        }

        return true;
    }

}
