package com.example.blaze;

import static android.content.Context.MODE_PRIVATE;

import android.Manifest;
import android.content.ContentResolver;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_History#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_History extends Fragment {

    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;

    RecyclerView recyclerView;

    Adapter_history hisory_adapter;
    String keys, value;
    ArrayList<history> dataagain = new ArrayList<>();
LinearLayoutCompat emptyLayout;
    ShimmerFrameLayout frameLayout;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Fragment_History() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_History.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_History newInstance(String param1, String param2) {
        Fragment_History fragment = new Fragment_History();
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
        View view = inflater.inflate(R.layout.fragment__history, container, false);


        recyclerView = view.findViewById(R.id.userList);
        emptyLayout = view.findViewById(R.id.emptyLayout);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        frameLayout = view.findViewById(R.id.shimmer);
        frameLayout.startShimmer();
//        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("myKey", MODE_PRIVATE);
//        value = sharedPreferences.getString("key", "");

        value = FirebaseAuth.getInstance().getUid();



//
//        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CONTACTS)
//                != PackageManager.PERMISSION_GRANTED) {
//            // Permission is not granted, request it
//            requestContactsPermission();
//        }
//

        ContentResolver contentResolver = getActivity().getContentResolver(); // Replace 'context' with your actual context variable
        Adapter_history adapter = new Adapter_history(contentResolver);

        FirebaseDatabase.getInstance().getReference().child("users_credentials").child(value).child("history")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {


                        if (snapshot.hasChildren()) {

                            dataagain.clear();

                            for (DataSnapshot snapshot1 : snapshot.getChildren()) {

                                history History = snapshot1.getValue(history.class);
                                dataagain.add(0, History);

                            }

                        }
                        selecteditemshows selecteditemshow = null;
                        Adapter_history adapter_history = new Adapter_history(dataagain);

                        if (adapter_history.getItemCount()==0)
                        {
                            frameLayout.stopShimmer();
                            frameLayout.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.GONE);
                                emptyLayout.setVisibility(View.VISIBLE);
                            }
                        else
                            {
                                // RecyclerView has items, show the regular layout
                                recyclerView.setVisibility(View.VISIBLE);
                                emptyLayout.setVisibility(View.GONE);
                                frameLayout.stopShimmer();
                                frameLayout.setVisibility(View.GONE);
                                recyclerView.setAdapter(adapter_history);
                            }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        return view;


    }

    private void requestContactsPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
            //            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.READ_CONTACTS}, 21);
        }
    }


//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//
//
//        switch (requestCode)
//        {
//            case PERMISSIONS_REQUEST_READ_CONTACTS: {
//                // If the request is cancelled, the result arrays are empty.
//                if (grantResults.length > 0
//                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    // Permission granted. You can proceed with reading contacts here.
//                    // Your code to read contacts goes here.
////                    recyclerView.setVisibility(View.VISIBLE);
////                    emptyLayout.setVisibility(View.GONE);
//
//
////                    Toast.makeText(getActivity(), "Contacts permission granted.", Toast.LENGTH_SHORT).show();
//                } else {
//                    // Permission denied. You can show a message to the user or handle this case accordingly.
//                    Toast.makeText(getActivity(), "Contacts permission denied.", Toast.LENGTH_SHORT).show();
////                    Log.d("onRequestPermissionsResult", "onRequestPermissionsResult: " + "denied");
//
//
//                }
//                return;
//            }
//
//
//        }
//
//    }

}