//package com.example.lottecinema;
//
//import retrofit2.Call;
//import retrofit2.http.Body;
//import retrofit2.http.Field;
//import retrofit2.http.FormUrlEncoded;
//import retrofit2.http.POST;
//
//public interface RetrofitAPI {
//
//    @POST("apiLink/info")
//    Call<Model__CheckAlready> postOverlapCheck(@Body Model__CheckAlready modelCheckAlready); //이건 바디 요청시 사용하는거
//
//    @FormUrlEncoded
//    @POST("http://kumas.dev/rotte_cinema/loginobject.do")
//    Call<Model__CheckAlready> postOverlapCheck(@Field("phone") String phoneNum, @Field("message") String message); //이건 요청시 사용하는거 (*데이터를 보낼때)
//}