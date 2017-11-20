package edu.umich.cliqus.event;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import edu.umich.cliqus.NavBar.NavDrawerActivity;
import edu.umich.cliqus.R;
import edu.umich.cliqus.profile.Profile;

public class EventFragment extends Fragment {

    private TextView textView;
    private CardView cardView;
    private String assessment;
    private String details;
    private int numOfRefreshes = 0;

    private RecyclerView rv;
    private RecyclerAdapter adapter;
    ItemTouchHelper touchHelper;

    private List<Event> events = new ArrayList<>();
    private List<String> preferences = new ArrayList<>();
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
                getUserPreferences();
            }

            touchHelper = new ItemTouchHelper(new ItemTouchHelper
                    .SimpleCallback(0,
                        ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                @Override
                public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
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
        rv = (RecyclerView) rootView.findViewById(R.id.cardList);

        populateEvents();
        checkIfImagesAreLoaded();
        final LinearLayoutManager llm = new LinearLayoutManager(context);
        rv.setLayoutManager(llm);

        return rootView;
    }

    public void populateEvents() {
        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference()
                .child("events");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.w(TAG, dataSnapshot.getValue().toString());

                for(int i = 0; i < preferences.size(); i++) {
                    Log.w(TAG, "Fetching tag " + preferences.get(i));
                    DataSnapshot ds = dataSnapshot
                            .child(preferences.get(i));
                    //grab everything belonging to tag

                    for (DataSnapshot snapshot : ds.getChildren()) {
                        events.add(snapshot.getValue(Event.class));
                        events.get(events.size() - 1).fetchImage();


                        Log.w(TAG, snapshot.getValue().toString());
                    }
                }
                setRecyclerAdapter();
            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {
                Log.w(TAG,"The read failed: " + firebaseError.getMessage());
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
        adapter = new RecyclerAdapter(events);
        rv.setAdapter(adapter);
        touchHelper.attachToRecyclerView(rv);
    }

    void checkIfImagesAreLoaded() {
        boolean loaded = true;

        for(int i = 0; i < events.size(); i++) {
            if(events.get(i).isImageReady() != true);
                loaded = false;
        }

        if(loaded) {
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //Do something after 100ms
                    checkIfImagesAreLoaded();
                    Log.w(TAG, "LOOPing until done");
                }
            }, 1000);
        } else {
            if(adapter == null)
                setRecyclerAdapter();
            else
                adapter.notifyDataSetChanged();
            Log.w(TAG, "displaying data");
        }
    }
}