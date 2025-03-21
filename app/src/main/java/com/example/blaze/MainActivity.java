package com.example.blaze;



import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.dialog.MaterialDialogs;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;

//ca-app-pub-1541492480072957/8888812295

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;

    TabLayout tabLayout;
    ViewPager viewPager;
    MaterialToolbar toolbar;
boolean check = false;
    private AdView adView;
    AdRequest adRequest;
    AlertDialog dialog;
    private RewardedAd rewardedAd;
//    public static final int REQUEST_CODE_OVERLAY_PERMISSION = 1001;
private String AdId="ca-app-pub-1541492480072957/8888812295";
    androidx.appcompat.app.AlertDialog  materialAlertDialogBuilder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setStatusBarColor(Color.parseColor("#029CF1"));

        tabLayout=(TabLayout)findViewById(R.id.tabLayout);
        viewPager=(ViewPager)findViewById(R.id.viewpager);


        adView = (AdView) findViewById(R.id.adView);
        adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        toolbar = findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.edit_profile);

        Intent serviceIntent = new Intent(this, OverlayService.class);
        startService(serviceIntent);


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted, request it
            requestContactsPermission();
        } else {

            // Permission is already granted, perform your contact reading logic here
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            }

        }

        MobileAds.initialize(this, new OnInitializationCompleteListener() {

            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
//                Toast.makeText(MainActivity.this, "Successful", Toast.LENGTH_SHORT).show();
            }
        });



//        loadRewardedVideoAd();






        materialAlertDialogBuilder = new MaterialAlertDialogBuilder(this,R.style.materialdesign)
                .setTitle("Requesting permission")
                .setCancelable(false)
                .setMessage("Please enable the 'Display over other apps' permission for Blaze to function properly.")
                .setPositiveButton("Enable", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        requestOverlayPermission();

                    }
                })
                .setNegativeButton("Exit App", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();

                    }
                }).create();

        materialAlertDialogBuilder.setOnShowListener( new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                materialAlertDialogBuilder.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#5EC0F6"));
                materialAlertDialogBuilder.getButton(AlertDialog.BUTTON_NEGATIVE).setHighlightColor(Color.TRANSPARENT);

                materialAlertDialogBuilder.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#5EC0F6"));
                materialAlertDialogBuilder.getButton(AlertDialog.BUTTON_POSITIVE).setHighlightColor(Color.TRANSPARENT);
            }
        });

        if (hasOverlayPermission()) {

            materialAlertDialogBuilder.dismiss();



        } else {

            materialAlertDialogBuilder.dismiss();

        }


        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                                switch (item.getItemId()) {

                                    case R.id.menu_edit:
                                        Intent intent = new Intent(MainActivity.this, Update_profile.class);
                                        startActivity(intent);

                                        return true;

                                    case R.id.menu_settings:

                                        Intent intent1 = new Intent(MainActivity.this, SettingsActivity.class);
                                        startActivity(intent1);

                                        return true;

                                        case R.id.share:
                                            Intent shareIntent = new Intent(Intent.ACTION_SEND);
                                            shareIntent.setType("text/plain");
                                            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name");
                                            String shareMessage= "Try Blaze, the new instant messaging app. Experience seamless communication like never before! " +
                                                    "\nDownload it form here ";
                                            shareMessage = shareMessage + "https://moviesmania123.great-site.net/"+"\n\n";
                                            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                                            startActivity(Intent.createChooser(shareIntent, "choose one"));

                                            return true;

                                            case R.id.Add:
//                                            Intent add = new Intent(Intent.ACTION_SEND);
                                                showRewardedVideoAd();


                                                return true;
                                }
                return false;
            }
        });
   RewardedAd.load(this, AdId, adRequest, new RewardedAdLoadCallback() {
       @Override
       public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
           // Handle the error.
//           Toast.makeText(MainActivity.this, loadAdError.toString(), Toast.LENGTH_SHORT).show();
           rewardedAd = null;
       }

       @Override
       public void onAdLoaded(@NonNull RewardedAd ad) {
           rewardedAd = ad;
//           Toast.makeText(MainActivity.this, "Add loaded", Toast.LENGTH_SHORT).show();
       }
   });


        if (rewardedAd != null) {
            Activity activityContext = MainActivity.this;
            rewardedAd.show(activityContext, new OnUserEarnedRewardListener() {
                @Override
                public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                    // Handle the reward.
                    Log.d("TAG", "The user earned the reward.");
                    int rewardAmount = rewardItem.getAmount();
                    String rewardType = rewardItem.getType();
                }
            });
        } else {
            Log.d("TAG", "The rewarded ad wasn't ready yet.");
        }

        final MyAdapter adapter = new MyAdapter(this,getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);



        String uid = FirebaseAuth.getInstance().getUid();
FirebaseDatabase.getInstance().getReference().child("users_credentials").child(uid)
                .child("lastseen").setValue("Online");

        String lasttime = String.valueOf(System.currentTimeMillis());


FirebaseDatabase.getInstance().getReference().child("users_credentials").child(uid)
                .child("lastseen").onDisconnect().setValue(lasttime);


        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }



    private boolean hasOverlayPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return Settings.canDrawOverlays(this);

        }
        return true;
    }


    private void requestOverlayPermission() {
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:" + getPackageName()));
        startActivityForResult(intent, 100);
    }
//
    private void requestContactsPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
            //            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.READ_CONTACTS}, 21);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {


        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If the request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted. You can proceed with reading contacts here.
                    // Your code to read contacts goes here.
//                    recyclerView.setVisibility(View.VISIBLE);
//                    emptyLayout.setVisibility(View.GONE);

                    Intent intent = new Intent(this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
//                    Toast.makeText(getActivity(), "Contacts permission granted.", Toast.LENGTH_SHORT).show();
                } else {

                    Log.d("onRequestPermissionsResult", "onRequestPermissionsResult: " + " permission denied");

                    // Permission denied. You can show a message to the user or handle this case accordingly.
//                    Toast.makeText(getActivity(), "Contacts permission denied.", Toast.LENGTH_SHORT).show();
//                    Log.d("onRequestPermissionsResult", "onRequestPermissionsResult: " + "denied");

//                    showPermissionExplanationDialog();

//                    requestContactsPermission();

                }
                return;
            }


        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        materialAlertDialogBuilder = new MaterialAlertDialogBuilder(this, R.style.materialdesign)
                .setTitle("Requesting permission")
                .setCancelable(false)
                .setMessage("Please enable the 'Display over other apps' permission for Blaze to function properly.")
                .setPositiveButton("Enable", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        requestOverlayPermission();

                    }
                })
                .setNegativeButton("Exit App", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();

                    }
                }).create();

        //2. now setup to change color of the button
        materialAlertDialogBuilder.setOnShowListener( new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                materialAlertDialogBuilder.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#5EC0F6"));
                materialAlertDialogBuilder.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#5EC0F6"));

            }
        });

        materialAlertDialogBuilder.show();

        if (requestCode == 100) {

            if (hasOverlayPermission()) {

//                Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
                materialAlertDialogBuilder.dismiss();

                check = true;

            } else {

                materialAlertDialogBuilder.show();



            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!hasOverlayPermission()) {

             materialAlertDialogBuilder = new MaterialAlertDialogBuilder(this, R.style.materialdesign)
                    .setTitle("Requesting permission")
                    .setCancelable(false)
                     .setMessage("Please enable the 'Display over other apps' permission for Blaze to function properly.")
                     .setPositiveButton("Enable", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            requestOverlayPermission();

                        }
                    })
                    .setNegativeButton("Exit App", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            finish();


                        }
                    }).create();

            //2. now setup to change color of the button
            materialAlertDialogBuilder.setOnShowListener( new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface arg0) {
                    materialAlertDialogBuilder.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#5EC0F6"));
                    materialAlertDialogBuilder.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#5EC0F6"));

                }
            });


            materialAlertDialogBuilder.show();


//            requestOverlayPermission();
        }
        if (adView != null) {
            adView.resume();
        }



    }

    @Override
    public void onPause() {
        if (adView != null) {
            adView.pause();
        }

        materialAlertDialogBuilder.cancel();

        super.onPause();
    }



    @Override
    public void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }
        materialAlertDialogBuilder.cancel();

        super.onDestroy();
    }

    private void showRewardedVideoAd() {
        if (rewardedAd != null) {
            Activity activityContext = MainActivity.this;
            rewardedAd.show(activityContext, new OnUserEarnedRewardListener() {
                @Override
                public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                    // Handle the reward.
                    int rewardAmount = rewardItem.getAmount();
                    String rewardType = rewardItem.getType();
//                    finish();
                    // You can implement your reward logic here based on the rewardAmount and rewardType.
                }
            });
        } else {
            Log.d("TAG", "The rewarded ad wasn't ready yet.");
        }
    }


    void loadRewardedVideoAd()

    {

        RewardedAd.load(this, AdId,
                adRequest, new RewardedAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error.
                        Log.d("TAG", loadAdError.toString());
                        Toast.makeText(MainActivity.this, "Currently no adds to show", Toast.LENGTH_SHORT).show();
                        rewardedAd = null;
                    }

                    @Override
                    public void onAdLoaded(@NonNull RewardedAd ad) {
                        rewardedAd = ad;
                        Log.d("TAG", "Ad was loaded.");
                    }


                });
    }



//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//    }
}