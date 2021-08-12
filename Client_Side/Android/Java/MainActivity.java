package com.quantum.mobile.login;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LoginLogic loginLogic = new LoginLogic(this);
        new MainHandler(findViewById(R.id.main_layout), loginLogic);
    }

}