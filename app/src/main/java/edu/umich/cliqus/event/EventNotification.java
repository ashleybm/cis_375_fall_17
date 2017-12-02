package edu.umich.cliqus.event;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.*;

import java.io.Serializable;

import static android.content.ContentValues.TAG;
import static com.google.firebase.database.FirebaseDatabase.getInstance;

/**
 * Created by Christian Thompson on 11/28/2017.
 */

public class EventNotification implements Serializable {

    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseDatabase database = getInstance();

    //FIXME
    public void FirebaseEventDate() {

        if (user != null) {

            String email = user.getEmail();
            final String userID = user.getUid();
            final DatabaseReference userRef = FirebaseDatabase
                                        .getInstance().getReference("users");

            //query the date for a specific events
            DatabaseReference dbRef = FirebaseDatabase
                                      .getInstance().getReference("events");

            final ValueEventListener eventDateListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Event events = dataSnapshot.getValue(Event.class);

                    DataSnapshot dateSnapshot = dataSnapshot
                                                .child("eventUID").child("date");
                    Iterable<DataSnapshot> eventChildren = dateSnapshot.getChildren();
                    //ArrayList<Event> eventArrayList = new ArrayList<>();
                    for(DataSnapshot eventsDate : eventChildren){
                        Event evt = eventsDate.getValue(Event.class);

                        if(evt.equals(userRef.child(userID)
                                             .child("attnEvtID").getRoot())){
                            //FIXME
                            //user.sendEmailVerification()
                        }
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };




            Query eventsDate = database.getReference("Events").child(
                    "eventUID").child("date");

            eventsDate.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snap : dataSnapshot.getChildren()) {
                        //TODO handle eventDate
                        //forEach events match the eventsUID
                        //with the users.eventsUID
                        //if match then user.sendEmailNotification
                        //a day before the event
                        //if(eventID.equals(snap.child(userID).child("attnID")));
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                    // Getting Post failed, log a message
                    Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                }
            });

        }
    }
}
