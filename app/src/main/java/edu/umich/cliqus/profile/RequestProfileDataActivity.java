package edu.umich.cliqus.profile;

import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.renderscript.BaseObj;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;
import edu.umich.cliqus.DatePickerFragment;
import edu.umich.cliqus.NavBar.NavDrawerActivity;
import edu.umich.cliqus.R;
import edu.umich.cliqus.UserInformation;

public class RequestProfileDataActivity extends AppCompatActivity {
    private static final String TAG = "CliqUs";

    @BindView(R.id.input_first_name)
    EditText _firstNameText;
    @BindView(R.id.input_last_name) EditText _lastNameText;
    @BindView(R.id.input_gender) EditText _genderText;
    @BindView(R.id.btn_save_profile) Button _saveDataButton;
    @BindView(R.id.dob_button) Button _dateOfBirthButton;
    @BindView(R.id.input_phone) EditText _phoneNumber;

    private FirebaseAuth mAuth;

    private String firstName;
    private String lastName;
    private String gender;
    private String dob;
    private String phone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_profile_data);
        ButterKnife.bind(this);

        _dateOfBirthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getFragmentManager(), "DatePicker");
            }
        });
        _saveDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createProfile();
            }
        });

    }


    public boolean validate() {
        boolean valid = true;

        firstName = _firstNameText.getText().toString();
        lastName = _lastNameText.getText().toString();
        gender = _genderText.getText().toString();
        dob = _dateOfBirthButton.getText().toString();
        phone = _phoneNumber.getText().toString();

        if (firstName.isEmpty() || firstName.length() < 3) {
            _firstNameText.setError("at least 3 characters");
            valid = false;
        } else {
            _firstNameText.setError(null);
        }

        if (lastName.isEmpty() || lastName.length() < 3) {
            _lastNameText.setError("at least 3 characters");
            valid = false;
        } else {
            _lastNameText.setError(null);
        }

        if (gender.isEmpty()) {
            _genderText.setError("enter your gender");
            valid = false;
        } else {
            _genderText.setError(null);
        }

        if (dob.isEmpty()) {
            _dateOfBirthButton.setError("enter your gender");
            valid = false;
        } else {
            _dateOfBirthButton.setError(null);
        }

        if(phone.isEmpty()) {
            _phoneNumber.setError("enter your phone number");
            valid = false;
        }
        else {
            _phoneNumber.setError(null);
        }
        return valid;
    }

    public void createProfile() {
        mAuth = FirebaseAuth.getInstance();

        if(validate()) {
            FirebaseUser user = mAuth.getCurrentUser();
            FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference myRef;
            FirebaseAuth.AuthStateListener mAuthListener;
            myRef = mFirebaseDatabase.getReference();

            Profile profile = new Profile(firstName, lastName, gender, dob,
                    user.getEmail(), phone, "");

            if(user != null) {
                String userID = user.getUid();

                Log.w("CliqUs", firstName);
                Log.w("CliqUs", lastName);

                myRef.child("users").child(userID).setValue(profile);
                this.finish();
            } else
                Log.w(TAG, "Contact support");

            Intent intent = new Intent(RequestProfileDataActivity.this, NavDrawerActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("profile", profile);
            startActivity(intent, bundle);
            finish();
        }
    }
}
