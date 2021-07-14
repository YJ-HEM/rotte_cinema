package com.example.lottecinema.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lottecinema.BitemapConverter;
import com.example.lottecinema.MyMovies;
import com.example.lottecinema.R;
import com.example.lottecinema.Review;
import com.example.lottecinema.databinding.ActivityWatchedMoviesBinding;
import com.example.lottecinema.databinding.WatchedmoviesItemBinding;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static com.example.lottecinema.HttpReviewThread.list;

public class WatchedMoviesAdapter extends RecyclerView.Adapter<WatchedMoviesAdapter.ViewHolder> {

    static public SharedPreferences sharedPreferences;
    static public int item_position;
    private ArrayList<MyMovies> mData = list;
    private static WatchedmoviesItemBinding binding;
    private static ActivityWatchedMoviesBinding binding2;
    ViewHolder holder2;
    private static final String TAG = "MovieAdapter";
    private Context context;
    Bitmap poster_bitmap;
    Bitmap age_bitmap;

    // 생성자에서 데이터 리스트 객체를 전달받음.
    public WatchedMoviesAdapter(Context context, ArrayList<MyMovies> list) {
        //  testbitmap = BitemapConverter.StringToBitmap(HttpReviewThread.Bitmap);

        this.context = context;
    }

    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: ");

        binding = WatchedmoviesItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        // View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.watchedmovies_item, parent, false);
        View view = binding.getRoot();
        ViewHolder holder = new ViewHolder(view);


        return holder;
    }

    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(WatchedMoviesAdapter.ViewHolder holder, int position) {
        Log.d("onBindViewHolder", "onBindViewHolder: " + position);


        MyMovies myMoviesOrder = mData.get(position);

        sharedPreferences = context.getSharedPreferences("review", context.MODE_PRIVATE);
        String reviewResult = sharedPreferences.getString("review_result", "");
        String position_string = reviewResult.replaceAll("[^0-9]", "");


        if (reviewResult.equals("success")) {
            holder.btn_write_review.setText("내가 쓴 리뷰");
        }


        holder.txt_date.setText(myMoviesOrder.getDate());

        String show_time = myMoviesOrder.getDate();

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        DateFormat time = new SimpleDateFormat("HH:mm");

        Calendar cal2 = null;
        try {
            Date show_date = df.parse(show_time); //상영시간을 df으로 변경
             cal2 = Calendar.getInstance();
            cal2.setTime(show_date);
            cal2.add(Calendar.HOUR, 1); //한시간 더하기

            Log.d("time","+1시간 : "+cal2.toString());
        } catch (ParseException e) {
            e.printStackTrace();
            Log.d("time",e.toString());
        }

        Calendar cal = Calendar.getInstance(); //현재시간
        Log.d("time", "현재시간 : "+cal.getTime().toString());
        boolean a = cal2.before(cal);
        Log.d("time", a+"true/false"); //현재시간이 상영시간+1시간 전이면 true반환

        //현재 시간이 영화시작시간+1시간 후면 qr코드로 입장 버튼을 보여주지 않는다.
        if(cal2.before(cal)){
            holder.btn_qr_code.setVisibility(View.GONE);
        }




            holder.txt_loca.setText(myMoviesOrder.getCinema());
            holder.txt_peopleNum.setText(myMoviesOrder.getCustomer());
            holder.txt_seatNum.setText(myMoviesOrder.getSeat());
            holder.txt_movieTitle.setText(myMoviesOrder.getMovie());

            age_bitmap = BitemapConverter.StringToBitmap(myMoviesOrder.getAge());
            poster_bitmap = BitemapConverter.StringToBitmap(myMoviesOrder.getPoster());
            holder.img_poster.setImageBitmap(poster_bitmap);
            holder.img_age.setImageBitmap(age_bitmap);
            holder.btn_write_review.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Log.d("revieww", "몇번 째 뷰 인지 " + holder.getAbsoluteAdapterPosition());
                    Log.d("revieww", myMoviesOrder.getMovie());
                    Log.d("revieww", String.valueOf(myMoviesOrder.getMovie_index()));

                    int position_int = holder.getAbsoluteAdapterPosition();
                    String position_string = Integer.toString(position_int);
                    Intent intent = new Intent(context, Review.class);
                    intent.putExtra("moiveTitle", myMoviesOrder.getMovie());
                    intent.putExtra("position", position_int);
                    intent.putExtra("movieIndex", myMoviesOrder.getMovie_index());


                    context.startActivity(intent);
                }
            });

        }

        // getItemCount() - 전체 데이터 갯수 리턴.
        @Override
        public int getItemCount () {
            Log.d("getItemCount", mData.size() + "");

            return mData.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView txt_date;
            TextView txt_loca;
            TextView txt_peopleNum;
            TextView txt_seatNum;
            TextView txt_movieTitle;

            ImageView img_poster;
            ImageView img_age;
            Button btn_write_review;
            Button btn_qr_code;
            public ViewHolder(@NonNull View view) {
                super(view);
                // 뷰 객체에 대한 참조. (hold strong reference)
                //  textView1 = view.findViewById(R.id.date);
                txt_date = binding.date;
                txt_loca = binding.theaterLocation;
                txt_peopleNum = binding.audienceNum;
                txt_seatNum = binding.seatNum;
                txt_movieTitle = binding.movieNameText;
                img_poster = binding.moviePoster;
                img_age = binding.imageAge;
                btn_write_review = binding.btnDoShowReview;
                btn_qr_code = binding.btnQrCode;

            }

        }

    }