package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.http.SslError;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.webkit.PermissionRequest;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.ui.AppBarConfiguration;

import com.example.myapplication.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;


public class MainActivity extends AppCompatActivity {


    private WebView mWebView;
    BottomNavigationView mBottomNV;
    private FragmentManager fm;
    private FragmentTransaction ft;
    static Button btn_login;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle drawerToggle;
    Toolbar toolbar;
    private View drawerView;
    static String loginId, loginPwd;
    EditText et_id;
    EditText et_pw;
    CheckBox cb_save;
    Button btnlogin;
    SharedPreferences.Editor autoLogin;
    TextView loginText;
    private AppBarConfiguration mAppBarConfiguration;
    // private ActivityMainBinding binding;
    //    Fragment1 fragment1;
//    Fragment2 fragment2;
//    private AppBarConfiguration mAppBarConfiguration;
//    private ActivityMain3Binding binding;
//    Main3Activity main3Activity = new Main3Activity();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        binding = ActivityMainBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
        //    setSupportActionBar(binding.appBarMain3.toolbar);

//        drawerLayout = binding.drawerLayout;
//        navigationView = binding.navView;
//        // Passing each menu ID as a set of Ids because each
//        // menu should be considered as top level destinations.
//        mAppBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
//                .setDrawerLayout(drawerLayout)
//                .build();
//
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerView = (View) findViewById(R.id.drawer);

        btn_login = (Button) findViewById(R.id.btn_signin);
        Button btnMovie = (Button) findViewById(R.id.btnMovie);
        Button btnReserve = (Button) findViewById(R.id.btnReserve);
        Button btnSchedule = (Button) findViewById(R.id.btnSchedule);
        Button btnCuration = (Button) findViewById(R.id.btnCuration);
        Button btnEvent = (Button) findViewById(R.id.btnEvent);
        Button btnCinemaInfo = (Button) findViewById(R.id.btnCinemaInfo);
        Button btn_reserve = (Button) findViewById(R.id.btn_reserve);


        //로그인구현
        et_id = (EditText) findViewById(R.id.inputId);
        et_pw = (EditText) findViewById(R.id.inputPwd);
        cb_save = (CheckBox) findViewById(R.id.check1);
        btnlogin = (Button) findViewById(R.id.btn_signin);
        loginText = (TextView) findViewById(R.id.textviewlogin);

        SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
        autoLogin = auto.edit();
        //처음에는 SharedPreferences에 아무런 정보도 없으므로 값을 저장할 키들을 생성한다.
        // getString의 첫 번째 인자는 저장될 키, 두 번쨰 인자는 값입니다.
        // 첨엔 값이 없으므로 키값은 원하는 것으로 하시고 값을 ""을 줍니다.
        loginId = auto.getString("inputId", "");
        loginPwd = auto.getString("inputPwd", "");


        if(btn_login.getText().equals("로그인")) {

            if ((!loginId.equals("") && !loginPwd.equals(""))) {
                et_id.setVisibility(View.GONE);
                et_pw.setVisibility(View.GONE);
                cb_save.setVisibility(View.GONE);
                btn_login.setText("로그아웃");
                loginText.setText(loginId + "님 환영합니다");
                Toast.makeText(MainActivity.this, loginId + "님 자동로그인완료", Toast.LENGTH_SHORT).show();
            }


            btn_login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    //첫로그인
                    if ((loginId.equals("") && loginPwd.equals(""))) {
                        AutoSinIn();

                    }
                }
            });
        }

        if(btn_login.getText().equals("로그아웃")){

        }

        // navigationMenu(btn_login,"https://kumas.dev/rotte_cinema/login.do");
        navigationMenu(btnMovie, "https://kumas.dev/rotte_cinema/movie.do");
        navigationMenu(btnReserve, "https://kumas.dev/rotte_cinema/ticketing.do");
        navigationMenu(btnSchedule, "https://kumas.dev/rotte_cinema/schedule.do");
        navigationMenu(btnCuration, "https://kumas.dev/rotte_cinema/qration.do");
        navigationMenu(btnEvent, "https://kumas.dev/rotte_cinema/event.do");
        navigationMenu(btnCinemaInfo, "https://kumas.dev/rotte_cinema/about.do");
        navigationMenu(btn_reserve, "https://kumas.dev/rotte_cinema/ticketing.do");


        // 웹뷰 셋팅
        mWebView = (WebView) findViewById(R.id.webView);//xml 자바코드 연결
        mWebView.getSettings().setJavaScriptEnabled(true);//자바스크립트 허용

        mWebView.loadUrl("https://kumas.dev/rotte_cinema");//웹뷰 실행
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


                }
                return true;
            }
        });
//
//        fragment1 = new Fragment1();
//        fragment2 = new Fragment2();
        //setFrag(0); // 첫 프래그먼트 화면 지정


        //  navigationView1.setNavigationItemSelectedListener(this);
        //  navigationView.setNavigationItemSelectedListener(this);
        //  navigationView.setNavigationItemSelectedListener(this);


    }


    void navigationMenu(Button button, String url) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawer(drawerView);
                mWebView.loadUrl(url);
            }
        });
    }

    //  DrawerLayout drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);


    private void setFrag(int n) {
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        switch (n) {
            case 0:
                //ft.replace(R.id.webView,fragment1);
                //    drawerLayout.close();

                mWebView.loadUrl("https://kumas.dev/rotte_cinema");

                break;

            case 1:
                //      drawerLayout.close();

                mWebView.loadUrl("https://kumas.dev/rotte_cinema/schedule.do");

                break;

            case 2:
                //      drawerLayout.close();

//                ft.replace(R.id.webView,fragment2);
                mWebView.loadUrl("https://kumas.dev/rotte_cinema/ticketing.do");

                break;
            case 3:
                //     drawerLayout.close();

                mWebView.loadUrl("https://kumas.dev/rotte_cinema/login.do");

                break;
            case 4:
                drawerLayout.closeDrawer(drawerView);

                drawerLayout.openDrawer(drawerView);

//                 NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main3);
//               // NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
//                NavigationUI.setupWithNavController(mBottomNV,navController);
//                  NavigationUI.setupWithNavController(navigationView, navController);
//                // binding.drawerLayout.closeDrawer(Gravity.RIGHT);
//                // binding.drawerLayout.openDrawer(Gravity.RIGHT);
//               onSupportNavigateUp();
////                main3Activity.onSupportNavigateUp();

                break;


            // Intent intent = new Intent(MainActivity.this,MainActivity2.class);
            //  startActivity(intent);


            // drawerLayout.open();


        }
    }

//            @Override
//            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
//            Log.v("test","test");
//                switch (item.getItemId()){
//                    case R.id.nav_homee:
//                        Toast.makeText(this, "item1 clicked..", Toast.LENGTH_SHORT).show();
//
//
//                        mWebView.loadUrl("https://duzi077.tistory.com/167");
//                        break;
//
//
//                }
//
//                return false;
//            }


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


    public void AutoSinIn() {

        if (TextUtils.isEmpty(et_id.getText()) || TextUtils.isEmpty(et_pw.getText())) {
            //아이디나 암호 둘 중 하나가 비어있으면 토스트메시지를 띄운다
            Toast.makeText(MainActivity.this, "이메일/암호를 입력해주세요", Toast.LENGTH_SHORT).show();
        }


        //아이디 비번이 다 입력 되었을 때, 첫 로그인
        if ((!TextUtils.isEmpty(et_id.getText()) && !TextUtils.isEmpty(et_pw.getText())) && loginId.equals("") && loginPwd.equals("")) {
            et_id.setVisibility(View.GONE);
            et_pw.setVisibility(View.GONE);
            cb_save.setVisibility(View.GONE);
            btn_login.setText("로그아웃");
            loginText.setText(loginId+"님 환영합니다");
            Toast.makeText(MainActivity.this, loginId + "님 자동로그인설정완료", Toast.LENGTH_SHORT).show();

            boolean boo = cb_save.isChecked(); //자동로그인 체크 유무 확인
            if (boo) { //자동로그인 체크 되어 있으면
                //입력한 아이디와 비밀번호를 SharedPreferences.Editor를 통해
                //auto파일의 loginId와 loginPwd에 값을 저장해 줍니다.

                autoLogin.putString("inputId", et_id.getText().toString());
                autoLogin.putString("inputPwd", et_pw.getText().toString());
                autoLogin.putBoolean("SAVE_LOGIN_DATA", cb_save.isChecked()); //현재 체크박스 상태 값 저장
                //꼭 commit()을 해줘야 값이 저장됩니다 ㅎㅎ
                autoLogin.commit();

            }

        }

        //   @Override
//    public boolean onSupportNavigateUp() {
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main3);
//        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
//                || super.onSupportNavigateUp();
//    }


    }
}