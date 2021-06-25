package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class signin extends AppCompatActivity {

   static String loginId,loginPwd;
    Button parsingBtn;
    EditText et_id;
    EditText et_pw;
    CheckBox cb_save;
    Button btnlogin;
    SharedPreferences.Editor autoLogin;
    TextView loginText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);


        et_id = (EditText) findViewById(R.id.inputId);
        et_pw = (EditText) findViewById(R.id.inputPwd);
        cb_save = (CheckBox) findViewById(R.id.check1);
        parsingBtn = (Button) findViewById(R.id.btns_signin);
        btnlogin = (Button) findViewById(R.id.btn_signin);
        loginText = (TextView) findViewById(R.id.textviewlogin);

        SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
        autoLogin = auto.edit();
        //처음에는 SharedPreferences에 아무런 정보도 없으므로 값을 저장할 키들을 생성한다.
        // getString의 첫 번째 인자는 저장될 키, 두 번쨰 인자는 값입니다.
        // 첨엔 값이 없으므로 키값은 원하는 것으로 하시고 값을 ""을 줍니다.
        loginId = auto.getString("inputId", "");
        loginPwd = auto.getString("inputPwd", "");


        boolean boo = cb_save.isChecked(); //자동로그인 체크 유무 확인 / 체크되어있으면 true
        if (boo) { //자동로그인 체크 되어 있으면
            //입력한 아이디와 비밀번호를 SharedPreferences.Editor를 통해
            //auto파일의 loginId와 loginPwd에 값을 저장해 줍니다.

            autoLogin.putString("inputId", et_id.getText().toString());
            autoLogin.putString("inputPwd", et_pw.getText().toString());
            autoLogin.putBoolean("SAVE_LOGIN_DATA", cb_save.isChecked()); //현재 체크박스 상태 값 저장
            //꼭 commit()을 해줘야 값이 저장됩니다 ㅎㅎ
            autoLogin.commit();

        }

        //로그인 버튼 클릭시
//        parsingBtn.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                if (TextUtils.isEmpty(et_id.getText()) || TextUtils.isEmpty(et_pw.getText())) {
//                    //아이디나 암호 둘 중 하나가 비어있으면 토스트메시지를 띄운다
//                    Toast.makeText(signin.this, "아이디/암호를 입력해주세요", Toast.LENGTH_SHORT).show();
//                }
//                //아이디 비번이 다 입력 되었을 때, 아이디 비번 입력이 저장된 값과 같으면 자동로그인실행
//                else if (et_id.getText().equals(loginId) && et_pw.getText().equals(loginPwd)) {
//                    btnlogin.setText("로그아웃");
//                    Toast.makeText(signin.this, loginId + "님 자동로그인설정완료", Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(signin.this, MainActivity.class);
//                    startActivity(intent);
//                    finish();
//
//                }
//            }
//        });


    }

    public void AutoSinIn(View view) {

        if (TextUtils.isEmpty(et_id.getText()) || TextUtils.isEmpty(et_pw.getText())) {
            //아이디나 암호 둘 중 하나가 비어있으면 토스트메시지를 띄운다
            Toast.makeText(signin.this, "이메일/암호를 입력해주세요", Toast.LENGTH_SHORT).show();
        }


        //아이디 비번이 다 입력 되었을 때, 첫 로그인
         if ((!TextUtils.isEmpty(et_id.getText()) && !TextUtils.isEmpty(et_pw.getText())) &&loginId.equals("")&&loginPwd.equals("")) {
             MainActivity.btn_login.setText("로그아웃");
            Toast.makeText(signin.this, loginId + "님 자동로그인설정완료", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(signin.this, MainActivity.class);
            startActivity(intent);
            finish();

        }


    }
}
