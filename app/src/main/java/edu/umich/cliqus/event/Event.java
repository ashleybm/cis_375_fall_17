package edu.umich.cliqus.event;

import android.graphics.Bitmap;

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
    private String eventUID;
    private Bitmap imageEvent;
    private boolean imageReady = false;

    public Event() {
        date = null;
        time = null;
        address = null;
        eventUID = null;
        imageEvent = null;
    }

    public Event(String date, String time, String address, String description, String eventUID) {
        this.date = date;
        this.time = time;
        this.address = address;
        this.description = description;
        this.eventUID = eventUID;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEventUID() {
        return eventUID;
    }

    public void setEventUID(String eventUID) {
        this.eventUID = eventUID;
    }

    public Bitmap getImageEvent() {
        return imageEvent;
    }

    public void setImageEvent(Bitmap imageEvent) {
        this.imageEvent = imageEvent;
        imageReady = true;
    }

    public boolean isImageReady() {
        return imageReady;
    }

    public void setImageReady(boolean imageReady) {
        this.imageReady = imageReady;
    }



}