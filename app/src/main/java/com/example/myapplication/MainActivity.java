package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.net.http.SslError;
import android.os.Bundle;
import android.webkit.PermissionRequest;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;


import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity {
    private WebView mWebView;
    BottomNavigationView mBottomNV;

    Fragment1 fragment1;
    Fragment2 fragment2;
    Fragment3 fragment3;
//수정
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 웹뷰 셋팅
        mWebView = (WebView) findViewById(R.id.webView);//xml 자바코드 연결
        mWebView.getSettings().setJavaScriptEnabled(true);//자바스크립트 허용

        mWebView.loadUrl("https://commin.tistory.com/63");//웹뷰 실행
        mWebView.setWebViewClient(new WebViewClientClass());//새창열기 없이 웹뷰 내에서 다시 열기//페이지 이동 원활히 하기위해 사용
        mWebView.setWebViewClient(new SslWebViewConnect());
        //ssl 인증이 없는 경우 해결을 위한 부분
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onPermissionRequest(final PermissionRequest request) {
                request.grant(request.getResources());
            }
        });


        mBottomNV = findViewById(R.id.bottom);
        mBottomNV.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
          @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) { //menu_bottom.xml에서 지정해줬던 아이디 값을 받아와서 각 아이디값마다 다른 이벤트를 발생시킵니다.
                    case R.id.tab1:{ getSupportFragmentManager().beginTransaction() .replace(R.id.webView,fragment1).commitAllowingStateLoss();
                    break;}
                    case R.id.tab2:{ getSupportFragmentManager().beginTransaction() .replace(R.id.webView,fragment2).commitAllowingStateLoss();break; }
                    case R.id.tab3:{ getSupportFragmentManager().beginTransaction() .replace(R.id.webView,fragment3).commitAllowingStateLoss(); break   ; }
                     }
              return true;
          } });


                }


                public class SslWebViewConnect extends WebViewClient {

                    @Override
                    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                        handler.proceed(); // SSL 에러가 발생해도 계속 진행!
                    }

                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        view.loadUrl(url);
                        return true;//응용프로그램이 직접 url를 처리함
                    }
                }


                private class WebViewClientClass extends WebViewClient {//페이지 이동

                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        view.loadUrl(url);
                        return true;
                    }


                }


                //webView 뒤로가기버튼 - 더이상 뒤로 갈 페이지가 없을 때 2초이내에 뒤로가기를 누르면 앱 종료
                private long backBtnTime = 0;


                @Override
                public void onBackPressed () {


                    long curTime = System.currentTimeMillis();
                    long gapTime = curTime - backBtnTime;
                    if (mWebView.canGoBack()) {
                        mWebView.goBack();
                    } else if (0 <= gapTime && 2000 >= gapTime) super.onBackPressed();

                    else {
                        backBtnTime = curTime;
                        Toast.makeText(getApplicationContext(), "한번 더 누르면 앱이 종료됩니다.", Toast.LENGTH_SHORT).show();
                    }


                }


            }
