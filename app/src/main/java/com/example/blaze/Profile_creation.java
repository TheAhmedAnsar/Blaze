package com.example.blaze;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.github.drjacky.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

public class Profile_creation extends AppCompatActivity {
    AppCompatButton bt ;
    Uri  uri,uri2;
    String send_data;
    de.hdodenhof.circleimageview.CircleImageView my_dp;
    HashMap<String, Object> obj = new HashMap<>();
LoginManager loginManager;
    FirebaseStorage storage;
    String cameraPermission[];
    String storagePermission[];
String value;
int permissionAttempts = 0;
int MAX_PERMISSION_ATTEMPTS =  100;

    ConstraintLayout layout;
    ProgressBar progressBar;
ImageView backbutton;
String getphone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_creation);


        bt = findViewById(R.id.Button);
        my_dp = findViewById(R.id.profile_image);
        getWindow().setStatusBarColor(Color.parseColor("#5EC0F6"));

        progressBar = findViewById(R.id.progressbar1);
        layout = findViewById(R.id.constraint);
backbutton = findViewById(R.id.back);
        cameraPermission = new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE};

//        SharedPreferences sharedPreferences = getSharedPreferences("myKey", MODE_PRIVATE);
//        value = sharedPreferences.getString("key", "");
value = FirebaseAuth.getInstance().getUid();
getphone = getIntent().getStringExtra("phone");


        my_dp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                change = true;
                launcher.launch(ImagePicker.Companion.with(Profile_creation.this)
                        .crop()
                        .cropFreeStyle()
                        .galleryOnly()
                        .setOutputFormat(Bitmap.CompressFormat.JPEG)
                        .createIntent()
                );


            }
        });

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//
                layout.setAlpha(0.5F);
                progressBar.setVisibility(View.VISIBLE);
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                uploadImage();



            }
        });

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();



            }


        });






    }

    ActivityResultLauncher<Intent> launcher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), (ActivityResult result) -> {
                if (result.getResultCode() == RESULT_OK) {
                     uri = result.getData().getData();
//                    uri2 = uri;
                    send_data = result.getData().getDataString();



                    if (uri == null)
                    {

                        String data = "https://firebasestorage.googleapis.com/v0/b/blaze-a383b.appspot.com/o/user_user.png?alt=media&token=a679e05c-34af-4fdc-864d-82404f367ca2";
                        uri = Uri.parse(data);
                        my_dp.setImageURI(uri);


                    }

                    else
                    {
                        my_dp.setImageURI(uri);


                    }
//

                    // Use the uri to load the image

                } else if (result.getResultCode() == ImagePicker.RESULT_ERROR) {
                    // Use ImagePicker.Companion.getError(result.getData()) to show an error


                    String data = "https://firebasestorage.googleapis.com/v0/b/blaze-a383b.appspot.com/o/user_user.png?alt=media&token=a679e05c-34af-4fdc-864d-82404f367ca2";
                    uri = Uri.parse(data);
                    my_dp.setImageURI(uri);

//

                }
            });





    private void requestContactsPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 101);
            //            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.READ_CONTACTS}, 21);
        }
    }



    private void uploadImage() {

//
//        uri = Uri.parse(send_data);

        FirebaseStorage storage = FirebaseStorage.getInstance();



        if (uri == null) {

            // Get the drawable resource
//            BitmapDrawable drawable = (BitmapDrawable) getResources().getDrawable(R.drawable.user_user);
//
//            // Convert drawable to Bitmap
//            Bitmap bitmap = drawable.getBitmap();
//            // Convert Bitmap to byte[]
//
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//            byte[] imageData = baos.toByteArray();
//


            String data = "https://static.thenounproject.com/png/642902-200.png";
//            uri = Uri.parse(data);
//            Toast.makeText(Profile_creation.this, "Please add profile picture", Toast.LENGTH_SHORT).show();
            Log.d("uploadImage", "Emptywaala: " + uri);


            HashMap<String, Object> image = new HashMap();

            image.put("image", data);
            FirebaseDatabase.getInstance().getReference().child("users_credentials")
                    .child(value)
                    .updateChildren(image).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {


//                            Toast.makeText(Profile_creation.this, "Uploaded succesfully", Toast.LENGTH_SHORT).show();

                            HashMap<String, Object> message = new HashMap<>();
                            message.put("message", "");
                            message.put("timestamp", "");
                            message.put("phone", "");
                            message.put("senderuid", "");

                            FirebaseDatabase.getInstance().getReference()
                                    .child("users_credentials").child(value).child("messages")
                                    .child(getphone)
                                    .updateChildren(message)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            SharedPreferences prefs = getSharedPreferences("Creation", Context.MODE_PRIVATE);
                                            prefs.edit().putBoolean("profile", true).apply();
                                            HashMap<String, Object> users = new HashMap<>();
                                            users.put(getphone, true);

                                            FirebaseDatabase.getInstance().getReference().child("USERS")
                                                    .updateChildren(users).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {


//                                                                            Intent intent = new Intent(AccountOtp_3.this, AccountPassword_4.class);
//                                                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//                                                                            intent.putExtra("number", number);
                                                            Intent intent = new Intent(Profile_creation.this, MainActivity.class);
                                                            layout.setAlpha(1);
                                                            progressBar.setVisibility(View.GONE);
                                                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                                            startActivity(intent);
                                                            finish();

                                                        }
                                                    });

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
//                                                            Toast.makeText(Profile_creation.this, "profilecreation "+ e.toString(), Toast.LENGTH_SHORT).show();
                                        }
                                    });


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Profile_creation.this, e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    });


//            reference.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
//
//                    if (task.isSuccessful()) {
//
//                        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                            @Override
//                            public void onSuccess(Uri uri) {
//
//
//                                String filePath = uri.toString();
//
//                                obj.put("image", filePath);
//
//
//                            }
//                        });
//
//
//                    }
//
//
//                }
//            });

        }

        else {


            StorageReference reference = storage.getReference().child(value).child("profile_pic");

            reference.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                    if (task.isSuccessful()) {

                        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {


                                String filePath = uri.toString();

                                obj.put("image", filePath);

                                FirebaseDatabase.getInstance().getReference().child("users_credentials")
                                        .child(value)
                                        .updateChildren(obj).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {


                                                HashMap<String, Object> message = new HashMap<>();
                                                message.put("message", "");
                                                message.put("timestamp", "");
                                                message.put("phone", "");
                                                message.put("senderuid", "");

                                                FirebaseDatabase.getInstance().getReference()
                                                        .child("users_credentials").child(value).child("messages")
                                                        .child(getphone)
                                                        .updateChildren(message)
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void unused) {
                                                                SharedPreferences prefs = getSharedPreferences("Creation", Context.MODE_PRIVATE);
                                                                prefs.edit().putBoolean("profile", true).apply();
                                                                HashMap<String, Object> users = new HashMap<>();
                                                                users.put(getphone, true);

                                                                FirebaseDatabase.getInstance().getReference().child("USERS")
                                                                        .updateChildren(users).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                            @Override
                                                                            public void onSuccess(Void unused) {


//                                                                            Intent intent = new Intent(AccountOtp_3.this, AccountPassword_4.class);
//                                                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//                                                                            intent.putExtra("number", number);
                                                                                Intent intent = new Intent(Profile_creation.this, MainActivity.class);
                                                                                layout.setAlpha(1);
                                                                                progressBar.setVisibility(View.GONE);
                                                                                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                                                                startActivity(intent);
                                                                                finish();

                                                                            }
                                                                        });

                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
//                                                            Toast.makeText(Profile_creation.this, "profilecreation "+ e.toString(), Toast.LENGTH_SHORT).show();
                                                            }
                                                        });


                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(Profile_creation.this, e.toString(), Toast.LENGTH_SHORT).show();
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