package com.quantum.mobile.login;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.os.Message;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;

public class MainHandler {

    public static ArrayList<MessageData> MESSAGE_QUEUE = new ArrayList<>();
    ConstraintLayout layout;
    TextView timerView;
    LoginLogic loginLogic;


    public MainHandler(ConstraintLayout layout, LoginLogic loginLogic) {
        this.layout = layout;
        this.loginLogic = loginLogic;
        timerView = layout.findViewById(R.id.timer);

        // Listen for messages from socket thread
        MESSAGE_HANDLER();
    }

    public static MessageData createMessageData(String msgType, String message) {
        MessageData data = new MessageData();
        data.setMsgType(msgType);
        data.setMessage(message);
        return data;
    }

    public static void sendMessage(MessageData messageData) {
        // Add message to heap
        MESSAGE_QUEUE.add(messageData);
    }

    private void MESSAGE_HANDLER() {
        // Recursive MainThread Looper
        timer(100, new Runnable() {
            @Override
            public void run() {
                try {
                    // Exec first in queue, then remove from heap
                    MessageData data = MESSAGE_QUEUE.get(0);
                    MESSAGE_QUEUE.remove(0);
                    handleMsg(data);
                } catch (Exception e) {
                    // Queue is Empty
                }
                MESSAGE_HANDLER();
            }
        });
    }

    public void handleMsg(MessageData data) {
        // Set view text to message received
        String textMessage = data.getMessage();
        loginLogic.networkResponse(textMessage);
    }

    public void timer(int time, Runnable runnable){
        // Hacky timer OFF UI thread,
        // Works solid and easily
        // Where as sending android.os.message to ui thread can get lost
        // Arbitrarily change alpha to fully opaque
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(timerView, "alpha", 1);
        objectAnimator.setDuration(time);
        objectAnimator.start();
        objectAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                runnable.run();
            }
        });
    }


}
