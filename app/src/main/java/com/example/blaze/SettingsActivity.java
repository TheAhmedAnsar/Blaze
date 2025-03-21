package com.example.blaze;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreferenceCompat;

import java.util.Objects;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new SettingsFragment())
                    .commit();
        }
        getWindow().setStatusBarColor(Color.parseColor("#5EC0F6"));

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);


            SwitchPreferenceCompat dndSwitch = findPreference("attachment");

            dndSwitch.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    // Get the new value of the switch
                    boolean switchValue = (Boolean) newValue;

                    if (switchValue)
                    {
                        SharedPreferences sharedPreferences = (getActivity().getSharedPreferences("DND_KEY", MODE_PRIVATE));
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("DND", true);
                    editor.apply();
//                    }

                    }

//                    if (switchValue){
//
//                    SharedPreferences sharedPreferences = (getActivity().getSharedPreferences("DND_KEY", MODE_PRIVATE));
//                    SharedPreferences.Editor editor = sharedPreferences.edit();
//                    editor.putBoolean("DND", true);
//                    editor.apply();
//                    }

                  else if (!switchValue) {
                        SharedPreferences sharedPreferences = (getActivity().getSharedPreferences("DND_KEY", MODE_PRIVATE));
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("DND", false);
                        editor.apply();

                    }

//                    Toast.makeText(getContext(), String.valueOf(switchValue), Toast.LENGTH_SHORT).show();

                    return true;
                }
            });





        }
    }
}