package com.quantum.mobile.login;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NetworkRequest {

    public static OkHttpClient client = new OkHttpClient();
    public static String LOCAL_HOST = "http://nickwhitedev.pythonanywhere.com/";
    public static String CREAT_ACCOUNT_URL = LOCAL_HOST + "create_account";
    MainActivity mainActivity;

    public NetworkRequest(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    public void request(String URL, String[] entries){
        if (isNetworkAvailable()) {
            Log.d("SUBMITSAVE", "Downloading statdata");
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("user_name", entries[0])
                    .addFormDataPart("user_email", entries[1])
                    .addFormDataPart("user_password", entries[2])
                    .build();
            Request request = new Request.Builder()
                    .url(URL)
                    .post(requestBody)
                    .build();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Log.d("tag", "Server Failure");
                    e.printStackTrace();
                    Log.d("SUBMITSAVE", "save data to server error");
                    MainHandler.sendMessage(MainHandler.createMessageData("Request", "Server Connect Error"));
                }
                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String body = response.body().string();
                    Log.d("SUBMITSAVE", "save data to server response = " + body);
                    MainHandler.sendMessage(MainHandler.createMessageData("Request", body));
                }
            });
        }
    }


    public boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) mainActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            isAvailable = true;
        }
        return isAvailable;
    }

}
