package com.example.lottecinema;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Window;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Review extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_review);

       RatingBar ratingBar = findViewById(R.id.starRating);
        TextView txtRating = findViewById(R.id.txtStarRating);
        EditText editText = findViewById(R.id.etxtReview);
        TextView textLength = findViewById(R.id.textLength);

       //별 선택할때마다 그 값 읽어오기
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {

            @Override

            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

                // 저는 0개를 주기싫어서, 만약 1개미만이면 강제로 1개를 넣었습니다.

                if (ratingBar.getRating()<1.0f){

                    ratingBar.setRating(1);

                }

                int intRating = (int) ratingBar.getRating();
                txtRating.setText(intRating+"점");

            }

        });

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                String input = editText.getText().toString();
                textLength.setText(input.length()+" / 220 글자 수");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }
}