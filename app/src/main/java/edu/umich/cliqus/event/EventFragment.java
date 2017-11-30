package edu.umich.cliqus.event;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.umich.cliqus.R;
import edu.umich.cliqus.profile.Profile;
import edu.umich.cliqus.profile.ProfileFragment;

public class EventFragment extends Fragment implements RecyclerAdapter.RecyclerViewClickListener {

    private TextView textView;
    private CardView cardView;
    private ImageView imageView;
    private String assessment;
    private String details;

    private RecyclerView rv;
    private RecyclerAdapter adapter;
    ItemTouchHelper touchHelper;

    private List<Event> events = new ArrayList<>();
    private List<String> preferences = new ArrayList<>();
    private List<String> preferenceUID = new ArrayList<>();
    private Profile profile = null;

    private static final String ARG_SECTION_NUMBER = "position";
    private static final String TAG = "cliqus";

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static EventFragment newInstance(int position) {
        EventFragment fragment = new EventFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, position);
        fragment.setArguments(args);
        return fragment;
    }

    public EventFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

            Bundle bundle = this.getArguments();
            profile = (Profile) bundle.getSerializable("profiledata");
            if (profile == null) {
                Log.w("Cliqus", "null for days");
            } else {
                Log.w("Cliqus", "no null for days");
            }

            touchHelper = new ItemTouchHelper(new ItemTouchHelper
                    .SimpleCallback(0,
                        ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                @Override
                public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                                      RecyclerView.ViewHolder target) {
                    return false;
                }

                @Override
                public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                    events.remove(viewHolder.getAdapterPosition());
                    Log.w(TAG, "Swiped event " +viewHolder.getAdapterPosition());
                    adapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                }
            });
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_event, container, false);

        Context context = getActivity();
        cardView = (CardView) rootView.findViewById(R.id.event_card_view);
        imageView = (ImageView) rootView.findViewById(R.id.event_card_image);

        rv = (RecyclerView) rootView.findViewById(R.id.cardList);

        getUserPreferences();
        populatePreferenceEvents();
        final LinearLayoutManager llm = new LinearLayoutManager(context);
        rv.setLayoutManager(llm);

        return rootView;
    }

    public void populatePreferenceEvents() {
        for(int i = 0; i < preferences.size(); i++) {
            DatabaseReference ref = FirebaseDatabase.getInstance()
                    .getReference()
                    .child("tags")
                    .child(preferences.get(i));

            Log.w(TAG, "pulling preference event data for " + preferences.get(i));


            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for(DataSnapshot ds: dataSnapshot.getChildren()) {
                        if (ds.getValue(String.class) != null) {
                            String uid = ds.getValue(String.class);
                            if (!preferenceUID.contains(uid)) {
                                preferenceUID.add(uid);
                                populateEvent(uid);
                                Log.w(TAG, "grabbed string for uid " + ds.getValue(String.class));
                            } else
                                Log.w(TAG, "duplicate uid " + ds.getValue(String.class) +
                                        "ignoring");
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.w(TAG,"The pref event read failed: " + databaseError.getMessage());
                }
            });
        }

    }

    public void populateEvent(final String uid) {
        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference()
                .child("events")
                .child(uid);

        Log.w(TAG, "Populating uid: " + uid);


        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.w(TAG, "pulling event " + dataSnapshot.getValue());


                Event singEvent = dataSnapshot.getValue(Event.class);

                if(singEvent != null)
                    fetchImage(singEvent);
                else
                    Log.w(TAG, "Event with uid " + uid + " was null");

                setRecyclerAdapter();
            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {
                Log.w(TAG, "The read failed: " + firebaseError.getMessage());
            }
        });
    }

    public void getUserPreferences() {
        String eventTags = profile.getDesiredEventTags();
        String split[] = eventTags.split(",");
        for (int i = 0; i < split.length; i++)
            preferences.add(split[i]);
        Log.w(TAG, "preferences set.");
    }

    void setRecyclerAdapter() {
        adapter = new RecyclerAdapter(events, this);
        rv.setAdapter(adapter);
        rv.setHasFixedSize(true);
        touchHelper.attachToRecyclerView(rv);
    }

    void fetchImage(final Event event) {
        StorageReference riversRef = FirebaseStorage.getInstance().getReference()
                .child("event_image")
                .child("thumb_" + event.getEventUID() + ".jpg");
        Log.w(TAG, "Attempting to fetch thumb_" + event.getEventUID());
        try {
            final File localFile = File
                    .createTempFile("thumb_", "jpg");
            riversRef.getFile(localFile)
                    .addOnSuccessListener(
                            new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                    Bitmap image = BitmapFactory.decodeFile(
                                            localFile.getAbsolutePath());
                                    event.setImageEvent(image);
                                    final int currEvent = events.size() - 1;
                                    events.add(event);
                                    //adapter.notifyItemChanged(events.size() - 1);
                                    Log.w(TAG, "imageUID " + event.getEventUID() +
                                            " is ready, notifying adapter");
                                    adapter.notifyDataSetChanged();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Log.w("cliqus", "event image" +
                            event.getEventUID() + " download failed");
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onEventClick(int position) {
    }

    @SuppressLint("ResourceType")
    @Override
    public void recyclerViewListClicked(View v, int position) {

        Fragment fragment = new SingleEventFragment();

        Bundle bundle = new Bundle();
        bundle.putSerializable("selected_event", events.get(position));
        fragment.setArguments(bundle);

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
        //        .setCustomAnimations(R.animator.slide_in_right, R.animator.slide_in_home, 0, 0)
                .replace(R.id.frame_container, fragment).commit();
    }

}