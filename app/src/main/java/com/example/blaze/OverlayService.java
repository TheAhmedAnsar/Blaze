package com.example.blaze;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.NotificationTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;

public class OverlayService extends Service {
    private WindowManager windowManager;
    private View overlayView;
    String name;
    private PowerManager.WakeLock wakeLock;
    String sendernumber;
    private boolean isRingtonePlaying = false;
    modal moda;
    private static final int REQUEST_CODE_NEW_ACTIVITY = 1;
    Ringtone ringtone;

    private ChildEventListener childEventListener;

    private static final String CHANNEL_ID = "notification_channel";
//    private static final String CHANNEL_IDD = "notification_channell";
    private static final int NOTIFICATION_ID = 123;
    private static final int NOTIFICATION_IDD = 1234;

    @Override
    public void onCreate() {
        super.onCreate();
//
//        // Create a notification for the foreground service
//        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
//                .setContentTitle("Blaze is running")
//                .setContentText("This will not affect your system...")
//                .setSmallIcon(R.drawable.photo)
//                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
//        Notification notification = notificationBuilder.build();
////        // Create a notification for the foreground service
////        Notification notification = createNotification();
//
//        // Start the service in the foreground with the provided notification
//        startForeground(NOTIFICATION_ID, notification);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        // Create a notification for the foreground service
//        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
//                .setContentTitle("Blaze is running")
//                .setContentText("This will not affect your system...")
//                .setSmallIcon(R.drawable.photo)
//                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
//        Notification notification = notificationBuilder.build();
////        // Create a notification for the foreground service
//        Notification notification = createNotification();
//
//        // Start the service in the foreground with the provided notification
//        startForeground(NOTIFICATION_ID, notification);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                                NotificationChannel channel = new NotificationChannel(
                                                        CHANNEL_ID,
                                                        "My Channel",
                                                        NotificationManager.IMPORTANCE_DEFAULT
                                                );

                                                channel.setSound(null, null);
                                                NotificationManager notificationManager = getSystemService(NotificationManager.class);
                                                if (notificationManager != null) {
                                                    notificationManager.createNotificationChannel(channel);
                                                }
                                            }

        Notification notificationforeground = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                .setContentTitle("Blaze is running")
                .setContentText("This will not affect your system...")
                .setSmallIcon(R.drawable.photo)
                .setSilent(true)
                .build();

        // Show the notification
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        if (notificationManager != null) {
            notificationManager.notify(111, notificationforeground);
        }

        startForeground(111, notificationforeground);


        FirebaseDatabase.getInstance().getReference().child("users_credentials").child(FirebaseAuth.getInstance().getUid())
                .child("messages")
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {


                        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("DND_KEY", MODE_PRIVATE);
                        boolean boolvalue = sharedPreferences.getBoolean("DND", false);

                        Intent intent = new Intent(getApplicationContext(), recieve_before.class);
                        Intent intent2 = new Intent(getApplicationContext(), reciceve_msg.class);


                        String msg = snapshot.child("message").getValue(String.class);

                        String my = snapshot.getKey();

                        // Decrypt the message after retrieving from Firebase

                        String senderuid = snapshot.child("senderuid").getValue(String.class);
                        sendernumber = snapshot.child("phone").getValue(String.class);


                        FirebaseDatabase.getInstance().getReference().child("users_credentials").child(senderuid)
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {


                                        String profileurl = snapshot.child("image").getValue(String.class);
                                        String sendername = snapshot.child("name").getValue(String.class);
                                        moda = new modal();
                                        moda.setProfile(profileurl);

//                                        String my_phone_number = snapshot.child("phone").getValue(String.class);
                                        String my_uid = snapshot.child("uid").getValue(String.class);
                                        intent.putExtra("closeReceiveActivity", true);
                                        intent.putExtra("name", name);
                                        intent.putExtra("profile", profileurl);
                                        intent.putExtra("message", msg);
                                        intent.putExtra("mynumber", my);
                                        intent.putExtra("number", sendernumber);
                                        intent.putExtra("uid", senderuid);

                                        intent2.putExtra("closeReceiveActivity", true);
                                        intent2.putExtra("name", name);
                                        intent2.putExtra("profile", profileurl);
                                        intent2.putExtra("message", msg);
                                        intent2.putExtra("mynumber", my);
                                        intent2.putExtra("number", sendernumber);
                                        intent2.putExtra("uid", senderuid);

                                        if (boolvalue == false) {

                                            wakeUpScreen();

                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                            Toast.makeText(OverlayService.this, "Running inetent", Toast.LENGTH_SHORT).show();
//                                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                        } else {
//                                                            showNotifi(sendernumber, profileurl, msg);

                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                                NotificationChannel channel = new NotificationChannel(
                                                        "786",
                                                        "My Channel",
                                                        NotificationManager.IMPORTANCE_DEFAULT
                                                );
                                                NotificationManager notificationManager = getSystemService(NotificationManager.class);
                                                if (notificationManager != null) {
                                                    notificationManager.createNotificationChannel(channel);
                                                }
                                            }

                                            // Create a pending intent for the notification
//                                                            Intent intent = new Intent(this, MainActivity.class);
                                            PendingIntent pendingIntent = PendingIntent.getActivity(
                                                    getApplicationContext(),
                                                    0,
                                                    intent2,
                                                    PendingIntent.FLAG_UPDATE_CURRENT
                                            );

                                            Glide.with(getApplicationContext())
                                                    .asBitmap()
                                                    .load(profileurl)
                                                    .into(new SimpleTarget<Bitmap>() {
                                                        @Override
                                                        public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                                                            // Create the notification with the loaded image
                                                            String decryptedMessage = enryptdecrypt.decrypt(msg);
                                                            String contactName = getContactNameFromPhoneNumber("+91" + sendernumber);

                                                            Notification notification = new NotificationCompat.Builder(getApplicationContext(), "786")
                                                                    .setContentTitle(contactName)
                                                                    .setContentText(decryptedMessage)
                                                                    .setSmallIcon(R.drawable.photo)
                                                                    .setLargeIcon(resource)
                                                                    .setContentIntent(pendingIntent)
                                                                    .build();

                                                            // Show the notification
                                                            NotificationManager notificationManager = getSystemService(NotificationManager.class);
                                                            if (notificationManager != null) {
                                                                notificationManager.notify(786, notification);
                                                            }
                                                            wakeUpScreen();

                                                        }
                                                    });
                                        }


                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });


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
                });


        return START_STICKY; // Service will be restarted if killed by the system
    }



    @Override
    public void onDestroy() {
//        super.onDestroy();
//        if (overlayView != null) {
//            windowManager.removeView(overlayView);
//        }

//        showNotification("Service Stopped", "The service has been stopped.");
//        // Release the wake lock
//        if (wakeLock != null && wakeLock.isHeld()) {
//            wakeLock.release();
//        }

    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    private Notification createNotification() {
        // Create and return a notification for the foreground service
        // Customize the notification based on your requirements
        // You can set the notification title, content, icon, etc.
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Blaze is running")
                .setContentText("This will not affect your system...")
                .setSmallIcon(R.drawable.photo)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        return notificationBuilder.build();
    }

    private String getContactNameFromPhoneNumber(String phoneNumber) {
        ContentResolver contentResolver = getContentResolver();
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
        String[] projection = new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME};

        Cursor cursor = contentResolver.query(uri, projection, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                @SuppressLint("Range") String contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
                cursor.close();
                return contactName;
            }
            cursor.close();
        }
        return null;
    }




    private void wakeUpScreen() {
        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        if (powerManager != null) {
            PowerManager.WakeLock wakeLock;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE, "MyApp:WakeLock");
            } else {
                wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "MyApp:WakeLock");
            }
            wakeLock.acquire(5000); // Screen will be awake for 5 seconds
            KeyguardManager keyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
            if (keyguardManager != null) {
                KeyguardManager.KeyguardLock keyguardLock = keyguardManager.newKeyguardLock("MyApp:KeyguardLock");
                keyguardLock.disableKeyguard();
            }
        }
    }
}
