package com.example.myplayerv.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myplayerv.R;
import com.example.myplayerv.activities.PlaylistActivity;
import com.example.myplayerv.entities.Playlist;

import java.util.ArrayList;
import java.util.List;

public class PlaylistTabAdapter extends RecyclerView.Adapter<PlaylistTabAdapter.ViewHolder>{
    private List<String> namePlaylist;
    private Context context;

    public PlaylistTabAdapter(List<String> namePlaylist, Context context) {
        this.namePlaylist = namePlaylist;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_playlist,parent,false);
        return new PlaylistTabAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
            holder.namePlaylist.setText(namePlaylist.get(position));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, PlaylistActivity.class);
                    intent.putExtra("namePlaylist",namePlaylist.get(position));
                    context.startActivity(intent);
                }
            });

    }

    @Override
    public int getItemCount() {
        return namePlaylist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView namePlaylist;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            namePlaylist = itemView.findViewById(R.id.tv_namePlaylist);
        }
    }
}
