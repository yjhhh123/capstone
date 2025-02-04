package com.oneconnect.techblax.OneConnectActivities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;

public class WarningActivity extends AppCompatActivity {
    private ListView listViewData;
    private EditText editTextUrl;
    private Button buttonAddUrl;
    private Button buttonSearchUrl;
    private static final String TAG = "WarningActivity";
    private ArrayAdapter<String> adapter;
    private ArrayList<String> urlList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warning);

        listViewData = findViewById(R.id.listViewData2);
        editTextUrl = findViewById(R.id.editTextUrl2);
        buttonAddUrl = findViewById(R.id.buttonAddUrl2);
        buttonSearchUrl = findViewById(R.id.buttonSearchUrl2);

        fetchData();

        buttonAddUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = editTextUrl.getText().toString();
                if (!url.isEmpty()) {
                    try {
                        insertData(URLEncoder.encode(url, "UTF-8"));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        buttonSearchUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchUrl = editTextUrl.getText().toString().trim();
                if (!searchUrl.isEmpty()) {
                    searchUrlInWarningTable(searchUrl);
                } else {
                    Toast.makeText(WarningActivity.this, "검색할 URL을 입력하세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void fetchData() {
        String url = "http://seong.dothome.co.kr/fetch_data_warning.php";

        // RequestQueue 초기화
        RequestQueue queue = Volley.newRequestQueue(this);

        // StringRequest 객체 생성
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "fetchData response: " + response); // 디버깅 로그 추가
                        String[] urls = response.split("\n");
                        urlList.clear();
                        urlList.addAll(Arrays.asList(urls));

                        adapter = new ArrayAdapter<String>(WarningActivity.this, R.layout.list_item_with_delete, R.id.textViewUrl, urlList) {
                            @Override
                            public View getView(final int position, View convertView, ViewGroup parent) {
                                View view = super.getView(position, convertView, parent);
                                Button buttonDelete = view.findViewById(R.id.buttonDelete);
                                buttonDelete.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        deleteData(urlList.get(position));
                                    }
                                });
                                return view;
                            }
                        };
                        listViewData.setAdapter(adapter);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // 에러 처리
                Log.e(TAG, "fetchData error: " + error.toString()); // 에러 로그 추가
                String[] errorArray = {"Error: " + error.toString()};
                adapter = new ArrayAdapter<>(WarningActivity.this,
                        android.R.layout.simple_list_item_1, errorArray);
                listViewData.setAdapter(adapter);
            }
        });

        // RequestQueue에 요청 추가
        queue.add(stringRequest);
    }

    private void insertData(final String url) {
        String insertUrl = "http://seong.dothome.co.kr/insert_data_warning.php?url=" + url;

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, insertUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "insertData response: " + response); // 디버깅 로그 추가
                        fetchData(); // 데이터 삽입 후 리스트를 갱신합니다.
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // 에러 처리
                Log.e(TAG, "insertData error: " + error.toString()); // 에러 로그 추가
                String[] errorArray = {"Error: " + error.toString()};
                adapter = new ArrayAdapter<>(WarningActivity.this,
                        android.R.layout.simple_list_item_1, errorArray);
                listViewData.setAdapter(adapter);
            }
        });

        queue.add(stringRequest);
    }

    private void searchUrlInWarningTable(final String searchUrl) {
        String url = "http://seong.dothome.co.kr/search_url_warning.php?url=" + searchUrl;

        // RequestQueue 초기화
        RequestQueue queue = Volley.newRequestQueue(this);

        // StringRequest 객체 생성
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "searchUrlInWarningTable response: " + response); // 디버깅 로그 추가
                        if (response.equals("found")) {
                            Toast.makeText(WarningActivity.this, searchUrl + "\n검색 결과: 신고 기록이 있는 사이트 입니다.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(WarningActivity.this, searchUrl + "\n검색 결과: 신고 기록이 없는 사이트 입니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // 에러 처리
                Log.e(TAG, "searchUrlInWarningTable error: " + error.toString()); // 에러 로그 추가
                Toast.makeText(WarningActivity.this, "Error searching URL: " + error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        // RequestQueue에 요청 추가
        queue.add(stringRequest);
    }

    private void deleteData(final String url) {
        String deleteUrl = "http://seong.dothome.co.kr/delete_data_warning.php?url=" + URLEncoder.encode(url);

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, deleteUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "deleteData response: " + response); // 디버깅 로그 추가
                        if (response.equals("success")) {
                            urlList.remove(url);
                            adapter.notifyDataSetChanged();
                            Toast.makeText(WarningActivity.this, "삭제 성공", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(WarningActivity.this, "삭제 실패", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // 에러 처리
                Log.e(TAG, "deleteData error: " + error.toString()); // 에러 로그 추가
                Toast.makeText(WarningActivity.this, "Error deleting URL: " + error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(stringRequest);
    }
}
