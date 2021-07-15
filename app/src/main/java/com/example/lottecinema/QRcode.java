package com.example.lottecinema;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lottecinema.Adapter.WatchedMoviesAdapter;
import com.example.lottecinema.databinding.ActivityWatchedMoviesBinding;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class QRcode extends Fragment {
    private ImageView iv;
    private TextView qr_movie_title;
    private TextView qr_seat_info;
    private   Bitmap age_bitmap;
    private   ImageView age_iv;
    private TextView qr_start_time;

    private String date;
    private String seat_info;
    private String movie_title;
    private String movie_age;
    private String start_time;

    HttpReviewThread httpReviewThread;

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);

    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        httpReviewThread = new HttpReviewThread();
        httpReviewThread.start();
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.activity_qrcode, container, false);
        qr_movie_title=rootView.findViewById(R.id.qr_movie_title);
        qr_seat_info=rootView.findViewById(R.id.qr_seat_info);
        age_iv = rootView.findViewById(R.id.qr_age);
        qr_start_time =rootView.findViewById(R.id.qr_start_time);



        try {
            httpReviewThread.join();
        } catch (Exception e){};

        NameAscending nameAscending = new NameAscending();
        Collections.sort(HttpReviewThread.list, nameAscending);


        Log.d("ascending",HttpReviewThread.list.size()+"");

        for (MyMovies temp : HttpReviewThread.list) {
            Log.d("ascending",temp.getDate());
        }

        date = HttpReviewThread.list.get(0).getDate();
        seat_info=  HttpReviewThread.list.get(0).getCinema() +" "+ HttpReviewThread.list.get(0).getCustomer()+ HttpReviewThread.list.get(0).getSeat() ;
        movie_title=HttpReviewThread.list.get(0).getMovie();
        movie_age=HttpReviewThread.list.get(0).getAge();
        start_time=HttpReviewThread.list.get(0).getDate();


        age_bitmap = BitemapConverter.StringToBitmap(movie_age);

        Calendar cal = Calendar.getInstance(); //현재시간

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        Calendar cal2 = null;
        try {
            Date show_date = df.parse(date); //상영시간을 df으로 변경
            cal2 = Calendar.getInstance();
            cal2.setTime(show_date);

            Log.d("ascending","가장임박한영화시작시간 : "+cal2.toString());
        } catch (ParseException e) {
            e.printStackTrace();
            Log.d("ascending",e.toString());
        }
        boolean a = cal2.before(cal); //가장임박한 영화 시작 시간이 현재 시간 이전이면 false
        Log.d("ascending",a+"");
        iv = (ImageView)rootView.findViewById(R.id.qrcode);

        //상영시간 전
        if(a==false){
            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
            try{
                BitMatrix bitMatrix = multiFormatWriter.encode(movie_title+date, BarcodeFormat.QR_CODE,2000,2000);
                BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                iv.setImageBitmap(bitmap);
                qr_movie_title.setText(movie_title);
                age_iv.setImageBitmap(age_bitmap);
                qr_start_time.setText(start_time);

                qr_seat_info.setText(seat_info);

            }catch (Exception e){}


        }
        //상영시간 후 - 보여줄 qr없음
        else{
            iv.setImageResource(R.drawable.ic_sad);
            qr_movie_title.setText("예매 내역이 없습니다.");
            qr_seat_info.setText("");
            age_iv.setVisibility(View.GONE);
            qr_start_time.setText("");

        }

        Log.d("ascending",date);

        return rootView;
    }



    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
    }








}
//date를 기준으로 내림차순
class NameAscending implements Comparator<MyMovies> {

    @Override
    public int compare(MyMovies a, MyMovies b) {

        String temp1 = a.getDate();
        String temp2 = b.getDate();

        return temp2.compareTo(temp1);
        /*return a.getName().compareTo(b.getName());*/
    }
}