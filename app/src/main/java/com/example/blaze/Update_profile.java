package com.example.blaze;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.github.drjacky.imagepicker.ImagePicker;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Arrays;
import java.util.HashMap;

public class Update_profile extends AppCompatActivity {


    de.hdodenhof.circleimageview.CircleImageView my_dp;
    ConstraintLayout constraintLayout;
    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;
    AppCompatButton saveButton;
    TextInputEditText my_name, mynumber_123;
    String send_data;
    ProgressBar progressBar;

    Uri uri, uri2;
    TextView profile_change;

    String value, class_name;
    boolean change, uploading;


    private AdView adView;
    AdRequest adRequest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        change = false;
        uploading = false;


        my_dp = findViewById(R.id.profile_image);
        my_name = findViewById(R.id.username);
        mynumber_123 = findViewById(R.id.my_number);


        constraintLayout = findViewById(R.id.constraint_layout);
        saveButton = findViewById(R.id.save_button);

        profile_change = findViewById(R.id.change_pic);
        progressBar = findViewById(R.id.progressbar1);

        getWindow().setStatusBarColor(Color.parseColor("#FFFFFF"));
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        auth = FirebaseAuth.getInstance();

        getWindow().setStatusBarColor(Color.parseColor("#5EC0F6"));


        adView = (AdView) findViewById(R.id.adView);
        adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        MobileAds.initialize(this, new OnInitializationCompleteListener() {

            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
//                Toast.makeText(MainActivity.this, "Successful", Toast.LENGTH_SHORT).show();
            }
        });

//        // Set the test device ID
//        RequestConfiguration requestConfiguration = new RequestConfiguration.Builder()
//                .setTestDeviceIds(Arrays.asList("A858A3A51A8116FEB5251EDD295B3349"))
//                .build();

//
//        MobileAds.setRequestConfiguration(requestConfiguration);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        String uidd = FirebaseAuth.getInstance().getUid();

        FirebaseDatabase.getInstance().getReference().child("users_credentials").child(uidd)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {


                        String name = snapshot.child("name").getValue(String.class);
                        String profile = snapshot.child("image").getValue(String.class);
                        String phonenumber = snapshot.child("phone").getValue(String.class);

//                        Log.d("name", "onDataChange: " + snapshot.getKey());
                        RequestOptions requestOptions = new RequestOptions().placeholder(R.drawable.user_user);

                        Glide.with(Update_profile.this)
                                .applyDefaultRequestOptions(requestOptions)
                                .load(profile).into(my_dp);
                        my_name.setText(name);
                     mynumber_123.setText(phonenumber);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


        profile_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                change = true;
                launcher.launch(ImagePicker.Companion.with(Update_profile.this)
                        .crop()
                        .cropFreeStyle()
                        .galleryOnly()
                        .setOutputFormat(Bitmap.CompressFormat.JPEG)
                        .createIntent()
                );


            }
        });


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String newName = my_name.getText().toString();


                HashMap<String, Object> obj = new HashMap<>();
                obj.put("name", newName);
                constraintLayout.setAlpha(0.5F);
                progressBar.setVisibility(View.VISIBLE);

                if (newName.isEmpty()) {
                    Toast.makeText(Update_profile.this, "Username cannot be empty", Toast.LENGTH_SHORT).show();
                    constraintLayout.setAlpha(1);
                    progressBar.setVisibility(View.VISIBLE);
                }


                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);


                if (my_dp.getTag(R.id.source) != null && my_dp.getTag(R.id.source).equals("internet")) {
//                    Toast.makeText(Update_profile.this, "Internet", Toast.LENGTH_SHORT).show();

                } else {
                    if (my_dp.getTag(R.id.source) != null && my_dp.getTag(R.id.source).equals("gallery")) {
//                        Toast.makeText(Update_profile.this, "Gallery", Toast.LENGTH_SHORT).show();
                        uploadImage();

                        Toast.makeText(Update_profile.this, "Uploaded succesfully", Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(Update_profile.this, MainActivity.class);
//                        startActivity(intent);
                        finish();

                    }
                }

                database.getReference().child("users_credentials").child(FirebaseAuth.getInstance().getUid())
                        .updateChildren(obj).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                if (uploading == false) {

                                    constraintLayout.setAlpha(1);
                                    progressBar.setVisibility(View.GONE);

                                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                                    Toast.makeText(Update_profile.this, "Your Profile has been Updated", Toast.LENGTH_SHORT).show();

//                                    Toast.makeText(Update_profile.this, "Uploaded succesfully", Toast.LENGTH_SHORT).show();
//                                    Intent intent = new Intent(Update_profile.this, MainActivity.class);
//                                    startActivity(intent);

                                    finish();
                                }

                            }
                        });


            }
        });


    }



    ActivityResultLauncher<Intent> launcher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), (ActivityResult result) -> {
                if (result.getResultCode() == RESULT_OK) {
                     uri = result.getData().getData();
                    uri2 = uri;
                    send_data = result.getData().getDataString();

//                        FirebaseStorage storage = FirebaseStorage.getInstance();
//                        long time = new Date().getTime();
//                        StorageReference reference = storage.getReference().child("Profiles").child(time + "");

                    // Use the uri to load the image
                    my_dp.setImageURI(uri);
                    my_dp.setTag(R.id.source, "gallery");
                } else if (result.getResultCode() == ImagePicker.RESULT_ERROR) {
                    // Use ImagePicker.Companion.getError(result.getData()) to show an error


                }
            });
    private void uploadImage() {

//        uri = Uri.parse(send_data);
//

        if (uri == null) {

            Toast.makeText(Update_profile.this, "Please add profile picture", Toast.LENGTH_SHORT).show();

        }

        else {

            FirebaseStorage storage = FirebaseStorage.getInstance();

            uploading = true;

            String uid = FirebaseAuth.getInstance().getUid();
            ;

            StorageReference reference = storage.getReference().child(uid).child("profile_pic");
            reference.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                    if (task.isSuccessful()) {

                        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {


                                String filePath = uri.toString();
                                HashMap<String, Object> obj = new HashMap<>();

                                obj.put("image", filePath);
                                obj.put("created", true);

                                FirebaseDatabase.getInstance().getReference().child("users_credentials")
                                        .child(FirebaseAuth.getInstance().getUid())
                                        .updateChildren(obj).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {


                                                constraintLayout.setAlpha(1);
                                                progressBar.setVisibility(View.GONE);
                                                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                                                uploading = false;

                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(Update_profile.this, e.toString(), Toast.LENGTH_SHORT).show();
                                            }
                                        });

                            }
                        });


                    }


                }
            });


        }

    }


}