package com.example.lottecinema;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lottecinema.Adapter.WatchedMoviesAdapter;
import com.example.lottecinema.databinding.ActivityWatchedMoviesBinding;
import com.example.lottecinema.databinding.WatchedmoviesItemBinding;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class watchedMovies extends Fragment {
   private RecyclerView recyclerView;
    private WatchedMoviesAdapter adapter;
    ArrayList<String> list = new ArrayList<>();

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }





    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        // 리사이클러뷰에 표시할 데이터 리스트 생성.

        for (int i=0; i<10; i++) {
            list.add(String.format("TEXT %d", i)) ;
        }

        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.activity_watched_movies, container, false);


        ActivityWatchedMoviesBinding binding = ActivityWatchedMoviesBinding.inflate(inflater);


        // 리사이클러뷰에 LinearLayoutManager 객체 지정.

         recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_watched_list);
        recyclerView.setHasFixedSize(true);



        // 리사이클러뷰에 SimpleTextAdapter 객체 지정.
         adapter = new WatchedMoviesAdapter(getActivity(), list) ;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerView.setAdapter(adapter);

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