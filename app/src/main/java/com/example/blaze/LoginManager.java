package com.example.blaze;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class LoginManager {
    private static final String PREF_LAST_LOGIN_ACTIVITY = "last_login_activity";
    private static final int ACTIVITY_LOGIN_1 = 1;
    private static final int ACTIVITY_LOGIN_2 = 2;
    private static final int ACTIVITY_LOGIN_3 = 3;
    private static final int ACTIVITY_LOGIN_4 = 4;

    private SharedPreferences sharedPreferences;

    public LoginManager(Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void setLastLoginActivity(int activityIndex) {
        sharedPreferences.edit().putInt(PREF_LAST_LOGIN_ACTIVITY, activityIndex).apply();
    }

    public int getLastLoginActivity() {
        return sharedPreferences.getInt(PREF_LAST_LOGIN_ACTIVITY, ACTIVITY_LOGIN_1);
    }

    public void clearLastLoginActivity() {
        sharedPreferences.edit().remove(PREF_LAST_LOGIN_ACTIVITY).apply();
    }
}
