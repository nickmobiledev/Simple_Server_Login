package com.quantum.mobile.login;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Regex {
    public static int USERNAME_MAX_LENGTH = 255;
    public static int USERNAME_MIN_LENGTH = 8;
    public static String regexEmail = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
    public static Pattern patternEmail = Pattern.compile(regexEmail);
    public static String regexUsernamePassword = "^[A-Za-z0-9+_.-]{8,255}$";
    public static Pattern patternUsernamePassword = Pattern.compile(regexUsernamePassword);

    public static boolean isUsernameOrPasswordValid(String text){
        Matcher matcher = patternUsernamePassword.matcher(text);
        return matcher.matches();
    }

    public static boolean isEmailValid(String email){
        Matcher matcher = patternEmail.matcher(email);
        return matcher.matches();
    }


}
