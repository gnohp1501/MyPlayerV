package com.example.myplayerv.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.example.myplayerv.AppDatabase.Database;
import com.example.myplayerv.R;
import com.example.myplayerv.adapters.VideoFileTabAdapter;
import com.example.myplayerv.entities.MediaFiles;

import java.util.ArrayList;

public class PlaylistActivity extends AppCompatActivity {
    private TextView tv_namePlaylist;
    private RecyclerView recyclerView;
    private ArrayList<MediaFiles> videoFileArrayListFavorites;
    static VideoFileTabAdapter videoFileTabAdapterFavorites;
    private String namePlaylist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);
        namePlaylist = getIntent().getStringExtra("namePlaylist");
        tv_namePlaylist=findViewById(R.id.tv_namePlaylist);
        recyclerView=findViewById(R.id.recycler_view_playlist);
        tv_namePlaylist.setText(namePlaylist);
        showVideoListFavorites();
    }
    private void showVideoListFavorites() {
        videoFileArrayListFavorites = (ArrayList<MediaFiles>) Database.getInstance(getApplicationContext()).mediaFilesDao().getPlaylist(namePlaylist);
        videoFileTabAdapterFavorites = new VideoFileTabAdapter(videoFileArrayListFavorites, getApplicationContext(),0);
        recyclerView.setAdapter(videoFileTabAdapterFavorites);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));
        videoFileTabAdapterFavorites.notifyDataSetChanged();
    }

}