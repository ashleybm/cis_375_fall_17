package edu.umich.cliqus.NavBar;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import edu.umich.cliqus.R;
import edu.umich.cliqus.auth.LoginActivity;
import edu.umich.cliqus.event.EventFragment;
import edu.umich.cliqus.profile.Profile;
import edu.umich.cliqus.profile.ProfileFragment;
import edu.umich.cliqus.profile.RequestProfileDataActivity;

public class NavDrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
        private static final String TAG = "CliqUs";

    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    private String uid;
    private DatabaseReference userRef;

    private Profile profile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();


        mFirebaseDatabase = FirebaseDatabase.getInstance();
        userRef = mFirebaseDatabase.getReference();
        FirebaseUser user = mAuth.getCurrentUser();

        if(user == null) {
            Log.w(TAG, "No user found, sign in");
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        } else {

            uid = user.getUid();
            Log.w(TAG, "USER ID " + uid);


            userRef.child("users").child(uid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    profile = dataSnapshot.getValue(Profile.class);

                    Log.w(TAG, "Data pulled!");
                    if(profile == null) {
                        Intent intent = new Intent(NavDrawerActivity.this,
                                RequestProfileDataActivity.class);
                        startActivity(intent);
                    } else {
                        Log.w("CliqUs", profile.getFirstName());
                        Log.w("CliqUs", profile.getLastName());
                        Log.w("CliqUs", profile.getGender());
                        Log.w("CliqUs", profile.getEmail());
                        Log.w("CliqUs", profile.getDob());
                        Log.w("CliqUs", profile.getPhone());
                    }

                    Fragment fragment = new ProfileFragment();

                    Bundle bundle = new Bundle();
                    bundle.putSerializable("profiledata", profile);
                    fragment.setArguments(bundle);

                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.frame_container, fragment).commit();

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.w(TAG, "cancelled? " + databaseError.toString());
                }
            });
        }
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.nav_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment = null;

        if (id == R.id.nav_profile) {
            //Intent intent = new Intent(NavDrawerActivity.this, ProfileActivity.class);
           // startActivity(intent);
            fragment = new ProfileFragment();

            Bundle bundle = new Bundle();
            bundle.putSerializable("profiledata", profile);
            fragment.setArguments(bundle);

        } else if (id == R.id.nav_events) {
            fragment = new EventFragment();

            Bundle bundle = new Bundle();
            bundle.putSerializable("profiledata", profile);
            fragment.setArguments(bundle);

        } else if (id == R.id.nav_questionnaire) {

        } else if (id == R.id.nav_settings) {

        } else if (id == R.id.nav_submit_event) {

            try{
                Intent intent = new Intent (Intent.ACTION_VIEW , Uri.parse("mailto:" +
                        "support@cliq.us"));
                intent.putExtra(Intent.EXTRA_SUBJECT, "Feature Request From " + profile.getFirstName() +
                    " " + profile.getLastName());
                intent.putExtra(Intent.EXTRA_TEXT, "Date: \n" +
                        "Event Type (Birthday, Movie, etc): \n" +
                        "Link to Event: ");
                startActivity(intent);
            }catch(ActivityNotFoundException e){
                Log.w(TAG, "Activity Exception: "+ e );
            }

        } else if (id == R.id.nav_submit_bug) {

            try{
                Intent intent = new Intent (Intent.ACTION_VIEW , Uri.parse("mailto:" +
                        "support@cliq.us"));
                intent.putExtra(Intent.EXTRA_SUBJECT, "Bug Report From " + profile.getFirstName() +
                        " " + profile.getLastName());
                intent.putExtra(Intent.EXTRA_TEXT, "Date: \n" +
                        "Problem Experienced: \n" +
                        "Steps to Recreate: ");
                startActivity(intent);
            }catch(ActivityNotFoundException e){
                Log.w(TAG, "Activity Exception: "+ e );
            }

        } else if (id == R.id.nav_signout) {
            Intent intent = new Intent(this, LoginActivity.class);
            mAuth.signOut();
            startActivity(intent);
        }

        if(fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.frame_container, fragment).commit();
        }

        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_home);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
