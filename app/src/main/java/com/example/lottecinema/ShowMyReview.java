package com.example.lottecinema;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.example.lottecinema.databinding.ActivityMainBinding;
import com.example.lottecinema.databinding.ActivityShowMyReviewBinding;

public class ShowMyReview extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityShowMyReviewBinding binding = ActivityShowMyReviewBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Intent intent = getIntent();
        String movie_title = intent.getStringExtra("movieTitle");
        String ratingNum  = intent.getStringExtra("ratingNum");
        String contents = intent.getStringExtra("contents");
        String date = intent.getStringExtra("date");

        Log.d("reviewdate","afterintent"+ratingNum);

        binding.reviewDate.setText(date);
        binding.showReviewMoiveName.setText(movie_title);
        binding.showReviewtxtReview.setText(contents);
        binding.showReviewTxtStarRating.setText(ratingNum+"점");
        binding.showReviewStarRating.setNumStars(Integer.parseInt(ratingNum));
        int rating = Integer.parseInt(ratingNum);
       binding.showReviewStarRating2.setNumStars(10-rating);

        Log.d("reviewrating", String.valueOf(10-rating));

       Spannable span = (Spannable) binding.showReviewTxtStarRating.getText();

        if ( binding.showReviewTxtStarRating.getText().length() == 3) {
            Log.d("textstlye", "3"+  String.valueOf( binding.showReviewTxtStarRating.getText().length()));
            span = (Spannable)  binding.showReviewTxtStarRating.getText();
            span.setSpan(new StyleSpan(Typeface.BOLD), 0, 2, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            span.setSpan(new RelativeSizeSpan(1.1f), 0, 2, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        } else if ( binding.showReviewTxtStarRating.getText().length() == 2) {
            Log.d("textstlye", "2"+  String.valueOf( binding.showReviewTxtStarRating.getText().length()));
            span = (Spannable)  binding.showReviewTxtStarRating.getText();
            span.setSpan(new StyleSpan(Typeface.BOLD), 0, 1, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            span.setSpan(new RelativeSizeSpan(1.1f), 0, 1, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        }



        binding.btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }



}