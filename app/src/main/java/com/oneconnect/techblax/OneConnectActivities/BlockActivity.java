package com.oneconnect.techblax.OneConnectActivities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.oneconnect.techblax.R;

import java.util.ArrayList;
import java.util.Arrays;

public class BlockActivity extends AppCompatActivity {
    private ListView listViewData;
    private EditText editTextUrl;
    private Button buttonSearch;
    private static final String TAG = "BlockActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_block);

        listViewData = findViewById(R.id.listViewData);
        editTextUrl = findViewById(R.id.editTextUrl);
        buttonSearch = findViewById(R.id.buttonSearch);

        fetchData();

        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchUrl = editTextUrl.getText().toString().trim();
                if (!searchUrl.isEmpty()) {
                    searchUrlInBlockTable(searchUrl);
                } else {
                    Toast.makeText(BlockActivity.this, "검색할 URL을 입력하세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });
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
                        Log.d(TAG, "fetchData response: " + response); // 디버깅 로그 추가
                        String[] urls = response.split("\n");

                        ArrayAdapter<String> adapter = new ArrayAdapter<>(BlockActivity.this,
                                android.R.layout.simple_list_item_1, urls);
                        listViewData.setAdapter(adapter);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // 에러 처리
                Log.e(TAG, "fetchData error: " + error.toString()); // 에러 로그 추가
                String[] errorArray = {"Error: " + error.toString()};
                ArrayAdapter<String> adapter = new ArrayAdapter<>(BlockActivity.this,
                        android.R.layout.simple_list_item_1, errorArray);
                listViewData.setAdapter(adapter);
            }
        });

        // RequestQueue에 요청 추가
        queue.add(stringRequest);
    }

    private void searchUrlInBlockTable(final String searchUrl) {
        String url = "http://seong.dothome.co.kr/search_url.php?url=" + searchUrl;

        // RequestQueue 초기화
        RequestQueue queue = Volley.newRequestQueue(this);

        // StringRequest 객체 생성
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "searchUrlInBlockTable response: " + response); // 디버깅 로그 추가
                        if (response.equals("found")) {
                            Toast.makeText(BlockActivity.this, searchUrl + "\n검색 결과: 피싱 사이트입니다.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(BlockActivity.this, searchUrl + "\n검색 결과: 피싱 사이트가 아닙니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // 에러 처리
                Log.e(TAG, "searchUrlInBlockTable error: " + error.toString()); // 에러 로그 추가
                Toast.makeText(BlockActivity.this, "Error searching URL: " + error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        // RequestQueue에 요청 추가
        queue.add(stringRequest);
    }
}
