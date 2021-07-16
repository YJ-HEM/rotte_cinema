package com.example.lottecinema;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.PermissionRequest;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.NotificationCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

public class MainActivity extends AppCompatActivity {

    static String token;
    public static Context context_main;

    NotificationManager manager;
    NotificationCompat.Builder builder;
    private static String CHANNEL_ID = "channel1";
    private static String CHANEL_NAME = "Channel1";
    static SharedPreferences auto;
    static SharedPreferences.Editor autoLogin;

    private WebView mWebView;
    BottomNavigationView mBottomNV;

    static Button btn_login;

    DrawerLayout drawerLayout;

    private View drawerView;
    static String loginId, loginPwd, storedUserName;
    EditText et_id;
    EditText et_pw;
    CheckBox cb_save;
    Button btnlogin;
    Button btnsignup;
    TextView loginText;
    Button btnQRcode;

    boolean autoLoginChecked;
     WebkitCookieManagerProxy webkitCookieManager = new WebkitCookieManagerProxy();
    HttpLoginThread httpLoginThread;
    HttpReviewThread httpReviewThread;
    public static CookieJar cookieJar = null;
    public static String URL = "http://kumas.dev/rotte_cinema/";
    static Login login = new Login();
    Fragment fragment = new watchedMovies();
    Fragment qrFragment = new QRcode();




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context_main=this;
        //쿠키 동기화용 CookieJar 생성 : 적절한 초기화 위치
        cookieJar = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(this));
        auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
        autoLogin = auto.edit();


        httpLoginThread = new HttpLoginThread(login);


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
        //Button btn_myPage = (Button) findViewById(R.id.btnMypage);
        btnQRcode = (Button) findViewById(R.id.btnQRcode);
        Button btnReview = (Button) findViewById(R.id.btnReview);
        Button btn_signup = (Button) findViewById(R.id.btn_signup);
        CoordinatorLayout forWatchedMovies  =  (CoordinatorLayout) findViewById(R.id.forWatchedMovies);

        //로그인구현
        et_id = (EditText) findViewById(R.id.inputId);
        et_pw = (EditText) findViewById(R.id.inputPwd);
        cb_save = (CheckBox) findViewById(R.id.check1);
        btnlogin = (Button) findViewById(R.id.btn_signin);
        loginText = (TextView) findViewById(R.id.textviewlogin);
        btnsignup = (Button) findViewById(R.id.btn_signup);


        //처음에는 SharedPreferences에 아무런 정보도 없으므로 값을 저장할 키들을 생성한다.
        // getString의 첫 번째 인자는 저장될 키, 두 번쨰 인자는 값입니다.
        // 첨엔 값이 없으므로 키값은 원하는 것으로 하시고 값을 ""을 줍니다.
        loginId = auto.getString("inputId", "");
        loginPwd = auto.getString("inputPwd", "");
        storedUserName = auto.getString("userName", "");
        autoLoginChecked = auto.getBoolean("SAVE_LOGIN_DATA", false);
        token = auto.getString("token","");

        if (autoLoginChecked) {
            //자동로그인 된 상태로 앱을 켰을 때 - 체크박스의 값이 true로 저장되어있을 때


            login.setID(loginId);
            login.setPW(loginPwd);
            login.setToken(token);


            if(httpLoginThread.isAlive()){
                httpLoginThread.interrupt();
            }
            httpLoginThread = new HttpLoginThread(login);
            httpLoginThread.start();
            //httpLoginThread쓰레드 완료시까지 메인쓰레드 대기
            try {
                httpLoginThread.join();
            } catch (Exception e) {
            }
            ;
            //만약 result가 connect이면
            //자동로그인완료 토스트 띄우기

            if (login.getLoginResult().equals("connect")) {

                Toast.makeText(MainActivity.this, storedUserName + "님 자동로그인완료", Toast.LENGTH_SHORT).show();
            }

            et_id.setVisibility(View.GONE);
            et_pw.setVisibility(View.INVISIBLE);
            cb_save.setVisibility(View.INVISIBLE);
            btnsignup.setVisibility(View.INVISIBLE);


            //버튼 위치 조정
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(250, 00, 0, 0);  // 왼쪽, 위, 오른쪽, 아래 순서입니다.
            btn_login.setLayoutParams(params);


            btn_login.setText("로그아웃");
            loginText.setText(storedUserName + "님 환영합니다");
            Toast.makeText(MainActivity.this, storedUserName + "님 자동로그인완료", Toast.LENGTH_SHORT).show();
        }


        //첫로그인
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginId = auto.getString("inputId", "");
                loginPwd = auto.getString("inputPwd", "");
                autoLoginChecked = auto.getBoolean("SAVE_LOGIN_DATA", false);

                AutoSinIn();

            }
        });


        long now = System.currentTimeMillis();
        Date mDate = new Date(now);
        SimpleDateFormat simpleDate = new SimpleDateFormat("MM-dd");
        String getTime = simpleDate.format(mDate);


        //notification 설정
        //오늘날짜가 며칠이면 알람뜨게하기
        if (getTime.equals("06-28")) {
            showNoti();
        }


        // navigationMenu(btn_login,"https://kumas.dev/rotte_cinema/login.do");
        navigationMenu(btnMovie, "https://kumas.dev/rotte_cinema/movie.do");
        navigationMenu(btnReserve, "https://kumas.dev/rotte_cinema/ticketing.do");
        navigationMenu(btnSchedule, "https://kumas.dev/rotte_cinema/schedule.do");
        navigationMenu(btnCuration, "https://kumas.dev/rotte_cinema/qration.do");
        navigationMenu(btnEvent, "https://kumas.dev/rotte_cinema/event.do");
        navigationMenu(btnCinemaInfo, "https://kumas.dev/rotte_cinema/about.do");
        navigationMenu(btn_reserve, "https://kumas.dev/rotte_cinema/schedule.do");
        navigationMenu(btn_signup, "https://kumas.dev/rotte_cinema/registration.do");
        //navigationMenu(btn_myPage, "https://kumas.dev/rotte_cinema/login.do");


        //리뷰버튼 클릭 시 리뷰 fragment로
        btnReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), Review.class);
//                startActivity(intent);
                drawerLayout.closeDrawer(drawerView);

                loginId = auto.getString("inputId", "");


                if (login.getLoginResult()!=null && login.getLoginResult().equals("connect")) {
                    httpReviewThread = new HttpReviewThread();
                    if(httpReviewThread.isAlive()){
                        httpReviewThread.interrupt();
                    }
                    httpReviewThread = new HttpReviewThread();
                    httpReviewThread.start();
                    //httpReviewThread 완료시까지 메인쓰레드 대기
                    try {
                        httpReviewThread.join();
                    } catch (Exception e) {
                    }




                    FragmentManager fm = getSupportFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.replace(R.id.webView, fragment);
                    ft.commit();
                }
                else if(login.getLoginResult()==null){
                    drawerLayout.closeDrawer(drawerView);

                    mWebView.loadUrl("https://kumas.dev/rotte_cinema/login.do");

                }
            }
        });

        //qr코드버튼 클릭시
        btnQRcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawer(drawerView);

                loginId = auto.getString("inputId", "");


                if (login.getLoginResult()!=null &&login.getLoginResult().equals("connect")) {
                    FragmentManager fm = getSupportFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.replace(R.id.webView, qrFragment);
                    ft.commit();
                    }


                else if(login.getLoginResult()==null){
                    drawerLayout.closeDrawer(drawerView);

                    mWebView.loadUrl("https://kumas.dev/rotte_cinema/login.do");

                }
            }
        });


//        CookieSyncManager.createInstance(this);
//        CookieManager.getInstance().removeAllCookie();
//        CookieSyncManager.getInstance().startSync();
//
//        android.webkit.CookieSyncManager.createInstance(this);
//        // unrelated, just make sure cookies are generally allowed
//        android.webkit.CookieManager.getInstance().setAcceptCookie(true);
//
//        //웹에 앱의 쿠키매니저 연동하기
//        java.net.CookieHandler.setDefault(webkitCookieManager);


        mWebView = (WebView) findViewById(R.id.webView);//xml 자바코드 연결
        mWebView.getSettings().setJavaScriptEnabled(true);//자바스크립트 허용

        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);



        mWebView.loadUrl("https://kumas.dev/rotte_cinema");//웹뷰 실행
        mWebView.setWebViewClient(new WebViewClientClass());//새창열기 없이 웹뷰 내에서 다시 열기//페이지 이동 원활히 하기위해 사용
        mWebView.setWebViewClient(new SslWebViewConnect());
        //웹과 앱 쿠키값 싱크
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
//                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
//                    CookieSyncManager.getInstance().sync();
//                } else {
//                    CookieManager.getInstance().flush();
//                }
                CookieManager cookieManager = CookieManager.getInstance();
                cookieManager.setAcceptCookie(true);
                cookieManager.setAcceptThirdPartyCookies(mWebView, true);
                cookieManager.removeAllCookies(null);
                cookieManager.flush();

                //CookieJar로 저장된 쿠키 읽어와서 주입시켜줌
                List<Cookie> cookies = cookieJar.loadForRequest(HttpUrl.parse(URL));
                if (cookies != null) {
                    for (Cookie cookie : cookies) {
                        //특정 조건을 두어서 그에 맞는 것만 주입
                        //아래는 JSP 톰캣의 세션을 위한 쿠키를 찾아서 넣는 것임("JSESSIONID")
                        //참고로 PHP에서의 세션 이름은 "PHPSESSID"
                        //(아래 조건을 없애면 모든 쿠키가 동기화 됨)
                        if (cookie.name().toLowerCase().contains("session") || cookie.name().toLowerCase().contains("sessid")){
                            String cookieString = cookie.name() + "=" + cookie.value() + ";";
                            cookieManager.setCookie(cookie.domain(), cookieString);
                        }
                    }
                }
            }

        });
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
                        removeFragment();
                        mWebView.loadUrl("https://kumas.dev/rotte_cinema");
                        break;
                    case R.id.tab2:
                        removeFragment();

                        mWebView.loadUrl("https://kumas.dev/rotte_cinema/schedule.do");
                        break;
                    case R.id.tab3:
                        removeFragment();

                        mWebView.loadUrl("https://kumas.dev/rotte_cinema/ticketing.do");
                        break;
                    case R.id.tab4:
                        removeFragment();

                        mWebView.loadUrl("https://kumas.dev/rotte_cinema/login.do");
                        break;
                    case R.id.tab5:

                        drawerLayout.closeDrawer(drawerView);

                        drawerLayout.openDrawer(drawerView);
                        break;
                }
                return true;
            }
        });




    }

    void removeFragment(){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.remove(fragment);
        ft.remove(qrFragment);
        ft.commit();
    }

    void showNoti() {
        builder = null;
        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        //버전 오레오 이상일 경우
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            manager.createNotificationChannel(new NotificationChannel(CHANNEL_ID, CHANEL_NAME, NotificationManager.IMPORTANCE_DEFAULT));
            builder = new NotificationCompat.Builder(this, CHANNEL_ID);
            //하위 버전일 경우
        } else {
            builder = new NotificationCompat.Builder(this);
        }
        Intent intent = new Intent(this, MainActivity.class);
        //intent.putExtra("name",name);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 101, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        //알림창 제목
        builder.setContentTitle("알림");
        //알림창 메시지
        builder.setContentText("알림 메시지");
        //알림창 아이콘
        builder.setSmallIcon(R.drawable.ic_logo_r);
        //알림창 터치시 상단 알림상태창에서 알림이 자동으로 삭제되게 합니다.
        builder.setAutoCancel(true);
        //pendingIntent를 builder에 설정 해줍니다. //알림창 터치시 인텐트가 전달할 수 있도록 해줍니다.
        builder.setContentIntent(pendingIntent);
        Notification notification = builder.build();
        //알림창 실행
        manager.notify(1, notification);
    }


    void navigationMenu(Button button, String url) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeFragment();
                drawerLayout.closeDrawer(drawerView);
                mWebView.loadUrl(url);
            }
        });
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


    public void AutoSinIn() {
        login.setID(et_id.getText().toString());
        login.setPW(et_pw.getText().toString());
        login.setToken(token);

        if (autoLoginChecked == false && (TextUtils.isEmpty(et_id.getText()) || TextUtils.isEmpty(et_pw.getText()))) {
            //아이디나 암호 둘 중 하나가 비어있으면 토스트메시지를 띄운다
            Toast.makeText(MainActivity.this, "이메일/비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show();
            return;
        }


        //아이디 비번이 다 입력 되었을 때, 첫 로그인
        if ((!TextUtils.isEmpty(et_id.getText()) && !TextUtils.isEmpty(et_pw.getText())) && btn_login.getText().equals("로그인")) {

            boolean boo = cb_save.isChecked(); //자동로그인 체크 유무 확인
            if (boo) { //자동로그인 체크 되어 있으면
                //체크박스 체크여부를 SharedPreferences.Editor를 통해 값을 저장해 줍니다.
                autoLogin.putBoolean("SAVE_LOGIN_DATA", cb_save.isChecked()); //현재 체크박스 상태 값 저장
                autoLogin.commit();
                autoLoginChecked = auto.getBoolean("SAVE_LOGIN_DATA", false);


                String loginResult;
                //아이디 비번이 다 입력되면, loginobeject 웹에 아이디비번토큰을 보낸다
                //http에 데이터(토큰,아이디,비밀번호) 보내고 성공/실패 값 읽어오기
                if(httpLoginThread.isAlive()){
                    httpLoginThread.interrupt();
                }
                httpLoginThread = new HttpLoginThread(login);
                httpLoginThread.start();


                //httpLoginThread쓰레드 완료시까지 메인쓰레드 대기
                try {
                    httpLoginThread.join();
                } catch (Exception e) {
                }
                ;

                //만약 result가 connect이면
                //입력한 아이디와 비밀번호를 SharedPreferences.Editor를 통해
                //auto파일의 loginId와 loginPwd, storedUserName에 값을 저장해 줍니다.

                if (login.getLoginResult().equals("connect")) {
                    autoLogin.putString("inputId", et_id.getText().toString());
                    autoLogin.putString("inputPwd", et_pw.getText().toString());
                    autoLogin.putString("userName", login.getName());
                    autoLogin.commit();
                    loginId = auto.getString("inputId", "");
                    loginPwd = auto.getString("inputPwd", "");
                    storedUserName = auto.getString("userName", "");

                    Toast.makeText(MainActivity.this, storedUserName + "님 자동로그인설정완료", Toast.LENGTH_SHORT).show();
                }

                //만약 result가 fail이면
                else {
                    Toast.makeText(MainActivity.this, "이메일/비밀번호를 다시 확인해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }

            }//자동로그인에 체크되어 있지 않다면
            else {
                //아이디 비번이 다 입력되면, loginobeject 웹에 아이디비번토큰을 보낸다
                //http에 데이터(토큰,아이디,비밀번호) 보내고 성공/실패 값 읽어오기
                if(httpLoginThread.isAlive()){
                    httpLoginThread.interrupt();
                }
                httpLoginThread = new HttpLoginThread(login);
                httpLoginThread.start();
                //httpLoginThread쓰레드 완료시까지 메인쓰레드 대기
                try {
                    httpLoginThread.join();
                } catch (Exception e) {
                }
                ;
                //만약 result가 connect이면 토스트를 띄워줍니다. sharedpreference에는 저장x
                if (login.getLoginResult().equals("connect")) {
                    Toast.makeText(MainActivity.this, login.getName() + "님 자동로그인설정완료", Toast.LENGTH_SHORT).show();
                }
                //만약 result가 fail이면
                else {
                    Toast.makeText(MainActivity.this, "이메일/비밀번호를 다시 확인해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }

            }

            Log.v("okhttp3", login.getLoginResult() + login.getName());
            et_id.setVisibility(View.GONE);
            et_pw.setVisibility(View.INVISIBLE);
            cb_save.setVisibility(View.GONE);
            btnsignup.setVisibility(View.GONE);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 0, 0, 0);  // 왼쪽, 위, 오른쪽, 아래 순서입니다.
            btn_login.setLayoutParams(params);

            btn_login.setText("로그아웃");
            loginText.setText(login.getName() + "님 환영합니다");
            Log.v("login", "저장확인" + loginId);
            Log.v("login", "저장확인" + loginPwd);
            Log.v("login", "저장확인" + autoLoginChecked);
            Log.v("login", "버튼텍스트확인" + btn_login.getText());


            return;


        }

        // 로그아웃 버튼 눌렀을 때 - http에 토큰값을 넣어서 보낸다. 그러면 웹쪽에서 세션을 끊어주고, 앱.웹에서 다 로그아웃이 된다.
        if (btn_login.getText().equals("로그아웃")) {
            login.setID("null");
            login.setPW("null");
            Log.d("okhttp", ""+login.getID() + login.getPW() +token);
            if(httpLoginThread.isAlive()){
                httpLoginThread.interrupt();
            }
            httpLoginThread = new HttpLoginThread(login);
            httpLoginThread.start();

            //httpLoginThread쓰레드 완료시까지 메인쓰레드 대기
            try {
                httpLoginThread.join();
            } catch (Exception e) {
            }
            ;
            //저장된 정보를 지운다.(아이디,비밀번호, 이름, 자동로그인체크여부 만) 토큰은 앱이 삭제되지 않는이상 새로생성되지 않으니 계속저장해둔다.
            autoLogin.remove("inputID");
            autoLogin.remove("inputPwd");
            autoLogin.remove("userName");
            autoLogin.remove("SAVE_LOGIN_DATA");
            autoLogin.commit();

            loginId = auto.getString("inputId", "");
            loginPwd = auto.getString("inputPwd", "");
            storedUserName = auto.getString("userName", "");
            autoLoginChecked = auto.getBoolean("SAVE_LOGIN_DATA", false);


            et_id.setVisibility(View.VISIBLE);
            et_pw.setVisibility(View.VISIBLE);
            cb_save.setVisibility(View.VISIBLE);
            btnsignup.setVisibility(View.VISIBLE);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(00, 00, 0, 0);  // 왼쪽, 위, 오른쪽, 아래 순서입니다.
            btn_login.setLayoutParams(params);

            btn_login.setText("로그인");
            loginText.setText("로그인 하시고 다양한 혜택을 확인하세요");
            Toast.makeText(MainActivity.this, "로그아웃", Toast.LENGTH_SHORT).show();
            Log.v("login", "로그아웃시저장확인" + loginId);
            Log.v("login", "로그아웃시저장확인" + loginPwd);
            Log.v("login", "로그아웃시저장확인" + autoLoginChecked);
            Log.v("login", "버튼텍스트확인" + btn_login.getText());
            return;


        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.v("test","test");
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            CookieSyncManager.getInstance().startSync();
        }else {
            CookieManager.getInstance().flush();}

    }

    @Override
    protected void onPause() {
        super.onPause();

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            CookieSyncManager.getInstance().stopSync();
        }else {
            CookieManager.getInstance().flush();}
    }


}




