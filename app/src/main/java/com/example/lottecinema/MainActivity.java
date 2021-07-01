package com.example.lottecinema;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
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
import androidx.core.app.NotificationCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    NotificationManager manager;
    NotificationCompat.Builder builder;
    private static String CHANNEL_ID = "channel1";
    private static String CHANEL_NAME = "Channel1";


    private WebView mWebView;
    BottomNavigationView mBottomNV;

    static Button btn_login;

    DrawerLayout drawerLayout;

    private View drawerView;
    static String loginId, loginPwd;
    EditText et_id;
    EditText et_pw;
    CheckBox cb_save;
    Button btnlogin;
    Button btnsignup;
    SharedPreferences.Editor autoLogin;
    TextView loginText;

    SharedPreferences auto;
    boolean autoLoginChecked;
    static WebkitCookieManagerProxy webkitCookieManager = new WebkitCookieManagerProxy();

    //로그인 http연동
    static String post(String url,String Id, String password, String token) throws IOException {

        OkHttpClient client = new OkHttpClient.Builder().cookieJar(webkitCookieManager).build();

        RequestBody formBody = new FormBody.Builder()
                .add("email", Id)
                .add("password", password)
                .add("token",token)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        Response response = client.newCall(request).execute();
        try {
            return response.body().string();
        } catch (Exception e) {
            Log.v("error", "error1" + e.toString());

        }
        return null;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);








        //토큰값가져오기
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("FIREBASE", "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();
                        autoLogin.putString("token", token);
                        //웹 있는 값 읽어오기
                        new Thread() {
                            public void run() {
                                try {
                                    JSONArray jsonObjects = new JSONArray(post("http://kumas.dev/rotte_cinema/loginobject.do","admin@kumas.dev","admin",token));

                                    for (int i = 0; i < jsonObjects.length(); i++) {
                                        Log.v("okhttp3", jsonObjects.get(i).toString());
                                    }
                                } catch (Exception e) {
                                    Log.v("okhttp3", e.toString());
                                }
                            }
                        }.start();

                        // Log and toast
                        //  String msg = getString(R.string.msg_token_fmt, token);
                        Log.d("FIREBASE", token);
                    }
                });






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
        Button btn_myPage = (Button) findViewById(R.id.btnMypage);
        Button btnReview = (Button) findViewById(R.id.btnReview);
        Button btn_signup = (Button) findViewById(R.id.btn_signup);

        //로그인구현
        et_id = (EditText) findViewById(R.id.inputId);
        et_pw = (EditText) findViewById(R.id.inputPwd);
        cb_save = (CheckBox) findViewById(R.id.check1);
        btnlogin = (Button) findViewById(R.id.btn_signin);
        loginText = (TextView) findViewById(R.id.textviewlogin);
        btnsignup = (Button) findViewById(R.id.btn_signup);

        auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
        autoLogin = auto.edit();
        //처음에는 SharedPreferences에 아무런 정보도 없으므로 값을 저장할 키들을 생성한다.
        // getString의 첫 번째 인자는 저장될 키, 두 번쨰 인자는 값입니다.
        // 첨엔 값이 없으므로 키값은 원하는 것으로 하시고 값을 ""을 줍니다.
        loginId = auto.getString("inputId", "");
        loginPwd = auto.getString("inputPwd", "");
        autoLoginChecked = auto.getBoolean("SAVE_LOGIN_DATA", false);

        if (autoLoginChecked) {
            //자동로그인 된 상태로 앱을 켰을 때 - 체크박스의 값이 true로 저장되어있을 때

            et_id.setVisibility(View.GONE);
            et_pw.setVisibility(View.INVISIBLE);
            cb_save.setVisibility(View.INVISIBLE);
            btnsignup.setVisibility(View.INVISIBLE);


            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(260, 00, 0, 0);  // 왼쪽, 위, 오른쪽, 아래 순서입니다.
            btn_login.setLayoutParams(params);


            btn_login.setText("로그아웃");
            loginText.setText(loginId + "님 환영합니다");
            Toast.makeText(MainActivity.this, loginId + "님 자동로그인완료", Toast.LENGTH_SHORT).show();

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
        navigationMenu(btn_reserve, "https://kumas.dev/rotte_cinema/ticketing.do");
        navigationMenu(btn_signup, "https://kumas.dev/rotte_cinema/registration.do");
        navigationMenu(btn_myPage, "https://kumas.dev/rotte_cinema/login.do");
        navigationMenu(btnReview, "https://kumas.dev/rotte_cinema/login.do");


        //로그인성공하면 이거를??
        java.net.CookieHandler.setDefault(webkitCookieManager);



        mWebView = (WebView) findViewById(R.id.webView);//xml 자바코드 연결
        mWebView.getSettings().setJavaScriptEnabled(true);//자바스크립트 허용

        mWebView.loadUrl("https://kumas.dev/rotte_cinema");//웹뷰 실행
        mWebView.setWebViewClient(new WebViewClientClass());//새창열기 없이 웹뷰 내에서 다시 열기//페이지 이동 원활히 하기위해 사용
        mWebView.setWebViewClient(new SslWebViewConnect());
        mWebView.setWebViewClient(new WebViewClient() {
                                     @Override
                                     public void onPageFinished(WebView view, String url) {
                                         if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                                             CookieSyncManager.getInstance().sync();
                                         } else {

                                             CookieManager.getInstance().flush();
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
                        mWebView.loadUrl("https://kumas.dev/rotte_cinema");
                        break;
                    case R.id.tab2:
                        mWebView.loadUrl("https://kumas.dev/rotte_cinema/schedule.do");
                        break;
                    case R.id.tab3:
                        mWebView.loadUrl("https://kumas.dev/rotte_cinema/ticketing.do");
                        break;
                    case R.id.tab4:
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

        if (autoLoginChecked == false && (TextUtils.isEmpty(et_id.getText()) || TextUtils.isEmpty(et_pw.getText()))) {
            //아이디나 암호 둘 중 하나가 비어있으면 토스트메시지를 띄운다
            Toast.makeText(MainActivity.this, "이메일/비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show();
            return;
        }


        //아이디 비번이 다 입력 되었을 때, 첫 로그인
        if ((!TextUtils.isEmpty(et_id.getText()) && !TextUtils.isEmpty(et_pw.getText())) && btn_login.getText().equals("로그인")) {

            boolean boo = cb_save.isChecked(); //자동로그인 체크 유무 확인
            if (boo) { //자동로그인 체크 되어 있으면
                //입력한 아이디와 비밀번호를 SharedPreferences.Editor를 통해
                //auto파일의 loginId와 loginPwd에 값을 저장해 줍니다.
                autoLogin.putString("inputId", et_id.getText().toString());
                autoLogin.putString("inputPwd", et_pw.getText().toString());
                autoLogin.putBoolean("SAVE_LOGIN_DATA", cb_save.isChecked()); //현재 체크박스 상태 값 저장
                //꼭 commit()을 해줘야 값이 저장됩니다 ㅎㅎ
                autoLogin.commit();
                loginId = auto.getString("inputId", "");
                loginPwd = auto.getString("inputPwd", "");
                autoLoginChecked = auto.getBoolean("SAVE_LOGIN_DATA", false);


//                //아이디 비번이 다 입력되면, loginobeject 웹에 아이디비번을 보내서 음..
//                HttpLoginThread httpLoginThread = new HttpLoginThread();
//                httpLoginThread.run(loginId,loginPwd);
//             //   jsonObjects.get(i).toString();



                Toast.makeText(MainActivity.this, loginId + "님 자동로그인설정완료", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, et_id.getText().toString() + "님 자동로그인설정완료", Toast.LENGTH_SHORT).show();
            }

            et_id.setVisibility(View.GONE);
            et_pw.setVisibility(View.INVISIBLE);
            cb_save.setVisibility(View.GONE);
            btnsignup.setVisibility(View.GONE);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(260, 0, 0, 0);  // 왼쪽, 위, 오른쪽, 아래 순서입니다.
            btn_login.setLayoutParams(params);

            btn_login.setText("로그아웃");
            loginText.setText(et_id.getText() + "님 환영합니다");
            Log.v("login", "저장확인" + loginId);
            Log.v("login", "저장확인" + loginPwd);
            Log.v("login", "저장확인" + autoLoginChecked);
            Log.v("login", "버튼텍스트확인" + btn_login.getText());



            return;


        }

        // 로그아웃 버튼 눌렀을 때
        if (btn_login.getText().equals("로그아웃")) {

            //저장된 정보를 지운다.
            autoLogin.clear();
            autoLogin.commit();

            loginId = auto.getString("inputId", "");
            loginPwd = auto.getString("inputPwd", "");
            autoLoginChecked = auto.getBoolean("SAVE_LOGIN_DATA", false);


            et_id.setVisibility(View.VISIBLE);
            et_pw.setVisibility(View.VISIBLE);
            cb_save.setVisibility(View.VISIBLE);
            btnsignup.setVisibility(View.VISIBLE);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(100, 00, 0, 0);  // 왼쪽, 위, 오른쪽, 아래 순서입니다.
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

}

//class HttpLoginThread extends Thread{
//    public void run(String httpId, String httpPassword) {
//        try {
//            JSONArray jsonObjects = new JSONArray(MainActivity.post("http://kumas.dev/rotte_cinema/loginobject.do",httpId,httpPassword));
//
//            for (int i = 0; i < jsonObjects.length(); i++) {
//                Log.v("okhttp3", jsonObjects.get(i).toString());
//            }
//        } catch (Exception e) {
//            Log.v("okhttp3", e.toString());
//        }
//    }
//}


