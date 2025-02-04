package com.oneconnect.techblax.OneConnectActivities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.oneconnect.techblax.R;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class URL_WhoisActivity extends AppCompatActivity {

    private EditText editTextDomain;
    private TextView textViewResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_url_whois);

        editTextDomain = findViewById(R.id.editTextDomain);
        textViewResult = findViewById(R.id.textViewResult);

        String scannedUrl = getIntent().getStringExtra("scanned_url");
        if (scannedUrl != null) {
            editTextDomain.setText(scannedUrl);
        }
    }

    public void onInfoButtonClick(View view) {
        String domain = editTextDomain.getText().toString().trim();
        if (!domain.isEmpty()) {
            String serviceKey = "2xqdyHYx3eNO9IRFpd2BOVpPNRuadd0eqd%2Fy2HEemA3l75uDLosh9322BbIU6IJsR1eR33pj%2BLJBSgIVrDOrHg%3D%3D";
            String encodedQuery = URLEncoder.encode(domain);
            String apiUrl = "http://apis.data.go.kr/B551505/whois/domain_name?serviceKey=" + serviceKey + "&query=" + encodedQuery + "&answer=xml";
            new RetrieveWhoisTask().execute(apiUrl);
        } else {
            Toast.makeText(this, "도메인 이름을 입력하세요.", Toast.LENGTH_SHORT).show();
        }
    }

    public void onExtractDomainButtonClick(View view) {
        String url = editTextDomain.getText().toString().trim();
        if (!url.isEmpty()) {
            try {
                // URL에서 'http://' 또는 'https://' 이후의 문자열 추출
                int startIndex = url.indexOf("://");
                if (startIndex != -1) {
                    startIndex += 3; // 프로토콜 종류와 '://' 문자열을 건너뛰고 시작
                } else {
                    startIndex = 0; // 프로토콜이 명시되어 있지 않은 경우
                }

                int endIndex = url.indexOf("/", startIndex);
                if (endIndex == -1) {
                    endIndex = url.length(); // '/'가 없는 경우 URL의 끝까지 문자열 추출
                }

                String domain = url.substring(startIndex, endIndex);

                String domain2 = "";

                // '.'가 2개 이상 포함되어 있는지 확인하여 domain2 설정
                int dotIndex1 = domain.indexOf(".");
                if (dotIndex1 != -1) {
                    int dotIndex2 = domain.indexOf(".", dotIndex1 + 1);
                    if (dotIndex2 != -1) {
                        domain2 = domain.substring(dotIndex1 + 1, dotIndex2);
                    }
                }

                // 도메인 이름 출력
                textViewResult.setText("도메인: " + domain);
                // 두 번째 변수가 비어있지 않은 경우에만 출력
                if (!domain2.isEmpty()) {
                    textViewResult.append("\n도메인 이름: " + domain2);
                }
            } catch (Exception e) {
                e.printStackTrace();
                textViewResult.setText("URL을 파싱하는 동안 문제가 발생했습니다.");
            }
        } else {
            Toast.makeText(this, "URL을 입력하세요.", Toast.LENGTH_SHORT).show();
        }
    }





    private class RetrieveWhoisTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            StringBuilder result = new StringBuilder();
            HttpURLConnection connection = null;
            try {
                URL url = new URL(urls[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line).append("\n");
                }
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
                result.append("Error: ").append(e.getMessage());
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
            return result.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            Log.d("API_RESPONSE", result);
            String parsedResult = parseWhoisResponse(result);
            textViewResult.setText(parsedResult);
        }

        private String parseWhoisResponse(String xmlResponse) {
            try {
                // XML 파싱
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document doc = builder.parse(new InputSource(new StringReader(xmlResponse)));

                // 태그의 내용 추출
                String resultCode = extractTagContent(doc, "result_code");
                String resultMsg = extractTagContent(doc, "result_msg");
                String name = extractTagContent(doc, "name");
                String regName = extractTagContent(doc, "regName");
                String addr = extractTagContent(doc, "addr");
                String post = extractTagContent(doc, "post");
                String adminName = extractTagContent(doc, "adminName");
                String adminEmail = extractTagContent(doc, "adminEmail");
                String adminPhone = extractTagContent(doc, "adminPhone");
                String lastUpdatedDate = extractTagContent(doc, "lastUpdatedDate");
                String regDate = extractTagContent(doc, "regDate");
                String endDate = extractTagContent(doc, "endDate");
                String infoYN = extractTagContent(doc, "infoYN");
                String domainStatus = extractTagContent(doc, "domainStatus");
                String agency = extractTagContent(doc, "agency");
                String agencyUrl = extractTagContent(doc, "agency_url");
                String eRegName = extractTagContent(doc, "e_regName");
                String eAddr = extractTagContent(doc, "e_addr");
                String eAdminName = extractTagContent(doc, "e_adminName");
                String eAgency = extractTagContent(doc, "e_agency");
                String dnssec = extractTagContent(doc, "dnssec");
                String ns1 = extractTagContent(doc, "ns1");
                String ip1 = extractTagContent(doc, "ip1");

                StringBuilder resultBuilder = new StringBuilder();
                resultBuilder.append("결과코드: ").append(resultCode).append("\n");
                resultBuilder.append("결과 메시지: ").append(resultMsg).append("\n");
                resultBuilder.append("도메인: ").append(name).append("\n");
                resultBuilder.append("등록인 이름: ").append(regName).append("\n");
                resultBuilder.append("주소: ").append(addr).append("\n");
                resultBuilder.append("우편번호: ").append(post).append("\n");
                resultBuilder.append("관리자 이름: ").append(adminName).append("\n");
                resultBuilder.append("관리자 이메일 주소: ").append(adminEmail).append("\n");
                resultBuilder.append("관리자 전화번호: ").append(adminPhone).append("\n");
                resultBuilder.append("최종 수정일자: ").append(lastUpdatedDate).append("\n");
                resultBuilder.append("등록일자: ").append(regDate).append("\n");
                resultBuilder.append("만료일자: ").append(endDate).append("\n");
                resultBuilder.append("정부공개여부: ").append(infoYN).append("\n");
                resultBuilder.append("도메인 상태: ").append(domainStatus).append("\n");
                resultBuilder.append("레지스트라 이름: ").append(agency).append("\n");
                resultBuilder.append("레지스트라 URL: ").append(agencyUrl).append("\n");
                resultBuilder.append("등록인 이름(영문): ").append(eRegName).append("\n");
                resultBuilder.append("주소(영문): ").append(eAddr).append("\n");
                resultBuilder.append("관리자 이름(영문): ").append(eAdminName).append("\n");
                resultBuilder.append("레지스트라 이름(영문): ").append(eAgency).append("\n");
                resultBuilder.append("DNSSEC: ").append(dnssec).append("\n");
                resultBuilder.append("네임서버 이름: ").append(ns1).append("\n");
                resultBuilder.append("네임서버 IP: ").append(ip1);

                return resultBuilder.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "Error: Unable to parse XML response";
        }



        private String extractTagContent(Document doc, String tagName) {
            NodeList nodeList = doc.getElementsByTagName(tagName);
            if (nodeList.getLength() > 0) {
                Node node = nodeList.item(0);
                return node.getTextContent();
            }
            return "Not found";
        }
    }
}
