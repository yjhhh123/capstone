package com.oneconnect.techblax.OneConnectActivities;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.app.AlertDialog;


import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.oneconnect.techblax.R;

public class URL_PreviewActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_url_preview);

        Button detectButton = findViewById(R.id.detectButton);
        Button domainInfoButton = findViewById(R.id.domainInfoButton);
        Button prewindowButton = findViewById(R.id.prewindowButton);
        Button vpnButton = findViewById(R.id.vpnButton);
        Button gotoUrlButton = findViewById(R.id.gotoURLButton);
        String scannedUrl = getIntent().getStringExtra("scanned_url");

        detectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(URL_PreviewActivity.this, URL_DetectActivity.class);
                intent.putExtra("search", true);
                startActivity(intent);
            }
        });

        domainInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(URL_PreviewActivity.this, URL_WhoisActivity.class);
                intent.putExtra("scanned_url", scannedUrl);
                startActivity(intent);
            }
        });

        prewindowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(URL_PreviewActivity.this, URL_PrewindowActivity.class);
                intent.putExtra("scanned_url", scannedUrl);
                startActivity(intent);
            }
        });

        vpnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(URL_PreviewActivity.this, SplashScreen.class);
                startActivity(intent);
            }
        });
        gotoUrlButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // URL_gotoURLActivity로 이동하고 URL 전달
                Intent intent = new Intent(URL_PreviewActivity.this, URL_gotoURLActivity.class);
                intent.putExtra("scanned_url", scannedUrl);
                startActivity(intent);
            }
        });
    }
}