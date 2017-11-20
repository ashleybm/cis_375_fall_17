package edu.umich.cliqus.event;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

/**
 * Created by somcom3x on 11/18/17.
 */

public class Event implements Serializable {
    private final String TAG = "cliqus";
    private String date;
    private String time;
    private String address;
    private String description;
    private String imageUID;
    private Bitmap imageEvent;
    private boolean imageReady = false;

    public Event() {
        date = null;
        time = null;
        address = null;
        imageUID = null;
        imageEvent = null;
    }

    public Event(String date, String time, String address, String imageUID) {
        this.date = date;
        this.time = time;
        this.address = address;
        this.imageUID = imageUID;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() { return description;}

    public void setDescription(String description) { this.description = description; }

    public String getImageUID() { return imageUID; }

    public void setImageID(ImageView image) { this.imageUID = imageUID; }

    public Bitmap getImageEvent() { return imageEvent; }

    public boolean isImageReady() { return imageReady; }

    public void setImageEvent(Bitmap imageEvent) {
        this.imageEvent = imageEvent;
    }
}