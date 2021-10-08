package com.example.payexample;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Query;

public class PayActivity extends AppCompatActivity {

//    static RequestQueue requestQueue; //Request 작업을 할 Queue
//    Gson gson; //json 파싱
    MyWebViewClient myWebViewClient;
    String tidPin; //결제 고유 번호
    String pgToken; //결제 요청 토큰

    static String productName; //상품 이름
    static Integer productPrice; //상품 가격

    WebView webView;

    public PayActivity() {
    }

    public PayActivity(String productName, Integer productPrice) {
        this.productName = productName;
        this.productPrice = productPrice;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);

//        requestQueue = Volley.newRequestQueue(getApplicationContext());
//        gson = new Gson();

        webView = findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        myWebViewClient = new MyWebViewClient();
        webView.setWebViewClient(myWebViewClient);

        myWebViewClient.readyAndCallPay();

//        requestQueue.add(myWebViewClient.readyRequest);
    }

    public class MyWebViewClient extends WebViewClient {

        RESTApi mRESTApi = RESTApi.retrofit.create(RESTApi.class);

        public void readyAndCallPay() {
            String cid = "TC0ONETIME";
            String partner_order_id = "1001";
            String partner_user_id = "gorany";
            String item_name = productName;
            Integer quantity = 1;
            Integer total_amount = productPrice;
            Integer tax_free_amount = 0;
            String approval_url = "https://developers.kakao.com";
            String cancel_url = "https://naver.com";
            String fail_url = "https://google.com";

            mRESTApi.paymentReady(cid, partner_order_id, partner_user_id, item_name, quantity,
                    total_amount, tax_free_amount, approval_url, cancel_url, fail_url).enqueue(new Callback<Ready>() {
                @Override
                public void onResponse(Call<Ready> call, retrofit2.Response<Ready> response) {
                    Ready ready = (Ready) response.body();

                    if (ready != null) {
                        String url = ready.getNext_redirect_mobile_url();
                        String tid = ready.getTid();

                        webView.loadUrl(url);
                        tidPin = tid;
                    }
                }

                @Override
                public void onFailure(Call<Ready> call, Throwable throwable) {
                    Log.e("Debug", "Error: " + throwable.getMessage());
                }
            });
        }

        public void approvePayment() {
            String cid = "TC0ONETIME";
            String tid = tidPin;
            String partner_order_id = "1001";
            String partner_user_id = "gorany";
            String pg_token = pgToken;
            Integer total_amount = productPrice;

            mRESTApi.paymentApprove(cid, tid, partner_order_id, partner_user_id, pg_token, total_amount).enqueue(new Callback<Approve>() {
                @Override
                public void onResponse(Call<Approve> call, Response<Approve> response) {
                    if(response.code() == 200) {
                        Log.d("Debug", "payment approve success");
                    }
                }

                @Override
                public void onFailure(Call<Approve> call, Throwable throwable) {
                    Log.e("Debug", "Error: " + throwable.getMessage());
                }
            });
        }

        // URL 변경시 발생 이벤트
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.d("Debug", "change url: " + url);

            if (url != null && url.contains("pg_token=")) {
                String pg_Token = url.substring(url.indexOf("pg_token=") + 9);
                pgToken = pg_Token;

                this.approvePayment();

            } else if (url != null && url.startsWith("intent://")) {
                try {
                    Intent intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                    startActivity(intent);
                    return true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            view.loadUrl(url);
            return false;
        }

//        Response.ErrorListener mErrorListener = new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.e("Debug", "Error: " + error);
//            }
//        };

//        //결제 준비 단계: 통신을 받을 Response 변수
//        Response.Listener<String> readyResponse = new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                //success -> JSON parsing
//                JsonParser parser = new JsonParser();
//                JsonElement element = parser.parse(response);
//
//                String url = element.getAsJsonObject().get("next_redirect_mobile_url").getAsString();
//                String tid = element.getAsJsonObject().get("tid").getAsString();
//
//                webView.loadUrl(url);
//                tidPin = tid;
//            }
//        };

//        // 결제 준비 단계 - 통신을 넘겨줄 Request 변수
//        StringRequest readyRequest = new StringRequest(Request.Method.POST, "https://kapi.kakao.com/v1/payment/ready", readyResponse, mErrorListener) {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Log.e("Debug", "name : " + productName);
//                Log.e("Debug", "price : " + productPrice);
//
//                Map<String, String> params = new HashMap<>();
//                params.put("cid", "TC0ONETIME"); // 가맹점 코드
//                params.put("partner_order_id", "1001"); // 가맹점 주문 번호
//                params.put("partner_user_id", "gorany"); // 가맹점 회원 아이디
//                params.put("item_name", productName); // 상품 이름
//                params.put("quantity", "1"); // 상품 수량
//                params.put("total_amount", productPrice.toString()); // 상품 총액
//                params.put("tax_free_amount", "0"); // 상품 비과세
//                params.put("vat_amount", "2222");
//                params.put("approval_url", "https://developers.kakao.com"); // 결제 성공시 돌려 받을 url 주소
//                params.put("cancel_url", "https://google.com"); // 결제 취소시 돌려 받을 url 주소
//                params.put("fail_url", "https://naver.com"); // 결제 실패시 돌려 받을 url 주소
//                return params;
//            }
//
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> headers = new HashMap<>();
//                headers.put("Authorization", "KakaoAK c9bcf20e5471151fd33d1700ef5476e3");
//                headers.put("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
//                return headers;
//            }
//        };
//
//        // 결제 요청 단계 - 통신을 받을 Response 변수
//        Response.Listener<String> approvalResponse = new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                Log.e("Debug", response);
//            }
//        };
//
//        // 결제 요청 단계 - 통신을 넘겨줄 Request 변수
//        StringRequest approvalRequest = new StringRequest(Request.Method.POST, "https://kapi.kakao.com/v1/payment/approve", approvalResponse, mErrorListener) {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<>();
//                params.put("cid", "TC0ONETIME");
//                params.put("tid", tidPin);
//                params.put("partner_order_id", "1001");
//                params.put("partner_user_id", "gorany");
//                params.put("pg_token", pgToken);
//                params.put("total_amount", productPrice.toString());
//                return params;
//            }
//
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> headers = new HashMap<>();
//                headers.put("Authorization", "KakaoAK " + "c9bcf20e5471151fd33d1700ef5476e3");
//                return headers;
//            }
//        };
    }
}