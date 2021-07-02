package com.example.lottecinema;

import android.util.Log;

import org.json.JSONObject;

public class HttpLoginThread extends Thread{



    Login login;




    public HttpLoginThread(Login login){
        this.login=login;
        Log.v("okhttp3","lo");

    }
    public void run() {
        try {

            JSONObject jsonObject = new JSONObject(MainActivity.post("http://kumas.dev/rotte_cinema/loginobject.do", login.getID(),login.getPW(),login.getToken()));
            login.setName(jsonObject.get("name").toString());
            login.setLoginResult(jsonObject.get("result").toString());
            Log.v("okhttp3", login.getLoginResult() +login.getID() +login.getPW() );
        } catch (Exception e) {
            Log.v("okhttp3", "error   " + e.toString());
        }

    }

}