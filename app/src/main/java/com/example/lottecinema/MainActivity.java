package com.example.lottecinema;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.http.SslError;
import android.os.AsyncTask;
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
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.ui.AppBarConfiguration;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    public static CookieJar cookieJar = null;

    NotificationManager manager;
    NotificationCompat.Builder builder;
    private static String CHANNEL_ID = "channel1";
    private static String CHANEL_NAME = "Channel1";


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
    Button btnsignup;
    SharedPreferences.Editor autoLogin;
    TextView loginText;
    private AppBarConfiguration mAppBarConfiguration;
    private EditText etMessage;
    SharedPreferences auto;
    OkHttpClient client = new OkHttpClient();
    boolean autoLoginChecked;

    String post(String url) throws IOException {
        RequestBody formBody = new FormBody.Builder()
                .add("email", "admin@kumas.dev")
                .add("password", "admin")
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        Response response = client.newCall(request).execute();
        try {
            return new JSONObject(response.body().string()).getString("");
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

                        // Log and toast
                        //  String msg = getString(R.string.msg_token_fmt, token);
                        Log.d("FIREBASE", token);
                    }
                });


        //서버에 있는 값 읽어오기
        new Thread() {
            public void run() {
                try {
                    JSONArray jsonObjects = new JSONArray(post("http://kumas.dev/rotte_cinema/loginobject.do"));
                    for (int i = 0; i < jsonObjects.length(); i++) {
                        Log.v("error", jsonObjects.get(i).toString());
                    }
                } catch (Exception e) {
                    Log.v("error", e.toString());
                }
            }
        }.start();


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


        // 웹뷰 셋팅
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

            CustomTask task = new CustomTask();
            //execute의 매개값은
            //sendMsg = "id="+strings[0]+"&pwd="+strings[1];
            //doInBackround에서 사용된 문자열 배열에 필요한 값을 넣습니다.
            //task.execute(loginId,loginPwd);
            // 그리고 jsp로 부터 리턴값을 받아야할 때는
            try {
                String returns = task.execute(loginId, loginPwd).get();
                Log.v("connectlog", returns);
            } catch (Exception e) {
                Log.v("connectlog", "catch" + e.toString());

            }
            //처럼 사용하시면 되는데요. get()에서 에러가 발생할 수 있어서 try catch문으로
            //감싸야에러가 나지 않습니다.

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

    HttpURLConnection conn;
    HttpURLConnection conn2;

    class CustomTask extends AsyncTask<String, Void, String> {
        String sendMsg, receiveMsg;

        @Override
        // doInBackground의 매개값이 문자열 배열인데요. 보낼 값이 여러개일 경우를 위해 배열로 합니다.
        protected String doInBackground(String... strings) {
            try {
                String str;
                URL url = new URL("http://kumas.dev/rotte_cinema/loginobject.do");//보낼 jsp 주소를 ""안에 작성합니다.
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");//데이터를 POST 방식으로 전송합니다.
                conn.setDefaultUseCaches(false);
                conn.setUseCaches(false);
                conn.setDoInput(true);
                conn.setDoOutput(true);
                setCookieHeader();
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
//                osw.flush();
//                osw.close();
                sendMsg = "email=" + strings[0] + "&password=" + strings[1];//보낼 정보인데요. GET방식으로 작성합니다. ex) "id=rain483&pwd=1234";
                //회원가입처럼 보낼 데이터가 여러 개일 경우 &로 구분하여 작성합니다.
                osw.write(sendMsg);//OutputStreamWriter에 담아 전송합니다.
                osw.flush();
                osw.close();
                //jsp와 통신이 정상적으로 되었을 때 할 코드들입니다.
                if (conn.getResponseCode() == conn.HTTP_OK) {
                    InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                    BufferedReader reader = new BufferedReader(tmp);
                    StringBuffer buffer = new StringBuffer();
                    //jsp에서 보낸 값을 받겠죠?
                    while ((str = reader.readLine()) != null) {
                        buffer.append(str);
                    }
                    receiveMsg = buffer.toString();
                    getCookieHeader();
                } else {
                    Log.i("connectlog", conn.getResponseCode() + "에러1");
                    // 통신이 실패했을 때 실패한 이유를 알기 위해 로그를 찍습니다.
                }

            } catch (MalformedURLException e) {

                Log.i("connectlog", e.toString() + "에러2");

            } catch (IOException e) {
                Log.i("connectlog", e.toString() + "에러3");

            }
            //jsp로부터 받은 리턴 값입니다.
            return receiveMsg;
        }

        private void getCookieHeader() {//Set-Cookie에 배열로 돼있는 쿠키들을 스트링 한줄로 변환
            List<String> cookies = conn.getHeaderFields().get("Set-Cookie");
            //cookies -> [JSESSIONID=D3F829CE262BC65853F851F6549C7F3E; Path=/smartudy; HttpOnly] -> []가 쿠키1개임.
            //Path -> 쿠키가 유효한 경로 ,/smartudy의 하위 경로에 위의 쿠키를 사용 가능.
            if (cookies != null) {
                for (String cookie : cookies) {
                    String sessionid = cookie.split(";\\s*")[0];
                    //JSESSIONID=FB42C80FC3428ABBEF185C24DBBF6C40를 얻음.
                    //세션아이디가 포함된 쿠키를 얻었음.
                    setSessionIdInSharedPref(sessionid);
                    CookieManager.getInstance().setCookie("http://kumas.dev/rotte_cinema", sessionid);

                }
            }
            return;
        }

        private void setSessionIdInSharedPref(String sessionid) {
            SharedPreferences pref = getSharedPreferences("sessionCookie", Context.MODE_PRIVATE);
            SharedPreferences.Editor edit = pref.edit();
            if (pref.getString("sessionid", null) == null) { //처음 로그인하여 세션아이디를 받은 경우
                Log.d("LOG", "처음 로그인하여 세션 아이디를 pref에 넣었습니다." + sessionid);
            } else if (!pref.getString("sessionid", null).equals(sessionid)) { //서버의 세션 아이디 만료 후 갱신된 아이디가 수신된경우
                Log.d("LOG", "기존의 세션 아이디" + pref.getString("sessionid", null) + "가 만료 되어서 "
                        + "서버의 세션 아이디 " + sessionid + " 로 교체 되었습니다.");
            }
            edit.putString("sessionid", sessionid);
            edit.apply(); //비동기 처리
        }


        private void setCookieHeader() {
            SharedPreferences pref = getSharedPreferences("sessionCookie", Context.MODE_PRIVATE);
            String sessionid = pref.getString("sessionid", null);
            if (sessionid != null) {
                Log.d("LOG", "세션 아이디" + sessionid + "가 요청 헤더에 포함 되었습니다.");
                try {
                    URL url = new URL("http://kumas.dev/rotte_cinema/login.do");//보낼 jsp 주소를 ""안에 작성합니다.
                    conn2 = (HttpURLConnection) url.openConnection();
                    conn2.setRequestProperty("Cookie", sessionid);
                } catch (Exception e) {
                }

            }

//        class CustomTask2 extends AsyncTask<String, Void, String> {
//            String sendMsg, receiveMsg;
//
//            @Override
//            // doInBackground의 매개값이 문자열 배열인데요. 보낼 값이 여러개일 경우를 위해 배열로 합니다.
//            protected String doInBackground(String... strings) {
//                try {
//                    String str;
//                    URL url = new URL("http://kumas.dev/rotte_cinema/login.do");//보낼 jsp 주소를 ""안에 작성합니다.
//                    conn2 = (HttpURLConnection) url.openConnection();
//                    conn2.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//                    conn2.setRequestMethod("POST");//데이터를 POST 방식으로 전송합니다.
//                    conn2.setDefaultUseCaches(false);
//                    conn2.setUseCaches(false);
//                    conn2.setDoInput(true);
//                    conn2.setDoOutput(true);
//                    setCookieHeader();
//               //     OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
////                osw.flush();
////                osw.close();
//                   // sendMsg = "email=" + strings[0] + "&password=" + strings[1];//보낼 정보인데요. GET방식으로 작성합니다. ex) "id=rain483&pwd=1234";
//                    //회원가입처럼 보낼 데이터가 여러 개일 경우 &로 구분하여 작성합니다.
////                    osw.write(sendMsg);//OutputStreamWriter에 담아 전송합니다.
////                    osw.flush();
////                    osw.close();
//                    //jsp와 통신이 정상적으로 되었을 때 할 코드들입니다.
//                    if (conn.getResponseCode() == conn.HTTP_OK) {
//                        Log.i("connectlog", "아마쿠키들어감?");
//
////                        InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
////                        BufferedReader reader = new BufferedReader(tmp);
////                        StringBuffer buffer = new StringBuffer();
////                        //jsp에서 보낸 값을 받겠죠?
////                        while ((str = reader.readLine()) != null) {
////                            buffer.append(str);
////                        }
////                        receiveMsg = buffer.toString();
//                    //    getCookieHeader();
//                    } else {
//                        Log.i("connectlog", conn.getResponseCode() + "에러1");
//                        // 통신이 실패했을 때 실패한 이유를 알기 위해 로그를 찍습니다.
//                    }
//
//                } catch (MalformedURLException e) {
//
//                    Log.i("connectlog", e.toString() + "에러2");
//
//                } catch (IOException e) {
//                    Log.i("connectlog", e.toString() + "에러3");
//
//                }
//                //jsp로부터 받은 리턴 값입니다.
//              //  return receiveMsg;
//                return null;
//            }
//        }


        }


    }
}



