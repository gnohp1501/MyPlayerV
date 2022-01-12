package com.example.myplayerv.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myplayerv.R;
import com.example.myplayerv.activities.VideoFileInFolderActivity;
import com.example.myplayerv.entities.MediaFiles;

import java.util.ArrayList;

public class VideoFolderListTabAdapter extends RecyclerView.Adapter<VideoFolderListTabAdapter.ViewHolder>{

    private ArrayList<MediaFiles> mediaFiles;
    private ArrayList<String> folderPath;
    private Context context;


    public VideoFolderListTabAdapter(ArrayList<MediaFiles> mediaFiles, ArrayList<String> folderPath, Context context) {
        this.mediaFiles = mediaFiles;
        this.folderPath = folderPath;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_folder,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int indexPath = folderPath.get(position).lastIndexOf("/");
        String nameOfFolder = folderPath.get(position).substring(indexPath+1);
        holder.folderName.setText(nameOfFolder);
        holder.folderPath.setText(folderPath.get(position));
        holder.noOfFiles.setText(noOfFolder(nameOfFolder)+"videos");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, VideoFileInFolderActivity.class);
                intent.putExtra("nameFolder",nameOfFolder);
                context.startActivity(intent);
            }
        });
    }
    @Override
    public int getItemCount() {
        return folderPath.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView folderName,folderPath,noOfFiles;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            folderName = itemView.findViewById(R.id.tv_nameFolder);
            folderPath = itemView.findViewById(R.id.tv_path);
            noOfFiles = itemView.findViewById(R.id.tv_numberOfVideo);
        }
    }
    int noOfFolder(String folder_name){
        int file_no =0;
        for(MediaFiles mediaFiles :mediaFiles){
            if(mediaFiles.getPath().substring(0,mediaFiles.getPath().lastIndexOf("/")).endsWith(folder_name)){
                file_no++;
            }
        }
        return file_no;
    }
}
