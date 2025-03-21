package com.example.blaze;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

public class MainActivity2 extends AppCompatActivity {
    public static final int REQUEST_CODE_OVERLAY_PERMISSION = 1001;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        startService(new Intent(this, OverlayService.class));

//            showOverlayActivity();

        if (hasOverlayPermission()) {
//            showOverlayActivity();


        } else {
            // Overlay permission denied, handle it accordingly
            Toast.makeText(this, "Cant run", Toast.LENGTH_SHORT).show();
        }


    }
        private boolean hasOverlayPermission() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return Settings.canDrawOverlays(this);
            }
            return true;
        }

        private void requestOverlayPermission() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, REQUEST_CODE_OVERLAY_PERMISSION);
            }
        }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == REQUEST_CODE_OVERLAY_PERMISSION) {
                if (hasOverlayPermission()) {
                    showOverlayActivity();
                } else {
                    // Overlay permission denied, handle it accordingly
                }
            }
        }

        @Override
        protected void onResume() {
            super.onResume();
            if (!hasOverlayPermission()) {
                requestOverlayPermission();
            }
        }

    public void showOverlayActivity() {
        Intent intent = new Intent(this, Blaze_send.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}