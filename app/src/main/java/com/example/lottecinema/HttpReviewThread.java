package com.example.lottecinema;

import android.graphics.Bitmap;
import android.util.Log;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
public class HttpReviewThread extends Thread{
  static public String Bitmap;

    static String post(String url) throws IOException {

        //앱 쿠키jar에 쿠키를 저장한다 (아마?)
//        client = new OkHttpClient.Builder().cookieJar(webkitCookieManager).build();
        OkHttpClient client = new OkHttpClient.Builder().cookieJar(MainActivity.cookieJar).build();
        RequestBody formBody = new FormBody.Builder()
                .build();

        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = client.newCall(request).execute();


        return response.body().string();
    }




    public HttpReviewThread(){
        Log.v("okhttp33","review");

    }
    public void run() {
        try {

            JSONObject jsonObject = new JSONObject( post("https://kumas.dev/rotte_cinema/imageobject.do"));
             Bitmap = jsonObject.get("image").toString();

            Log.v("okhttp33", Bitmap );
        } catch (Exception e) {
            Log.v("okhttp33", "error   " + e.toString());
        }

    }

}