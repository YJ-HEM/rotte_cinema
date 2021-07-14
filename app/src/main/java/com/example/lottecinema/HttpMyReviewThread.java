package com.example.lottecinema;

import android.util.Log;

import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpMyReviewThread extends Thread {
    static public JsonObject result;
    static public String resulttxt;
    static public int watchedMoviesNum;
    static public JSONArray jArray;
    static public ArrayList<MyReviews> review_list = new ArrayList<>();

    static String post(String url) throws IOException {

        //앱 쿠키jar에 쿠키를 저장한다 (아마?)
//        client = new OkHttpClient.Builder().cookieJar(webkitCookieManager).build();
        OkHttpClient client = new OkHttpClient.Builder().cookieJar(MainActivity.cookieJar).build();
        RequestBody formBody = new FormBody.Builder()
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        Response response = client.newCall(request).execute();


        return response.body().string();
    }


    public HttpMyReviewThread() {
        Log.v("okhttp33", "showmyreiew");

    }
    public void run() {
        try {
            // 가장 큰 JSONObject를 가져옵니다.
            JSONObject jsonObject = new JSONObject(post("http://kumas.dev/rotte_cinema/reviewsobject.do"));

            //  배열을 가져옵니다.
            jArray = jsonObject.getJSONArray("values");

            resulttxt = jsonObject.get("result").toString();//결과값저장

            watchedMoviesNum=jArray.length();
            review_list.clear();
           // JSONObject review_jsonObject = (JSONObject)jArray.get(0); //무비 전체 가져오기
            //JSONObject test =   jArray.getJSONObject(1);


           // JSONObject reviewcontents_jsonObject = (JSONObject)jArray.get(1);
            // 배열의 모든 아이템을 출력합니다.
            for(int i=0; i<jArray.length();i++) {
                JSONObject review_jsonObject = (JSONObject)jArray.get(i); //순서대로 전체 가져오기
                MyReviews myReviews = new MyReviews();
               // String name = review_jsonObject.get("movie").toString();
                JSONArray jsonArray_title = review_jsonObject.getJSONObject("movie").toJSONArray(review_jsonObject.getJSONObject("movie").names());
                String string_title = jsonArray_title.getString(1);

                JSONArray jsonArray_review = review_jsonObject.getJSONObject("review").toJSONArray(review_jsonObject.getJSONObject("review").names());
                String string_rating = jsonArray_review.getString(1);
                int idx = string_rating.indexOf(".");

                String string_review_txt = jsonArray_review.getString(2);
               String string_rating2 = string_rating.substring(0,idx);
                String string_review_date = jsonArray_review.getString(3);

                myReviews.setResult(resulttxt);
                myReviews.setMovieTitle(string_title);
                myReviews.setDate(string_review_date);
                myReviews.setRatingNum(string_rating2);
                myReviews.setReviewTxt(string_review_txt);
                review_list.add(myReviews);

            Log.d("reviewdate","string"+string_rating+"aftersubstring"+string_rating2);

            }

//
//            Log.d("reviewContents", resulttxt);
//            Log.d("reviewContents", "영화 타이틀 1,2,3" + string_title);
//            Log.d("reviewContents", "리뷰내용" + int_rating +string_review_txt+string_review_date);

        } catch (Exception e) {
            Log.v("okhttp33", "error   " + e.toString());

        }

    }


}
