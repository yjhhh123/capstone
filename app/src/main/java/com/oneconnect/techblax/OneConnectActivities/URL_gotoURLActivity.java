package com.oneconnect.techblax.OneConnectActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.oneconnect.techblax.R;

import java.util.ArrayList;
import java.util.List;

public class URL_gotoURLActivity extends AppCompatActivity {

    private List<String> fetchedUrls = new ArrayList<>();
    private List<String> fetchedWarningUrls = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_url_goto_urlactivity);

        EditText urlEditText = findViewById(R.id.urlEditText);
        Button goButton = findViewById(R.id.goButton);

        // 인텐트에서 스캔된 URL을 가져와서 urlEditText에 설정
        String scannedUrl = getIntent().getStringExtra("scanned_url");
        if (scannedUrl != null) {
            urlEditText.setText(scannedUrl);
        }

        // fetchData() 호출
        fetchData();
        // fetchDataWarning() 호출
        fetchDataWarning();

        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = urlEditText.getText().toString();
                if (!url.isEmpty()) {
                    openUrl(url); // URL을 열기 위한 메서드 호출
                }
            }
        });
    }

    private void openUrl(final String url) {
        final String formattedUrl = url.startsWith("http://") || url.startsWith("https://") ? url : "http://" + url;

        if (isUrlInFetchedData(formattedUrl)) {
            // 차단된 URL에 접속하려 할 때 알림 표시
            Toast.makeText(URL_gotoURLActivity.this, "차단된 URL입니다.", Toast.LENGTH_SHORT).show();
            return; // URL이 차단 목록에 있으므로 더 이상의 처리를 하지 않습니다.
        }

        if (isUrlInFetchedWarningData(formattedUrl)) {
            // 경고 메시지를 사용자에게 표시
            new AlertDialog.Builder(this)
                    .setTitle("경고")
                    .setMessage("신고를 많이 당한 URL입니다. 그래도 접속하시겠습니까?")
                    .setPositiveButton("네", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // 사용자가 "네"를 눌렀을 때 URL 열기
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse(url));
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("아니오", null)
                    .show();
            return;
        }

        // 경고 및 차단되지 않은 경우 URL 열기
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(formattedUrl));
        startActivity(intent);
    }
    private boolean isUrlInFetchedData(String url) {
        // 가져온 URL 목록과 비교하여 입력한 URL이 목록에 있는지 확인합니다.
        return fetchedUrls.contains(url);
    }

    private boolean isUrlInFetchedWarningData(String url) {
        // 가져온 경고 URL 목록과 비교하여 입력한 URL이 목록에 있는지 확인합니다.
        return fetchedWarningUrls.contains(url);
    }

    private void fetchData() {
        String url = "http://seong.dothome.co.kr/fetch_data.php";

        // RequestQueue 초기화
        RequestQueue queue = Volley.newRequestQueue(this);

        // StringRequest 객체 생성
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // 서버에서 응답을 받았을 때 처리하는 코드
                        // 받아온 데이터를 fetchedUrls 리스트에 저장
                        parseFetchedData(response, fetchedUrls);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // 에러 처리
                // 에러 발생 시 알림을 사용자에게 표시
                Toast.makeText(URL_gotoURLActivity.this, "Error fetching data", Toast.LENGTH_SHORT).show();
            }
        });

        // RequestQueue에 요청 추가
        queue.add(stringRequest);
    }

    private void fetchDataWarning() {
        String url = "http://seong.dothome.co.kr/fetch_data_warning.php";

        // RequestQueue 초기화
        RequestQueue queue = Volley.newRequestQueue(this);

        // StringRequest 객체 생성
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // 서버에서 응답을 받았을 때 처리하는 코드
                        // 받아온 데이터를 fetchedWarningUrls 리스트에 저장
                        parseFetchedData(response, fetchedWarningUrls);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // 에러 처리
                // 에러 발생 시 알림을 사용자에게 표시
                Toast.makeText(URL_gotoURLActivity.this, "Error fetching warning data", Toast.LENGTH_SHORT).show();
            }
        });

        // RequestQueue에 요청 추가
        queue.add(stringRequest);
    }

    private void parseFetchedData(String data, List<String> urlList) {
        // 받아온 데이터를 처리하여 주어진 리스트에 저장하는 코드
        String[] urls = data.split("\n");
        urlList.clear(); // 기존의 데이터를 모두 지우고 새로 가져온 데이터로 대체
        for (String url : urls) {
            urlList.add(url.trim()); // 공백을 제거한 후 리스트에 추가
        }
    }
}