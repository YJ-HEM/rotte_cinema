package com.example.lottecinema;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Review extends Activity {
    int intRating = 0;

    static String post(String url, String eboxContents, String ratingNum, String indexMovie) throws IOException {


        OkHttpClient client = new OkHttpClient.Builder().cookieJar(MainActivity.cookieJar).build();
        RequestBody formBody = new FormBody.Builder()
                .add("text", eboxContents)
                .add("rating", ratingNum)
                .add("indexMovie", indexMovie)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        Response response = client.newCall(request).execute();


        return response.body().string();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_review);
        TextView movieTitle = findViewById(R.id.moiveName);
        RatingBar ratingBar = findViewById(R.id.starRating);
        TextView txtRating = findViewById(R.id.txtStarRating);
        EditText editText = findViewById(R.id.etxtReview);
        TextView textLength = findViewById(R.id.textLength);
        Button btnAccept = findViewById(R.id.btnAccept);
        Button btnCancle = findViewById(R.id.btnCancle);
        intRating = (int) ratingBar.getRating();


        Intent intent = getIntent();
        String movie_title = intent.getStringExtra("moiveTitle");
        movieTitle.setText(movie_title);


        //textview 특정 글자만 스타일조정
        Spannable span = (Spannable) txtRating.getText();
        span.setSpan(new StyleSpan(Typeface.BOLD), 0, 2, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        span.setSpan(new RelativeSizeSpan(1.3f), 0, 2, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);


        //별 선택할때마다 그 값 읽어오기
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {

            @Override

            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

                // 저는 0개를 주기싫어서, 만약 1개미만이면 강제로 1개를 넣었습니다.

                if (ratingBar.getRating() < 1.0f) {

                    ratingBar.setRating(1);

                }


                intRating = (int) ratingBar.getRating();
                txtRating.setText(intRating + "점");

                if (txtRating.getText().length() == 3) {
                    span.setSpan(new StyleSpan(Typeface.BOLD), 0, 2, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                    span.setSpan(new RelativeSizeSpan(1.3f), 0, 2, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                } else if (txtRating.getText().length() == 2) {
                    span.setSpan(new StyleSpan(Typeface.BOLD), 0, 1, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                    span.setSpan(new RelativeSizeSpan(1.3f), 0, 1, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                }
            }

        });


        editText.addTextChangedListener(new TextWatcher() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                String input = editText.getText().toString();
                textLength.setText(input.length() + " / 220 글자 수");
                //220글자가 되면 notice
                if (input.length() == 220) {
                    textLength.setTextColor(Color.RED);
                    textLength.setTypeface(null, Typeface.BOLD);
                } else {
                    textLength.setTextColor(Color.BLACK);
                    textLength.setTypeface(null, Typeface.NORMAL);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //작성버튼 누르면 서버에 데이터보내기
        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(editText.getText())) {

                    Toast.makeText(Review.this, "리뷰내용을 입력하세요.", Toast.LENGTH_SHORT).show();
                } else {

                    String stringRating = Integer.toString(intRating);

                    new Thread() {
                        @Override
                        public void run() {

                            super.run();
                            try {
                                String stringEditText = editText.getText().toString();
                                JSONObject jsonObject = new JSONObject(post("http://kumas.dev/rotte_cinema/reviewobject.do", stringEditText, stringRating, "1"));
                                Log.v("okhttp33", "연결성공" + jsonObject.get("result").toString());
                                Log.v("okhttp33", editText.getText().toString() + stringRating);

                            } catch (
                                    Exception e) {
                                Log.v("okhttp33", "error  " + editText.getText().toString() + "별점" + stringRating + e.toString());
                            }
                        }
                    }.start();
                }
            }
        });

        //닫기 버튼
        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }
}