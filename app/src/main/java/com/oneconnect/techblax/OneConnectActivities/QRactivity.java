package com.oneconnect.techblax.OneConnectActivities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.oneconnect.techblax.R;

public class QRactivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qractivity);

        // QR 코드 스캔을 시작합니다.
        startQRScan();
    }

    // QR 코드 스캔을 시작하는 메소드
    private void startQRScan() {
        new IntentIntegrator(this).initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null && result.getContents() != null) {
            // 스캔된 결과를 복사합니다.
            String scannedUrl = result.getContents();

            // URL_PreviewActivity로 이동합니다.
            Intent intent = new Intent(QRactivity.this, URL_PreviewActivity.class);
            intent.putExtra("scanned_url", scannedUrl); // 스캔한 URL을 인텐트에 추가
            startActivity(intent);
        } else {
            // 스캔된 결과가 없으면 메시지를 표시합니다.
            Toast.makeText(this, "QR 코드를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
        }
    }
}