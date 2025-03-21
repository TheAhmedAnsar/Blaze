package com.example.blaze;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;

import com.google.android.gms.ads.MobileAds;

public class MyApplication extends Application {
    private Activity currentActivity;



//    @Override
//    public void onCreate() {
//        super.onCreate();
//
//        // Start the OverlayService when the application starts
//        Intent serviceIntent = new Intent(this, OverlayService.class);
//        startService(serviceIntent);
//    }

    public Activity getCurrentActivity() {
        return currentActivity;
    }



    public void setCurrentActivity(Activity currentActivity) {
        this.currentActivity = currentActivity;
    }
}