package com.example.lottecinema;

import android.graphics.Bitmap;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpReviewThread extends Thread {
    static public JsonObject result;

   static public int watchedMoviesNum;
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


    public HttpReviewThread() {
        Log.v("okhttp33", "review");

    }

    public void run() {
        try {
            // 가장 큰 JSONObject를 가져옵니다.
            JSONObject jsonObject = new JSONObject(post("http://kumas.dev/rotte_cinema/reservsobject.do"));

            //  배열을 가져옵니다.
            JSONArray jArray = jsonObject.getJSONArray("reservs");

            watchedMoviesNum=jArray.length();

            // 배열의 모든 아이템을 출력합니다.
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject obj = jArray.getJSONObject(i);
                MyMovies myMovies = new MyMovies();
                myMovies.setMovie(obj.getString("movie"));
                myMovies.setAge(obj.getString("age"));
                myMovies.setCinema(obj.getString("cinema") + obj.getString("theater"));
                myMovies.setCustomer(obj.getString("customer"));
                myMovies.setPoster(obj.getString("poster"));
                myMovies.setSeat(obj.getString("seat"));
                myMovies.setDate(obj.getString("date"));
            }


        } catch (Exception e) {
            Log.v("okhttp33", "error   " + e.toString());
        }

    }


}