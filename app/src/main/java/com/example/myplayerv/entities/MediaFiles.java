package com.example.myplayerv.entities;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

@Entity(tableName = "MediaFiles")
public class MediaFiles implements Parcelable {

    @PrimaryKey
    @NotNull
    @ColumnInfo(name = "id")
    private String id;
    @ColumnInfo(name = "title")
    private String title;
    @ColumnInfo(name = "displayName")
    private String displayName;
    @ColumnInfo(name = "size")
    private String size;
    @ColumnInfo(name = "duration")
    private String duration;
    @ColumnInfo(name = "path")
    private String path;
    @ColumnInfo(name = "dateAdded")
    private String dateAdded;

    public MediaFiles(String id, String title, String displayName, String size, String duration, String path, String dateAdded) {
        this.id = id;
        this.title = title;
        this.displayName = displayName;
        this.size = size;
        this.duration = duration;
        this.path = path;
        this.dateAdded = dateAdded;
    }

    protected MediaFiles(Parcel in) {
        id = in.readString();
        title = in.readString();
        displayName = in.readString();
        size = in.readString();
        duration = in.readString();
        path = in.readString();
        dateAdded = in.readString();
    }

    public static final Creator<MediaFiles> CREATOR = new Creator<MediaFiles>() {
        @Override
        public MediaFiles createFromParcel(Parcel in) {
            return new MediaFiles(in);
        }

        @Override
        public MediaFiles[] newArray(int size) {
            return new MediaFiles[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(displayName);
        dest.writeString(size);
        dest.writeString(duration);
        dest.writeString(path);
        dest.writeString(dateAdded);
    }
}
