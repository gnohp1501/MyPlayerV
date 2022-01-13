package com.example.myplayerv.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.myplayerv.AppConstant;
import com.example.myplayerv.R;
import com.example.myplayerv.activities.MainActivity;
import com.example.myplayerv.adapters.VideoFileTabAdapter;
import com.example.myplayerv.entities.MediaFiles;

import java.util.ArrayList;


public class VideoListTab extends Fragment {

    RecyclerView recyclerView;
    private ArrayList<MediaFiles> videoFileArrayList = new ArrayList<>();
    static VideoFileTabAdapter videoFileTabAdapter;
    SwipeRefreshLayout swipeRefreshLayout;
    EditText search;
    String sort_value;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video_list_tab, container, false);
        recyclerView = view.findViewById(R.id.rv_videoList);
        search = view.findViewById(R.id.et_searchVideo);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_folders);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                showVideoList();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        showVideoList();
        refreshWhenSort();
        setSearch();
        return view;
    }

    private void refreshWhenSort() {
        ((MainActivity)getActivity()).setFragmentRefreshListener(new MainActivity.FragmentRefreshListener() {
            @Override
            public void onRefresh() {
                showVideoList();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void setSearch() {
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String inputs = s.toString().toLowerCase();
                ArrayList<MediaFiles> mediaFiles = new ArrayList<>();
                for (MediaFiles media : videoFileArrayList) {
                    if (media.getTitle().toLowerCase().contains(inputs)) {
                        mediaFiles.add(media);
                    }
                }
                VideoListTab.videoFileTabAdapter.updateVideoFile(mediaFiles);
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void showVideoList() {
        videoFileArrayList = fetchMedia();
        videoFileTabAdapter = new VideoFileTabAdapter(videoFileArrayList, getContext(),0);
        recyclerView.setAdapter(videoFileTabAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        videoFileTabAdapter.notifyDataSetChanged();
    }

    private ArrayList<MediaFiles> fetchMedia() {
        SharedPreferences preferences = getActivity().getSharedPreferences(AppConstant.MY_PREF, Context.MODE_PRIVATE);
        sort_value = preferences.getString("sort", "");
        if (sort_value.equals("sortName")) {
            sort_value = MediaStore.MediaColumns.DISPLAY_NAME + " ASC";
        } else if (sort_value.equals("sortSize")) {
            sort_value = MediaStore.MediaColumns.SIZE + " DESC";
        } else if (sort_value.equals("sortDate")) {
            sort_value = MediaStore.MediaColumns.DATE_ADDED + " DESC";
        } else if (sort_value.equals("sortLength")) {
            sort_value = MediaStore.MediaColumns.DURATION + " DESC";
        } else {
            sort_value = null;
        }
        ArrayList<MediaFiles> v = new ArrayList<>();
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, sort_value);
        if (cursor != null && cursor.moveToNext()) {
            do {
                @SuppressLint("Range") String id = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media._ID));
                @SuppressLint("Range") String title = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.TITLE));
                @SuppressLint("Range") String displayName = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME));
                @SuppressLint("Range") String size = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.SIZE));
                @SuppressLint("Range") String duration = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DURATION));
                @SuppressLint("Range") String path = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
                @SuppressLint("Range") String dateAdded = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATE_ADDED));
                MediaFiles mediaFiles = new MediaFiles(id, title, displayName, size, duration, path, dateAdded);
                Log.d("hehe",id+"\n"+title+"\n"+displayName);
                v.add(mediaFiles);
            } while (cursor.moveToNext());
        }
        return v;
    }
}