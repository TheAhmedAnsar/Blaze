package com.example.blaze;

import static android.content.Context.MODE_PRIVATE;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_Contacts#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_Contacts extends Fragment {

    // Add a global boolean flag to track whether the permission was already requested.
    private boolean permissionRequested = false;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mUserListAdapter;
    private RecyclerView.LayoutManager mUserListLayoutManager;

    int PERMISSION_REQUEST_CODE = 21;


    ShimmerFrameLayout frameLayout;
LinearLayoutCompat emptyLayout, permission;

    ArrayList<UserObject> userList, contactList;
    String ISO, UID, profile_image;

    ContentResolver contentResolver;
    AppCompatButton invitebutton, enablebutton;

    boolean created;

    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Fragment_Contacts() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_Contacts.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_Contacts newInstance(String param1, String param2) {
        Fragment_Contacts fragment = new Fragment_Contacts();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment__contacts, container, false);

        frameLayout = view.findViewById(R.id.shimmer);
        SharedPreferences prefs = getActivity().getSharedPreferences("contacts", Context.MODE_PRIVATE);
        created = prefs.getBoolean("permission", false);

        contactList = new ArrayList<>();
        userList = new ArrayList<>();
        emptyLayout = view.findViewById(R.id.emptyLayout);
        permission = view.findViewById(R.id.permissiondenied);

        recyclerView = view.findViewById(R.id.userList);

//        muserList = view.findViewById(R.id.userList);

            invitebutton = view.findViewById(R.id.Button);
            enablebutton = view.findViewById(R.id.buttonenable);
        frameLayout.startShimmer();

//        frameLayout.setVisibility(View.GONE);
        contentResolver = getContext().getContentResolver();


//        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CONTACTS)
//                != PackageManager.PERMISSION_GRANTED) {
//            // Permission is not granted, request it
//            requestContactsPermission();
//        } else {
//
//            // Permission is already granted, perform your contact reading logic here
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                initializeRecyclerview();
//                getContactList();
//
//            }
//
//        }


        enablebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);

            }
        });

//
//
        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), android.Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            // Request the permission
            permission.setVisibility(View.VISIBLE);
            frameLayout.stopShimmer();

            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.READ_CONTACTS}, 21);
        } else {
            // Permission is already granted
//            // Perform the desired action
//permission.setVisibility(View.GONE);
//            frameLayout.stopShimmer();
//            recyclerView.setVisibility(View.VISIBLE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                permission.setVisibility(View.GONE);
                initializeRecyclerview();
                getContactList();
            }
        }


//        Log.d("Permissions", "onCreateView: Helolooo" );

invitebutton.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name");
        String shareMessage= "Try Blaze, the new instant messaging app. Experience seamless communication like never before! " +
                "\nDownload it form here ";
        shareMessage = shareMessage + "https://moviesmania123.great-site.net/"+"\n\n";
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
        startActivity(Intent.createChooser(shareIntent, "choose one"));


    }
});

        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void getContactList() {

        String ISOPrefix = getCountryISO();
        Cursor phones = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null
                , null, null, null);

        while (phones.moveToNext()) {
            @SuppressLint("Range") String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            @SuppressLint("Range") String phone = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            phone = phone.replace(" ", "");
            phone = phone.replace("-", "");
            phone = phone.replace("(", "");
            phone = phone.replace(")", "");

            if (!String.valueOf(phone.charAt(0)).equals("+"))
                phone = ISOPrefix + phone;

            UserObject mcontact = new UserObject("", name, phone, "");
            contactList.add(mcontact);
            frameLayout.stopShimmer();
            frameLayout.setVisibility(View.GONE);

            getuserdetails(mcontact);

        }

    }

    private void getuserdetails(UserObject mcontact) {

        DatabaseReference mUserDB = FirebaseDatabase.getInstance().getReference().child("users_credentials");
        mUserDB.keepSynced(true);

        Query query = mUserDB.orderByChild("phone").equalTo(mcontact.getPhone());


        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String phone = "",
                            name = "";
                    UserObject mUser = null;
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        if (snapshot1.child("phone").getValue().toString() != null) {
                            phone = snapshot1.child("phone").getValue().toString();
                        }
                        if (snapshot1.child("image").getValue().toString() != null) {
                            profile_image = snapshot1.child("image").getValue().toString();
                        }
                        if (snapshot1.child("uid").getValue().toString() != null) {

                            UID = snapshot1.child("uid").getValue().toString();
                        }

                        String realname = snapshot1.child("realname").getValue(String.class);
                        mUser = new UserObject(UID, mcontact.getName(), phone, profile_image);
                        // Sort the data alphabetically by name


                    }

                    boolean userExists = false;
                    for (UserObject user : userList) {
                        if (user.getPhone().equals(mUser.getPhone())) {
                            userExists = true;
                            break;
                        }
                    }

                    if (!userExists) {
                        userList.add(mUser);
                    }

                    if (mUserListAdapter.getItemCount()==0)
                    {
                        recyclerView.setVisibility(View.GONE);
                        emptyLayout.setVisibility(View.VISIBLE);
                        permission.setVisibility(View.GONE);
                        frameLayout.stopShimmer();
                        frameLayout.setVisibility(View.GONE);


                    }
                    else
                    {
                        // RecyclerView has items, show the regular layout
                        recyclerView.setVisibility(View.VISIBLE);
                        emptyLayout.setVisibility(View.GONE);
                        permission.setVisibility(View.GONE);
                        frameLayout.stopShimmer();
                        frameLayout.setVisibility(View.GONE);

                    }
                    Log.d("test", "onDataChange: " + mUser.getName());
                    mUserListAdapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private String getCountryISO() {

        TelephonyManager telephonyManager = (TelephonyManager) getContext().getSystemService(getContext().TELEPHONY_SERVICE);
        if (telephonyManager.getNetworkCountryIso() != null)
            if (!telephonyManager.getNetworkCountryIso().toString().equals(""))
                ISO = telephonyManager.getNetworkCountryIso().toString();
        return CountryToPhone.getPhone(ISO);


    }


    private void initializeRecyclerview() {
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(false);
        mUserListLayoutManager = new LinearLayoutManager(getContext(), LinearLayout.VERTICAL, false);
        recyclerView.setLayoutManager(mUserListLayoutManager);
        Collections.sort(userList, new Comparator<UserObject>() {
            @Override
            public int compare(UserObject nameModel1, UserObject nameModel2) {
                return nameModel1.getName().compareToIgnoreCase(nameModel2.getName());
            }
        });

        mUserListAdapter = new UserListAdapter(userList);
        recyclerView.setAdapter(mUserListAdapter);


    }



    private void requestContactsPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

        requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
            //            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.READ_CONTACTS}, 21);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {


        switch (requestCode)
        {
            case PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If the request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted. You can proceed with reading contacts here.
                    // Your code to read contacts goes here.
//                    recyclerView.setVisibility(View.VISIBLE);
//                    emptyLayout.setVisibility(View.GONE);
                    permission.setVisibility(View.GONE);
frameLayout.stopShimmer();
frameLayout.setVisibility(View.GONE);

//                    Toast.makeText(getActivity(), "Contacts permission granted.", Toast.LENGTH_SHORT).show();
                } else {
                    // Permission denied. You can show a message to the user or handle this case accordingly.
//                    Toast.makeText(getActivity(), "Contacts permission denied.", Toast.LENGTH_SHORT).show();
//                    Log.d("onRequestPermissionsResult", "onRequestPermissionsResult: " + "denied");
frameLayout.stopShimmer();
                    recyclerView.setVisibility(View.GONE);
                    emptyLayout.setVisibility(View.GONE);
                    permission.setVisibility(View.VISIBLE);
//                    showPermissionExplanationDialog();

                }
                return;
            }


        }

        }








}