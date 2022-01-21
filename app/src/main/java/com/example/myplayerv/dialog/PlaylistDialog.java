package com.example.myplayerv.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myplayerv.AppDatabase.Database;
import com.example.myplayerv.R;
import com.example.myplayerv.adapters.PlaylistTabAdapter;
import com.example.myplayerv.adapters.VideoFileTabAdapter;
import com.example.myplayerv.entities.MediaFiles;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

public class PlaylistDialog extends BottomSheetDialogFragment {
    private RecyclerView recyclerView;
    private List<String> playlists;
    private PlaylistTabAdapter playlistTabAdapter;
    private BottomSheetDialog bottomSheetDialog;

    public PlaylistDialog(List<String> playlists, PlaylistTabAdapter playlistTabAdapter) {
        this.playlists = playlists;
        this.playlistTabAdapter = playlistTabAdapter;
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        //
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        //
        bottomSheetDialog = (new BottomSheetDialog(getContext(), R.style.BottomSheetDialog));
        View view = LayoutInflater.from(getContext()).inflate(R.layout.playlist_bs,null);
        bottomSheetDialog.getBehavior().setPeekHeight(height/2);
        bottomSheetDialog.setContentView(view);
        recyclerView = view.findViewById(R.id.recycler_viewplaylist_add);
        playlists = Database.getInstance(getActivity()).playlistDao().getList();
        playlistTabAdapter = new PlaylistTabAdapter(playlists, getContext(),0);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(playlistTabAdapter);
        playlistTabAdapter.notifyDataSetChanged();
        inListener();
        return bottomSheetDialog;

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
