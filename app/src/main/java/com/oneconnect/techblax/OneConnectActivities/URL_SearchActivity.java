package com.oneconnect.techblax.OneConnectActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.oneconnect.techblax.R;

public class URL_SearchActivity extends AppCompatActivity {

    private EditText editTextURL;
    private WebView webView;
    private DatabaseReference blockListRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_url_search);

        editTextURL = findViewById(R.id.editTextText);
        webView = findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.setVisibility(View.GONE); // WebView를 숨기기

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        blockListRef = database.getReference("blockList");


    }

    public void onSearchButtonClick(View view) {
        final String url = editTextURL.getText().toString();
        blockListRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean isBlocked = false;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String blockedUrl = snapshot.getValue(String.class);
                    if (blockedUrl != null && blockedUrl.equals(url)) {
                        isBlocked = true;
                        break;
                    }
                }
                if (isBlocked) {
                    Toast.makeText(URL_SearchActivity.this, "차단된 URL입니다.", Toast.LENGTH_SHORT).show();
                    webView.setVisibility(View.GONE);
                } else {
                    webView.loadUrl(url);
                    webView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // 에러 처리
            }
        });
    }
}
