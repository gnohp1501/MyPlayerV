package com.example.myplayerv.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.SystemClock;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myplayerv.AppDatabase.Database;
import com.example.myplayerv.R;
import com.example.myplayerv.SwipeHelper;
import com.example.myplayerv.activities.MainActivity;
import com.example.myplayerv.adapters.PlaylistTabAdapter;

import java.io.File;
import java.util.List;

public class PlaylistTab extends Fragment {
    private RecyclerView recyclerView;
    private List<String> playlists;
    private PlaylistTabAdapter playlistTabAdapter;
    SwipeRefreshLayout swipeRefreshLayout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nodefine_tab, container, false);
        recyclerView = view.findViewById(R.id.recycler_view_playlist);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_folders);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                showList();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        refreshWhenAdd();
        showList();
        //
        SwipeHelper swipeHelper = new SwipeHelper(getActivity(), recyclerView) {
            @Override
            public void instantiateUnderlayButton(RecyclerView.ViewHolder viewHolder, List<UnderlayButton> underlayButtons) {
                underlayButtons.add(new SwipeHelper.UnderlayButton(
                        "Rename",
                        0,
                        Color.parseColor("#FF3C30"),
                        new SwipeHelper.UnderlayButtonClickListener() {
                            @Override
                            public void onClick(int pos) {
                                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                                alert.setTitle("Rename to :");
                                alert.setIcon(R.drawable.ic_outline_edit_24);
                                EditText editText = new EditText(getContext());
                                String name = playlists.get(pos);
                                editText.setText(name);
                                alert.setView(editText);
                                editText.requestFocus();
                                alert.setPositiveButton("OKE", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Database.getInstance(getContext()).playlistDao().update(playlists.get(pos),editText.getText().toString());
                                    }
                                });
                                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                                alert.create().show();
                            }
                        }
                ));

                underlayButtons.add(new SwipeHelper.UnderlayButton(
                        "Detele",
                        0,
                        Color.parseColor("#FF9502"),
                        new SwipeHelper.UnderlayButtonClickListener() {
                            @Override
                            public void onClick(int pos) {
                                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                                alert.setTitle("DELETE");
                                alert.setIcon(R.drawable.ic_baseline_delete_outline_24);
                                alert.setMessage("Do you want to delete it");
                                alert.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //Toast.makeText(getContext(), playlists.get(pos), Toast.LENGTH_SHORT).show();
                                        Database.getInstance(getContext()).playlistDao().deleteByUserId(playlists.get(pos));


                                    }
                                });
                                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                                alert.show();
                            }
                        }
                ));
            }
        };
        //
        return view;
    }
    private void showList(){
        playlists = Database.getInstance(getActivity()).playlistDao().getList();
        playlistTabAdapter = new PlaylistTabAdapter(playlists, getContext(),1);
        recyclerView.setAdapter(playlistTabAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        playlistTabAdapter.notifyDataSetChanged();
    }
    private void refreshWhenAdd() {
        ((MainActivity)getActivity()).setFragmentRefreshListener(new MainActivity.FragmentRefreshListener() {
            @Override
            public void onRefresh() {
                showList();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

}