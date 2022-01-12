package com.example.myplayerv.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myplayerv.R;
import com.example.myplayerv.entities.Icon;

import java.io.PipedOutputStream;
import java.util.ArrayList;

public class IconAdapter extends RecyclerView.Adapter<IconAdapter.ViewHolder> {
    private ArrayList<Icon> icons = new ArrayList<>();
    private Context context;
    private OnItemClickListener mListener;

    public IconAdapter(ArrayList<Icon> icons, Context context) {
        this.icons = icons;
        this.context = context;
    }
    public interface OnItemClickListener{
        void onItemClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        mListener =listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.icon_view,parent,false);
        return new ViewHolder(view,mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.iv_icon.setImageResource(icons.get(position).getImaView());
        holder.tv_titleIcon.setText(icons.get(position).getIconTitle());
    }

    @Override
    public int getItemCount() {
        return icons.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView iv_icon;
        private TextView tv_titleIcon;
        public ViewHolder(@NonNull View itemView,OnItemClickListener listener) {
            super(itemView);
            iv_icon = itemView.findViewById(R.id.iv_icon);
            tv_titleIcon = itemView.findViewById(R.id.tv_titleIcon);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener !=null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}
