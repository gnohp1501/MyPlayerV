package com.example.myplayerv.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myplayerv.R;
import com.example.myplayerv.entities.Icon;

import java.util.ArrayList;
import java.util.List;

public class IconPlaylistAdapter extends RecyclerView.Adapter<IconPlaylistAdapter.ViewHolder> {
    private List<String> icons ;
    private final Context context;
    private IconPlaylistAdapter.OnItemClickListener mListener;

    public IconPlaylistAdapter(List<String> icons, Context context) {
        this.icons = icons;
        this.context = context;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(IconPlaylistAdapter.OnItemClickListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public IconPlaylistAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.icon_viewplaylist, parent, false);
        return new IconPlaylistAdapter.ViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull IconPlaylistAdapter.ViewHolder holder, int position) {
        holder.tv_titleIcon.setText(icons.get(position));
    }

    @Override
    public int getItemCount() {
        return icons.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView iv_icon;
        private final TextView tv_titleIcon;

        public ViewHolder(@NonNull View itemView, IconPlaylistAdapter.OnItemClickListener listener) {
            super(itemView);
            iv_icon = itemView.findViewById(R.id.iv_iconPlaylist);
            tv_titleIcon = itemView.findViewById(R.id.tv_titleIconPlaylist);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

}
