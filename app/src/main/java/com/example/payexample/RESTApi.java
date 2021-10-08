package com.example.payexample;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RESTApi {

    @Headers({"Authorization: KakaoAK c9bcf20e5471151fd33d1700ef5476e3",
            "Content-Type:application/x-www-form-urlencoded;charset=utf-8"})

    @POST("v1/payment/ready")
    Call<Ready> paymentReady(
            @Query("cid") String cid,
            @Query("partner_order_id") String partner_order_id,
            @Query("partner_user_id") String partner_user_id,
            @Query("item_name") String item_name,
            @Query("quantity") Integer quantity,
            @Query("total_amount") Integer total_amount,
            @Query("tax_free_amount") Integer tax_free_amount,
            @Query("approval_url") String approval_url,
            @Query("fail_url") String fail_url,
            @Query("cancel_url") String cancel_url);

    @POST("v1/payment/approve")
    Call<Approve> paymentApprove(
            @Query("cid") String cid,
            @Query("tid") String tid,
            @Query("partner_order_id") String partner_order_id,
            @Query("partner_user_id") String partner_user_id,
            @Query("pg_token") String pg_token,
            @Query("total_amount") Integer total_amount);

    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://kapi.kakao.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
}
