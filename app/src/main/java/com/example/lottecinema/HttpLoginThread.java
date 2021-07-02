package com.example.lottecinema;

import android.util.Log;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpLoginThread extends Thread{

   static String post(String url, String HttpId, String HttpPW, String token) throws IOException {

        //앱 쿠키jar에 쿠키를 저장한다 (아마?)
//        client = new OkHttpClient.Builder().cookieJar(webkitCookieManager).build();
        OkHttpClient client = new OkHttpClient.Builder().cookieJar(MainActivity.cookieJar).build();
        RequestBody formBody = new FormBody.Builder()
                .add("email", HttpId)
                .add("password", HttpPW)
                .add("token", token)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        Response response = client.newCall(request).execute();


        return response.body().string();
    }
    Login login;



    public HttpLoginThread(Login login){
        this.login=login;
        Log.v("okhttp3","lo");

    }
    public void run() {
        try {

           JSONObject jsonObject = new JSONObject( post("http://kumas.dev/rotte_cinema/loginobject.do",login.getID(), login.getPW(), login.getToken()));
            login.setName(jsonObject.get("name").toString());
            login.setLoginResult(jsonObject.get("result").toString());
            Log.v("okhttp3", login.getLoginResult() +login.getID() +login.getPW() );
        } catch (Exception e) {
            Log.v("okhttp3", "error   " + e.toString());
        }

    }

}

