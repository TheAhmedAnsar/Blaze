package com.example.blaze;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

public class MyForegroundService extends Service {



    private static final String TAG = "MyForegroundService";
    private static final int NOTIFICATION_ID = 123;
    private FirebaseDatabase database;
    private WindowManager windowManager;
    View toastView;
//    ChildEventListener childEventListener;

    private View overlayView;

    @Override
    public void onCreate() {
        super.onCreate();
//        database = FirebaseDatabase.getInstance();
//        // Set up the Firebase database reference
//        // Replace "your-node" with the actual node name in your Firebase database
//        database.getReference("users_credentials").child("7977334091")
//                .child("messages").addChildEventListener(childEventListener);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Perform any additional setup or operations here
        database = FirebaseDatabase.getInstance();
        // Set up the Firebase database reference
        // Replace "your-node" with the actual node name in your Firebase database


//        childEventListener = new ChildEventListener() {
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
//            }
//
//            @Override
//            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
//
//                for (DataSnapshot snapshot1: snapshot.getChildren())
//                {
//                    if (snapshot1.exists())
//                    {
//
////                    Intent intent = new Intent(getApplicationContext(), Blaze_send.class);
//
//                        Intent  intent = getPackageManager().getLaunchIntentForPackage("com.example.blaze");
//                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY);
////
////                    intent.setComponent(new ComponentName(getApplicationContext().getPackageName(), Blaze_send.class.getName()));
//                        startActivity(intent);
//                        Toast.makeText(getApplicationContext(), "Yess we have", Toast.LENGTH_SHORT).show();
//
//                    }
//                    else
//                    {
//                        Toast.makeText(getApplicationContext(), "We dont hve", Toast.LENGTH_SHORT).show();
//                    }
//                    System.out.println(snapshot1.getKey() + " This is service class");
//                    Log.d("OK", snapshot1.getKey() + " This is service class");
//
//
//                }
//
//            }
//
//            @Override
//            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        };

        database.getReference("users_credentials").child("7977334091")
                .child("messages").addChildEventListener(childEventListener);


        return START_STICKY;
    }


    private ChildEventListener childEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {



        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            for (DataSnapshot snapshot1: snapshot.getChildren())
            {
                if (snapshot1.exists())
                {


//                    windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
//
////                    LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
//
//                    // Create an overlay view
//                    overlayView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.mylist, null);
//
//                    // Set up the layout params for the overlay view
//                    WindowManager.LayoutParams params = new WindowManager.LayoutParams(
//                            WindowManager.LayoutParams.MATCH_PARENT,
//                            WindowManager.LayoutParams.MATCH_PARENT,
//                            Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ?
//                                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY :
//                                    WindowManager.LayoutParams.TYPE_PHONE,
//                            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
//                                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
//                                    WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN |
//                                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
//                            PixelFormat.TRANSLUCENT);
//                    params.gravity = Gravity.CENTER;
//
//                    windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
//                    windowManager.addView(overlayView, params);



//                    Intent  intent = getPackageManager().getLaunchIntentForPackage("com.example.blaze");
//                    PackageManager packageManager = getPackageManager();
//                    startActivity(packageManager.getLaunchIntentForPackage("com.example.blaze"));
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    startActivity(intent);
//

                    Toast.makeText(getApplicationContext(), "Yess we have", Toast.LENGTH_SHORT).show();
                    showFullscreenToast();

                }
                else
                {
                    Toast.makeText(getApplicationContext(), "We dont hve", Toast.LENGTH_SHORT).show();
                }
                System.out.println(snapshot1.getKey() + " This is service class");
                Log.d("OK", snapshot1.getKey() + " This is service class");


            }

        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot snapshot) {

        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };

//    private void showFullscreenToast() {
//        // Inflate the custom view for the toast
//        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View layout = inflater.inflate(R.layout.mylist, null);
//        // Set the text or update any other view properties as needed
//        TextView textView = layout.findViewById(R.id.toast_text);
//
//
//
//
//        // Create the toast and set its view
//        // Create a new Toast object
//        Toast toast = new Toast(getApplicationContext());
//        toast.setView(layout);
//        toast.setDuration(Toast.LENGTH_LONG);
//        // Set the toast gravity to fullscreen
//        toast.setGravity(Gravity.FILL, 0, 0);
//
//        // Get the window manager and display the toast
//        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
//        params.height = WindowManager.LayoutParams.MATCH_PARENT;
//        params.width = WindowManager.LayoutParams.MATCH_PARENT;
//        params.format = PixelFormat.TRANSLUCENT;
//        params.type = WindowManager.LayoutParams.TYPE_TOAST;
//
//        WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
//        windowManager.addView(layout, params);
//    }



    private void showFullscreenToast() {
        // Inflate the custom view for the toast
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        toastView = inflater.inflate(R.layout.mylist, null);

        // Set the text or update any other view properties as needed
        TextView textView = toastView.findViewById(R.id.toast_text);
        textView.setText("This is a fullscreen toast");

        // Create WindowManager.LayoutParams for the toast window
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                PixelFormat.TRANSLUCENT
        );

        // Get the system WindowManager
        windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);

        // Add the toast view to the WindowManager
        if (windowManager != null) {
            windowManager.addView(toastView, params);

            // Delay the removal of the toast to simulate its duration
            Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    removeFullscreenToast();
                }
            }, 3000); // Adjust the delay according to your needs
        }
    }


    private void removeFullscreenToast() {
        if (windowManager != null && toastView != null) {
            windowManager.removeView(toastView);
            toastView = null;
        }
    }




    // ...



//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        // Remove the child event listener when the service is destroyed
//
//        Toast.makeText(getApplicationContext(), "Destroyes", Toast.LENGTH_SHORT).show();
////        database.getReference("your-node").removeEventListener(childEventListener);
//    }



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }



}
