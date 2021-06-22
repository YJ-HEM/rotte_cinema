package com.example.myapplication;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.http.SslError;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.PermissionRequest;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.example.myapplication.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import org.jetbrains.annotations.NotNull;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private WebView mWebView;
    BottomNavigationView mBottomNV;
    private FragmentManager fm;
    private FragmentTransaction ft;


    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle drawerToggle;
    Toolbar toolbar;


    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    //    Fragment1 fragment1;
//    Fragment2 fragment2;
//    private AppBarConfiguration mAppBarConfiguration;
//    private ActivityMain3Binding binding;
//    Main3Activity main3Activity = new Main3Activity();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.appBarMain3.toolbar);

        drawerLayout = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setDrawerLayout(drawerLayout)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main3);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

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
                switch (menuItem.getItemId()) {
                    case R.id.tab1:
                        setFrag(0);
                        break;
                    case R.id.tab2:
                        setFrag(1);
                        break;
                    case R.id.tab3:
                        setFrag(2);
                        break;
                    case R.id.tab4:
                        setFrag(3);
                        break;
                    case R.id.tab5:
                        setFrag(4);
                        break;
//                    case R.id.fragment3:
//                        setFrag(5);
//                        break;
                }
                return true;
            }
        });
//
//        fragment1 = new Fragment1();
//        fragment2 = new Fragment2();
        //setFrag(0); // 첫 프래그먼트 화면 지정

//        Button butttt = findViewById(R.id.button100);
//        butttt.setOnClickListener(new Button.OnClickListener() {
//            @Override
//          public void onClick(View view) {
//               // TODO : click event
//                main3Activity.onSupportNavigateUp();
//            }
//        });


    }


    //  DrawerLayout drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main3);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


    private void setFrag(int n) {
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        switch (n) {
            case 0:
                //ft.replace(R.id.webView,fragment1);
                //    drawerLayout.close();

                mWebView.loadUrl("https://lesslate.github.io/android/안드로이드-하단-네비게이션(Bottom-Navigation)-추가하기/");
                break;

            case 1:
                //      drawerLayout.close();

                mWebView.loadUrl("https://linsoo.co.kr/archives/10687");

                break;

            case 2:
                //      drawerLayout.close();

//                ft.replace(R.id.webView,fragment2);
                mWebView.loadUrl("https://www.google.com/search?q=w&oq=w&aqs=chrome..69i60j69i57j69i60j69i61j69i60l2.2911j0j4&sourceid=chrome&ie=UTF-8");

                break;
            case 3:
                //     drawerLayout.close();

                mWebView.loadUrl("https://www.google.com/search?q=%EC%A7%91%EC%97%90%EA%B0%80%EC%9E%90&oq=%EC%A7%91%EC%97%90%EA%B0%80%EC%9E%90&aqs=chrome..69i57.2344j0j1&sourceid=chrome&ie=UTF-8");

                break;
            case 4:
                binding.drawerLayout.closeDrawer(Gravity.LEFT);
                binding.drawerLayout.openDrawer(Gravity.LEFT);
//                onSupportNavigateUp();
//                main3Activity.onSupportNavigateUp();

                break;
            // Intent intent = new Intent(MainActivity.this,MainActivity2.class);
            //  startActivity(intent);


            // drawerLayout.open();

            case 5:

//                drawerLayout.close();

//                break;


        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
        return false;
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
    public void onBackPressed() {


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

    //   @Override
//    public boolean onSupportNavigateUp() {
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main3);
//        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
//                || super.onSupportNavigateUp();
//    }


}