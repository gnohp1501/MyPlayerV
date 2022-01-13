package com.example.myplayerv.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myplayerv.AppConstant;
import com.example.myplayerv.R;
import com.example.myplayerv.adapters.VideoFileTabAdapter;
import com.example.myplayerv.entities.MediaFiles;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;

public class PlaylistDialog extends BottomSheetDialogFragment {
    private ArrayList<MediaFiles> arrayList = new ArrayList<>();
    private VideoFileTabAdapter adapter;
    private BottomSheetDialog bottomSheetDialog;
    private RecyclerView recyclerView;

    public PlaylistDialog(ArrayList<MediaFiles> arrayList, VideoFileTabAdapter adapter) {
        this.arrayList = arrayList;
        this.adapter = adapter;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        bottomSheetDialog = (new BottomSheetDialog(getContext(), R.style.BottomSheetDialog));
        View view = LayoutInflater.from(getContext()).inflate(R.layout.videolist_bs_layout,null);
        bottomSheetDialog.setContentView(view);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        Log.d("HE", "onCreateDialog: "+height);
        recyclerView = view.findViewById(R.id.recycler_viewplaylist);
        arrayList = fetchMedia();
        adapter = new VideoFileTabAdapter(arrayList,getContext(),1);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        inListener();
        return bottomSheetDialog;

    }
    private ArrayList<MediaFiles> fetchMedia() {
        ArrayList<MediaFiles> v = new ArrayList<>();
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = getContext().getContentResolver().query(uri, null, null, null, null);
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
                v.add(mediaFiles);
            } while (cursor.moveToNext());
        }
        return v;
    }

    private void inListener(){
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                v.onTouchEvent(event);
                return true;
            }
        });
    }
}
