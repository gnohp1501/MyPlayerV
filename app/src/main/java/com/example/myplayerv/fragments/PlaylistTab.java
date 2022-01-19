package com.example.myplayerv.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myplayerv.AppDatabase.Database;
import com.example.myplayerv.R;
import com.example.myplayerv.adapters.PlaylistTabAdapter;
import com.example.myplayerv.adapters.VideoFileTabAdapter;
import com.example.myplayerv.entities.Playlist;

import java.util.ArrayList;
import java.util.List;

public class PlaylistTab extends Fragment {
    private RecyclerView recyclerView;
    private List<String> playlists;
    private PlaylistTabAdapter playlistTabAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nodefine_tab, container, false);
        recyclerView = view.findViewById(R.id.recycler_view_playlist);
        showList();
        return view;
    }
    private void showList(){
        playlists = Database.getInstance(getActivity()).playlistDao().getList();
        playlistTabAdapter = new PlaylistTabAdapter(playlists, getContext());
        recyclerView.setAdapter(playlistTabAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        playlistTabAdapter.notifyDataSetChanged();
    }

}