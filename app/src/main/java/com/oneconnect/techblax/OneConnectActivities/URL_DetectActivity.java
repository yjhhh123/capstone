package com.oneconnect.techblax.OneConnectActivities;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.oneconnect.techblax.R;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class URL_DetectActivity extends AppCompatActivity {

    private EditText editTextURL;
    private WebView webView;
    private TextView predictionTextView;
    private DatabaseReference blockListRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_url_detect);

        editTextURL = findViewById(R.id.editTextText);
        webView = findViewById(R.id.webView);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                webView.getSettings().setLoadsImagesAutomatically(true);
                String js = "javascript:(function() { " +
                        "document.getElementById('form1').style.display = 'block';" +
                        "document.getElementById('form2').style.display = 'none'; })()";
                webView.loadUrl(js);
            }
        });

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        blockListRef = database.getReference("blockList");

        predictionTextView = findViewById(R.id.predictionTextView);

//        webView.loadUrl("https://port-0-flask-2aat2cluo28s1a.sel5.cloudtype.app/");
        webView.loadUrl("https://port-0-flask2-1ru12mlw4kij01.sel5.cloudtype.app/");
    }


    private void sendUrlToWebsite(String url) {
//        webView.loadUrl("https://port-0-flask-2aat2cluo28s1a.sel5.cloudtype.app/?url=" + url);
        webView.loadUrl("https://port-0-flask2-1ru12mlw4kij01.sel5.cloudtype.app/?url=" + url);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                view.evaluateJavascript(
                        "(function() { " +
                                "   var predictionElement = document.getElementById('prediction');" +
                                "   return predictionElement ? predictionElement.innerText : null;" +
                                "})();",
                        value -> {
                            if (value != null && !value.isEmpty() && !value.equals("null")) {
                                predictionTextView.setText(value);
                            } else {
                                predictionTextView.setText("예측 결과를 가져올 수 없습니다.");
                            }
                        }
                );
            }
        });
    }

    private void fetchPredictionResult(String url) {
        OkHttpClient client = new OkHttpClient();

        RequestBody formBody = new FormBody.Builder()
                .add("url", url)
                .build();

        Request request = new Request.Builder()

//                .url("https://port-0-flask-2aat2cluo28s1a.sel5.cloudtype.app/")
                .url("https://port-0-flask2-1ru12mlw4kij01.sel5.cloudtype.app/")
                .post(formBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                final String responseData = response.body().string();

                URL_DetectActivity.this.runOnUiThread(() -> {
                    String prediction = extractPrediction(responseData);
                    predictionTextView.setText(prediction);
                });
            }
        });
    }

    public void onDetectButtonClick(View view) {
        String url = editTextURL.getText().toString();
        sendUrlToWebsite(url);
        fetchPredictionResult(url);
    }

    private String extractPrediction(String responseData) {
        int startIdx = responseData.indexOf("id=\"prediction\"");
        if (startIdx != -1) {
            startIdx = responseData.indexOf(">", startIdx) + 1;
            int endIdx = responseData.indexOf("</h3>", startIdx);
            if (endIdx != -1) {
                return responseData.substring(startIdx, endIdx);
            } else {
                return "예측 결과를 찾을 수 없습니다.";
            }
        } else {
            return "예측 결과를 찾을 수 없습니다.";
        }
    }
}
