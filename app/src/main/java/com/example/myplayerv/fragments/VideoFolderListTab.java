package com.example.myplayerv.fragments;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myplayerv.R;
import com.example.myplayerv.adapters.VideoFolderListTabAdapter;
import com.example.myplayerv.entities.MediaFiles;

import java.util.ArrayList;

public class VideoFolderListTab extends Fragment {

    private ArrayList<MediaFiles> mediaFiles = new ArrayList<>();
    private ArrayList<String> allFolderString = new ArrayList<>();
    RecyclerView recyclerView;
    VideoFolderListTabAdapter adapter;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video_folder_list_tab, container, false);
        recyclerView = view.findViewById(R.id.rv_videoListFolder);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_folders);
        showFolder();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                showFolder();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        return view;
    }

    private void showFolder() {
        mediaFiles = fetchMedia();
        adapter = new VideoFolderListTabAdapter(mediaFiles,allFolderString,getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false));
        adapter.notifyDataSetChanged();
    }

    public ArrayList<MediaFiles> fetchMedia() {
        ArrayList<MediaFiles> mediaFilesArrayList = new ArrayList<>();
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;

        Cursor cursor = getActivity().getContentResolver().query(uri,null,null,null,null);
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

                int index = path.lastIndexOf("/");
                String subString = path.substring(0,index);
                if(!allFolderString.contains(subString)){
                    allFolderString.add(subString);
                }
                mediaFilesArrayList.add(mediaFiles);
            }while (cursor.moveToNext());
        }
        return mediaFilesArrayList;
    }

}