package com.quantum.mobile.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class UserProfile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        TextView usernameView = findViewById(R.id.username);
        TextView emailView = findViewById(R.id.email);
        Intent intent = getIntent();
        usernameView.setText(intent.getStringExtra("username"));
        emailView.setText(intent.getStringExtra("email"));
    }

}