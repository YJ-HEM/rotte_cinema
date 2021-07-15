package com.example.lottecinema;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import org.jetbrains.annotations.NotNull;

public class AllQRCode extends Activity {
    private Bitmap bitmap_age;
    private TextView all_qr_moiveName;
    private TextView all_qr_seat_info;
    private TextView all_qr_start_time;
    private ImageView all_qr_age;
    private ImageView all_qrcode;
    private Button cancle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_all_qr_code);


        Intent intent = getIntent();
        String movieTitle = intent.getStringExtra("movieTitle");
        String seat_info = intent.getStringExtra("seat_info");
        String date = intent.getStringExtra("date");
        String age_limit = intent.getStringExtra("age_limit");

        bitmap_age =  BitemapConverter.StringToBitmap(age_limit);

        all_qr_age = findViewById(R.id.all_qr_age);
        all_qr_moiveName = findViewById(R.id.all_qr_moiveName);
        all_qr_seat_info = findViewById(R.id.all_qr_seat_info);
        all_qr_start_time = findViewById(R.id.all_qr_start_time);
        all_qrcode = findViewById(R.id.all_qrcode);
        cancle = findViewById(R.id.btn_all_qr_cancle);
        all_qr_age.setImageBitmap(bitmap_age);
        all_qr_moiveName.setText(movieTitle);
        all_qr_seat_info.setText(seat_info);
        all_qr_start_time.setText(date);


        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try{
            BitMatrix bitMatrix = multiFormatWriter.encode(movieTitle+date, BarcodeFormat.QR_CODE,300,300);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            all_qrcode.setImageBitmap(bitmap);}
        catch (Exception e){};

        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



    }
}