package edu.umich.cliqus.event;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.util.*;

import java.io.Serializable;

import static android.content.ContentValues.TAG;

/**
 * Created by Christian Thompson on 11/28/2017.
 */

public class EventNotification implements Serializable {

    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();

    //FIXME
    if (user != null){

        String email = user.getEmail();
        String userID = user.getUid();

        //query the date for a specific events
        Query eventsDate = database.getReference("Events").child(
                                                 userID).equalTo("date");

        eventsDate.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapShot : dataSnapshot.getChildren()){
                    //TODO handle eventDate
                    //forEach events match the eventsUID
                    //with the users.eventsUID
                    //if match then user.sendEmailNotification
                    //a day before the event
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
