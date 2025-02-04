package com.oneconnect.techblax.OneConnectActivities;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.oneconnect.techblax.R;
import com.squareup.picasso.Picasso;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;

public class URL_PrewindowActivity extends AppCompatActivity {

    private EditText linkEditText;
    private Button previewButton;
    private TextView titleTextView;
    private TextView descriptionTextView;
    private ImageView imageView;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_url_prewindow);

        linkEditText = findViewById(R.id.linkEditText);
        previewButton = findViewById(R.id.previewButton);
        titleTextView = findViewById(R.id.titleTextView);
        descriptionTextView = findViewById(R.id.descriptionTextView);
        imageView = findViewById(R.id.imageView);

        String scannedUrl = getIntent().getStringExtra("scanned_url");
        if (scannedUrl != null) {
            linkEditText.setText(scannedUrl);
        }

        previewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = linkEditText.getText().toString().trim();
                if (url.isEmpty()) {
                    Toast.makeText(URL_PrewindowActivity.this, "URL을 입력하세요.", Toast.LENGTH_SHORT).show();
                    return; // URL이 비어있으면 더 이상 진행하지 않음
                }
                if (!isValidUrl(url)) {
                    Toast.makeText(URL_PrewindowActivity.this, "올바른 형식을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return; // 잘못된 URL 형식이면 더 이상 진행하지 않음(url 유효성 검사)
                }
                if (!TextUtils.isEmpty(url)) {
                    new UrlPreviewAsyncTask().execute(url);
                }
            }
        });
    }

    private boolean isValidUrl(String url) {
        return android.util.Patterns.WEB_URL.matcher(url).matches();
    }//url 유효성 확인

    private class UrlPreviewAsyncTask extends AsyncTask<String, Void, URL_PrewindowData> {

        @Override
        protected URL_PrewindowData doInBackground(String... params) {
            String url = params[0];
            try {
                Document doc = Jsoup.connect(url).get();
                String title = doc.title();
                String description = "";
                String imageUrl = "";

                Elements metaTags = doc.select("meta");
                for (Element metaTag : metaTags) {
                    if (metaTag.attr("property").equals("og:description")) {
                        description = metaTag.attr("content");
                    }
                    if (metaTag.attr("property").equals("og:image")) {
                        imageUrl = metaTag.attr("content");
                    }
                }

                return new URL_PrewindowData(title, description, imageUrl);

            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(URL_PrewindowData previewData) {
            if (previewData != null) {
                titleTextView.setText(previewData.getTitle());
                descriptionTextView.setText(previewData.getDescription());
                if (!TextUtils.isEmpty(previewData.getImageUrl())) {
                    Picasso.get().load(previewData.getImageUrl()).into(imageView);
                } else {
                    imageView.setImageBitmap(null);
                }
            }
        }
    }


}
