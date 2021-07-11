package com.example.lottecinema.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.lottecinema.R;
import com.example.lottecinema.databinding.WatchedmoviesItemBinding;

import java.util.ArrayList;

public class WatchedMoviesAdapter extends RecyclerView.Adapter<WatchedMoviesAdapter.ViewHolder> {

private ArrayList<String> mData = null ;
private static WatchedmoviesItemBinding binding = null;
    private static final String TAG = "MovieAdapter";
private Context context;
public  class ViewHolder extends RecyclerView.ViewHolder {
    TextView textView1 ;

    public ViewHolder(@NonNull View itemView) {
        super(itemView) ;
//        binding.date.setText("123");
//        binding.hour.setText("123");
//        binding.movieNameText.setText("123");
//        binding.theaterLocation.setText("123");
//
//
//
//        // 뷰 객체에 대한 참조. (hold strong reference)
        textView1 = binding.date;
    }

}

    // 생성자에서 데이터 리스트 객체를 전달받음.
    public WatchedMoviesAdapter(Context context, ArrayList<String> list) {
        mData = list ;
        this.context=context;
    }

    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: ");

        binding = WatchedmoviesItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        Context context = parent.getContext() ;
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.watchedmovies_item, parent, false);
        ViewHolder holder = new ViewHolder(view);



        return holder ;
    }

    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @Override
    public void onBindViewHolder(WatchedMoviesAdapter.ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: "+position);

        String text = mData.get(position) ;
        holder.textView1.setText(text) ;
    }

    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: ");

        return mData.size() ;
    }
}