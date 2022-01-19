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

import com.example.myplayerv.AppDatabase.Database;
import com.example.myplayerv.R;
import com.example.myplayerv.adapters.VideoFileTabAdapter;
import com.example.myplayerv.entities.MediaFiles;

import java.util.ArrayList;
import java.util.List;


public class NewFavoritesTab extends Fragment {

    RecyclerView recyclerViewNew,recyclerViewList;
    private ArrayList<MediaFiles> videoFileArrayList = new ArrayList<>();
    static VideoFileTabAdapter videoFileTabAdapter;
    private ArrayList<MediaFiles> videoFileArrayListFavorites;
    static VideoFileTabAdapter videoFileTabAdapterFavorites;
    SwipeRefreshLayout swipeRefreshLayout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_playlist_tab, container, false);
        init(view);
        showVideoList();
        showVideoListFavorites();
        return view;
    }

    public void init(View view){
        recyclerViewNew = view.findViewById(R.id.recycler_view_new);
        recyclerViewList = view.findViewById(R.id.recycler_view_list);
    }
    private void showVideoList() {
        videoFileArrayList = fetchMedia();
        videoFileTabAdapter = new VideoFileTabAdapter(videoFileArrayList, getContext(),0);
        recyclerViewNew.setAdapter(videoFileTabAdapter);
        recyclerViewNew.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        videoFileTabAdapter.notifyDataSetChanged();
    }
    private void showVideoListFavorites() {
        videoFileArrayListFavorites = (ArrayList<MediaFiles>) Database.getInstance(getActivity()).mediaFilesDao().getPlaylist("Favorites");
        videoFileTabAdapterFavorites = new VideoFileTabAdapter(videoFileArrayListFavorites, getContext(),0);
        recyclerViewList.setAdapter(videoFileTabAdapterFavorites);
        recyclerViewList.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        videoFileTabAdapterFavorites.notifyDataSetChanged();
    }
    private ArrayList<MediaFiles> fetchMedia() {
        int i =0;
        ArrayList<MediaFiles> v = new ArrayList<>();
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null,MediaStore.MediaColumns.DATE_ADDED + " DESC");
        if (cursor != null && cursor.moveToNext()) {
            do{
                @SuppressLint("Range") String id = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media._ID));
                @SuppressLint("Range") String title = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.TITLE));
                @SuppressLint("Range") String displayName = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME));
                @SuppressLint("Range") String size = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.SIZE));
                @SuppressLint("Range") String duration = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DURATION));
                @SuppressLint("Range") String path = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
                @SuppressLint("Range") String dateAdded = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATE_ADDED));
                MediaFiles mediaFiles = new MediaFiles(id, title, displayName, size, duration, path, dateAdded);
                v.add(mediaFiles);
                i++;
            }while (cursor.moveToNext() && i<3);
        }
        return v;
    }
}