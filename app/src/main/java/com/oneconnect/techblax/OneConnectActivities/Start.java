package com.oneconnect.techblax.OneConnectActivities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.integration.android.IntentIntegrator;
import com.oneconnect.techblax.R;

public class Start extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_start);

        findViewById(R.id.QRbt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new IntentIntegrator(Start.this).initiateScan();
            }
        });

        findViewById(R.id.URLbt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Start.this, URL_PreviewActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.QRbt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Start.this, QRactivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.blockbt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Start.this, BlockActivity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.WARNINGbt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Start.this, WarningActivity.class);
                startActivity(intent);
            }
        });

    }

}
