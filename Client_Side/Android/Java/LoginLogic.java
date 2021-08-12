package com.quantum.mobile.login;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

public class LoginLogic {
    public static final int USERNAME = 0;
    public static final int EMAIL = 1;
    public static final int PASSWORD = 2;
    public static final String SERVER_CONNECT_ERROR = "Server Connect Error";
    public static final String SERVER_ERROR = "Server Error";
    public static final String USER_ALREADY_EXISTS = "User Already Exists";
    public static final String USER_CREATED = "User Created";
    public static final String INTERNAL_SERVER_ERROR = "Internal Server Error";
    MainActivity mainActivity;
    KeyboardHandler keyboardHandler;
    NetworkRequest networkRequest;
    TextView titleView;
    Button submitButton;
    ConstraintLayout layout;
    TextView submitErrorView;
    TextView[] errorViews = new TextView[3];
    EditText[] editTexts = new EditText[3];
    boolean[] validEntries = new boolean[3];
    String[] entries = new String[3];


    public LoginLogic(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        networkRequest = new NetworkRequest(mainActivity);
        keyboardHandler = new KeyboardHandler(mainActivity);
        setViews();
    }

    public void setViews() {
        layout = mainActivity.findViewById(R.id.main_layout);
        editTexts[0] = mainActivity.findViewById(R.id.username_edit_text);
        editTexts[0].addTextChangedListener(editTextListener(USERNAME));
        editTexts[1] = mainActivity.findViewById(R.id.email_edit_text);
        editTexts[1].addTextChangedListener(editTextListener(EMAIL));
        editTexts[2] = mainActivity.findViewById(R.id.password_edit_text);
        editTexts[2].addTextChangedListener(editTextListener(PASSWORD));
        errorViews[0] = mainActivity.findViewById(R.id.username_error_view);
        errorViews[1] = mainActivity.findViewById(R.id.email_error_view);
        errorViews[2] = mainActivity.findViewById(R.id.password_error_view);
        titleView = mainActivity.findViewById(R.id.title_view);
        submitButton = mainActivity.findViewById(R.id.submit_button);
        submitButton.setOnClickListener(submitButtonClick());
        submitErrorView = mainActivity.findViewById(R.id.submit_error);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitErrorView.setVisibility(View.GONE);
                keyboardHandler.outsideKeyboardClickUtil(v);
            }
        });

    }

    public TextWatcher editTextListener(final int type) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void afterTextChanged(Editable s) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = String.valueOf(s);
                entries[type] = text;
                boolean valid = validateText(type, text);
                setValid(type, valid);
            }
        };
    }

    public void sendRequest() {
        networkRequest.request(NetworkRequest.CREAT_ACCOUNT_URL, entries);
    }

    public void showSubmitError(String text) {
        submitErrorView.setVisibility(View.VISIBLE);
        submitErrorView.setText(text);
    }

    public void networkResponse(String message) {
        Log.d("tag", "Network Response = " + message);
        if (message.equals(SERVER_CONNECT_ERROR)){
            showSubmitError(SERVER_CONNECT_ERROR);
        } else if (message.equals(SERVER_ERROR)) {
            showSubmitError(SERVER_ERROR);
        } else if (message.equals(USER_ALREADY_EXISTS)) {
            showSubmitError(USER_ALREADY_EXISTS);
        } else if (message.contains(INTERNAL_SERVER_ERROR)){
            showSubmitError(INTERNAL_SERVER_ERROR);
        } else if (message.equals(USER_CREATED)){
            Intent intent = new Intent(mainActivity, UserProfile.class);
            intent.putExtra("username", entries[USERNAME]);
            intent.putExtra("email", entries[EMAIL]);
            mainActivity.startActivity(intent);
        }
    }

    public void setValid(int type, boolean valid) {
        if (valid) {
            validEntries[type] = true;
            errorViews[type].setVisibility(View.GONE);
        } else {
            validEntries[type] = false;
        }
    }

    public boolean validateText(int type, String text) {
        if (type == USERNAME || type == PASSWORD) {
            return Regex.isUsernameOrPasswordValid(text);
        } else {
            // type == EMAIL
            return Regex.isEmailValid(text);
        }
    }

    public boolean showErrors() {
        boolean errors = false;
        for (int i=0; i<validEntries.length; i++) {
            if (!validEntries[i]) {
                errorViews[i].setText("Error");
                errors = true;
            }
        }
        return errors;
    }

    public View.OnClickListener submitButtonClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean errorsShown = showErrors();
                if (!errorsShown) {
                    // Check server for username existence
                    // If everythin checked out the accout is created
                    sendRequest();
                    // A message will then be delivered to Main Handler
                    // Main Handler has an instance of this class
                }
            }
        };
    }



}
