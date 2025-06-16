package com.example.firstaidapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private static final String PREF_NAME = "user_session";

    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USER_NAME = "user_name";
    private static final String KEY_USER_EMAIL = "user_email";
    private static final String KEY_USER_PHONE = "user_phone";
    private static final String KEY_USER_TYPE = "user_type";
    private static final String KEY_PROFILE_IMAGE = "profile_image";

    private final SharedPreferences prefs;
    private final SharedPreferences.Editor editor;

    public SessionManager(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    // Save the full user session
    public void saveUserSession(int userId, String name, String email, String phone, String type, String imageUri) {
        editor.putInt(KEY_USER_ID, userId);
        editor.putString(KEY_USER_NAME, name);
        editor.putString(KEY_USER_EMAIL, email);
        editor.putString(KEY_USER_PHONE, phone);
        editor.putString(KEY_USER_TYPE, type);
        editor.putString(KEY_PROFILE_IMAGE, imageUri);
        editor.apply();
    }

    // Getters
    public int getUserId() {
        return prefs.getInt(KEY_USER_ID, -1);
    }

    public String getUserName() {
        return prefs.getString(KEY_USER_NAME, "");
    }

    public String getUserEmail() {
        return prefs.getString(KEY_USER_EMAIL, "");
    }

    public String getUserPhone() {
        return prefs.getString(KEY_USER_PHONE, "");
    }

    public String getUserType() {
        return prefs.getString(KEY_USER_TYPE, "general");
    }

    public String getProfileImage() {
        return prefs.getString(KEY_PROFILE_IMAGE, null);
    }

    // Individual updaters (optional, but useful)
    public void updateProfileImage(String imageUri) {
        editor.putString(KEY_PROFILE_IMAGE, imageUri);
        editor.apply();
    }

    public void updateUserType(String type) {
        editor.putString(KEY_USER_TYPE, type);
        editor.apply();
    }

    public void updateUserName(String name) {
        editor.putString(KEY_USER_NAME, name);
        editor.apply();
    }

    public void updateUserEmail(String email) {
        editor.putString(KEY_USER_EMAIL, email);
        editor.apply();
    }

    public void updateUserPhone(String phone) {
        editor.putString(KEY_USER_PHONE, phone);
        editor.apply();
    }

    // Clear session on logout
    public void clearSession() {
        editor.clear();
        editor.apply();
    }
}
