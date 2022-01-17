package com.example.myplayerv.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myplayerv.MyService;
import com.example.myplayerv.R;
import com.example.myplayerv.activities.VideoPlayerActivity;
import com.example.myplayerv.entities.MediaFiles;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class VideoFileTabAdapter extends RecyclerView.Adapter<VideoFileTabAdapter.ViewHolder> {
    private ArrayList<MediaFiles> videoList;
    private final Context context;
    BottomSheetDialog bottomSheetDialog;
    private final int view;

    public VideoFileTabAdapter(ArrayList<MediaFiles> videoList, Context context, int view) {
        this.videoList = videoList;
        this.context = context;
        this.view = view;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_video, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.videoName.setText(videoList.get(position).getDisplayName().replace(".mp4", ""));
        //dateConversion(long value)
        double dateAdded = Double.parseDouble(videoList.get(position).getDateAdded());
        holder.createDate.setText(dateConversion((long) dateAdded));
        holder.videoSize.setText(android.text.format.Formatter.formatFileSize(context,
                Long.parseLong(videoList.get(position).getSize())));
        //
        double milliSeconds = Double.parseDouble(videoList.get(position).getDuration());
        holder.videoDuration.setText(timeConversion((long) milliSeconds));
        Glide.with(context).load(new File(videoList.get(position).getPath())).into(holder.thumbnail);
        Glide.with(context).load(videoList.get(position).getPath()).into(holder.thumbnail);
        Log.d("IMAGE", videoList.get(position).getPath());
        if (view == 0) {
            holder.more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bottomSheetDialog = new BottomSheetDialog(context, R.style.BottomSheetTheme);

                    View bsView = LayoutInflater.from(context).inflate(R.layout.video_bs_layout,
                            v.findViewById(R.id.bottom_sheet));

                    bsView.findViewById(R.id.bs_play).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            holder.itemView.performClick();
                            bottomSheetDialog.dismiss();
                        }
                    });


                    bsView.findViewById(R.id.bs_edit).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AlertDialog.Builder alert = new AlertDialog.Builder(context);
                            alert.setTitle("Rename to :");
                            alert.setIcon(R.drawable.ic_outline_edit_24);
                            EditText editText = new EditText(context);
                            String path = videoList.get(position).getPath();
                            final File file = new File(path);
                            String videoName = file.getName();
                            videoName = videoName.substring(0, videoName.lastIndexOf("."));
                            editText.setText(videoName);
                            alert.setView(editText);
                            editText.requestFocus();
                            alert.setPositiveButton("OKE", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (TextUtils.isEmpty(editText.getText().toString())) {
                                        Toast.makeText(context, "Name video cant empty ", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    String onlyPath = file.getParentFile().getAbsolutePath();
                                    String ext = file.getAbsolutePath();
                                    ext = ext.substring(ext.lastIndexOf("."));
                                    String newPath = onlyPath + "/" + editText.getText().toString() + ext;
                                    File newFile = new File(newPath);
                                    boolean rename = file.renameTo(newFile);
                                    if (rename) {
                                        ContentResolver resolver = context.getApplicationContext().getContentResolver();
                                        resolver.delete(MediaStore.Files.getContentUri("external"),
                                                MediaStore.MediaColumns.DATA + "=?", new String[]
                                                        {file.getAbsolutePath()});
                                        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                                        intent.setData(Uri.fromFile(newFile));
                                        context.getApplicationContext().sendBroadcast(intent);
                                        notifyDataSetChanged();
                                        Toast.makeText(context, "Video renamed", Toast.LENGTH_SHORT).show();
                                        SystemClock.sleep(200);
                                        ((Activity) context).recreate();
                                    } else {
                                        Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });
                            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            alert.create().show();
                            bottomSheetDialog.dismiss();
                        }
                    });
                    bsView.findViewById(R.id.bs_share).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Uri uri = Uri.parse(videoList.get(position).getPath());
                            Intent share = new Intent(Intent.ACTION_SEND);
                            share.setType("video/*");
                            share.putExtra(Intent.EXTRA_STREAM, uri);
                            context.startActivity(Intent.createChooser(share, "Share"));
                            bottomSheetDialog.dismiss();
                        }
                    });
                    bsView.findViewById(R.id.bs_delete).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AlertDialog.Builder alert = new AlertDialog.Builder(context);
                            alert.setTitle("DELETE");
                            alert.setIcon(R.drawable.ic_baseline_delete_outline_24);
                            alert.setMessage("Do you want to delete it" + videoList.get(position).getTitle());
                            alert.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Uri contentUri = ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                                            Long.parseLong(videoList.get(position).getId()));
                                    File file = new File(videoList.get(position).getPath());
                                    boolean delete = file.delete();
                                    if (delete) {
                                        context.getContentResolver().delete(contentUri, null, null);
                                        videoList.remove(position);
                                        notifyItemRemoved(position);
                                        notifyItemRangeChanged(position, videoList.size());
                                        Toast.makeText(context.getApplicationContext(), "Done", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(context.getApplicationContext(), "No", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            alert.show();
                            bottomSheetDialog.dismiss();
                        }
                    });
                    bsView.findViewById(R.id.bs_info).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AlertDialog.Builder alert = new AlertDialog.Builder(context);
                            alert.setTitle("Info");
                            alert.setIcon(R.drawable.ic_outline_info_24);
                            //get DATA
                            String name = "File name : " + (videoList.get(position).getDisplayName()).replace(".mp4", "");
                            String dateAdd = "Date add : " + dateConversion((long) dateAdded);
                            String path = "Path : " + videoList.get(position).getPath();
                            String size = "Size : " + android.text.format.Formatter
                                    .formatFileSize(context, Long.parseLong(videoList.get(position).getSize()));
                            String time = "Length : " + timeConversion((long) milliSeconds);
                            alert.setMessage("\n" + name + "\n\n" + dateAdd + "\n\n" + path + "\n\n" + size + "\n\n" + time);
                            //
                            alert.setPositiveButton("OKE", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            alert.create().show();
                        }
                    });
                    bottomSheetDialog.setContentView(bsView);
                    bottomSheetDialog.show();
                }
            });
        } else {
            holder.more.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, VideoPlayerActivity.class);
                intent.putExtra("position", position);
                intent.putExtra("video_title", videoList.get(position).getDisplayName());
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("videoArrayList", videoList);
                intent.putExtras(bundle);
                context.startActivity(intent);
                if (view == 1) {
                    ((Activity) context).finish();
                }
                //
//                Intent serviceIntent = new Intent(context, MyService.class);
//                serviceIntent.putExtra("position", position);
//                serviceIntent.putExtras(bundle);
//                ContextCompat.startForegroundService(context, serviceIntent);
                //
            }
        });
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView thumbnail;
        private final ImageView more;
        private final TextView videoName;
        private final TextView videoSize;
        private final TextView videoDuration;
        private final TextView createDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbnail = itemView.findViewById(R.id.iv_thumbnail);
            more = itemView.findViewById(R.id.iv_more);
            videoName = itemView.findViewById(R.id.tv_video_name);
            videoSize = itemView.findViewById(R.id.tv_size);
            videoDuration = itemView.findViewById(R.id.tv_total_time);
            createDate = itemView.findViewById(R.id.tv_created_day);
        }
    }

    private String timeConversion(long v) {
        String videoTime;
        int duration = (int) v;
        int hours = (duration / 3600000);
        int mns = (duration / 60000) % 60000;
        int scs = duration % 60000 / 1000;
        if (hours > 0) {
            videoTime = String.format("%02d:%02d:%02d", hours, mns, scs);
        } else {
            videoTime = String.format("%02d:%02d", mns, scs);
        }
        return videoTime;
    }

    private String dateConversion(long value) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String dateAdded = dateFormat.format(new Date((int) value * 1000L));
        return dateAdded;
    }

    public void updateVideoFile(ArrayList<MediaFiles> files) {
        videoList = new ArrayList<>();
        videoList.addAll(files);
        notifyDataSetChanged();
    }
}
