package com.example.myplayerv.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Toast;

import com.example.myplayerv.R;
import com.example.myplayerv.adapters.VideoFileTabAdapter;
import com.example.myplayerv.entities.MediaFiles;

import java.util.ArrayList;

public class VideoFileInFolderActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    private ArrayList<MediaFiles> videoFileArrayList = new ArrayList<>();
    VideoFileTabAdapter videoFileTabAdapter;
    private String nameFolder;
    SwipeRefreshLayout swipeRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_file_in_folder);

        nameFolder = getIntent().getStringExtra("nameFolder");
        recyclerView = findViewById(R.id.rv_videoListInFolder);

        showVideoList();
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_folders);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                showVideoList();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void showVideoList() {
        videoFileArrayList = fetchMedia(nameFolder);
        videoFileTabAdapter = new VideoFileTabAdapter(videoFileArrayList,this);
        recyclerView.setAdapter(videoFileTabAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));
        videoFileTabAdapter.notifyDataSetChanged();
    }
    private ArrayList<MediaFiles> fetchMedia(String folder) {
        //load Folder fetchMedia(String folder)
        ArrayList<MediaFiles> v = new ArrayList<>();
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Video.Media.DATA+" like?";
        String[] selectionArg = new String[]
                {"%"+folder+"%"};
        Cursor cursor = getContentResolver().query(uri,null,selection,selectionArg,null);
        if(cursor !=null && cursor.moveToNext()){
            do{
                @SuppressLint("Range") String id = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media._ID));
                @SuppressLint("Range") String title = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.TITLE));
                @SuppressLint("Range") String displayName = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME));
                @SuppressLint("Range") String size = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.SIZE));
                @SuppressLint("Range") String duration = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DURATION));
                @SuppressLint("Range") String path = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
                @SuppressLint("Range") String dateAdded = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATE_ADDED));
                MediaFiles mediaFiles = new MediaFiles(id,title,displayName,size,duration,path,dateAdded);
                v.add(mediaFiles);
            }while (cursor.moveToNext());
        }
        return v;
    }
}