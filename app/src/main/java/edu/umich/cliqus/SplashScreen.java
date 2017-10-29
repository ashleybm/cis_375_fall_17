package edu.umich.cliqus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import edu.umich.cliqus.auth.LoginActivity;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}