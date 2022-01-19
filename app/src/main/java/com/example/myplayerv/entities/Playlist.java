package com.example.myplayerv.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

@Entity(tableName = "Playlist")
public class Playlist {
    @PrimaryKey(autoGenerate = true)
    @NotNull
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "idMediaFile")
    private String idMediaFile;

    @ColumnInfo(name = "namePlaylist")
    private String namePlaylist;

    public Playlist(String idMediaFile, String namePlaylist) {
        this.idMediaFile = idMediaFile;
        this.namePlaylist = namePlaylist;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIdMediaFile() {
        return idMediaFile;
    }

    public void setIdMediaFile(String idMediaFile) {
        this.idMediaFile = idMediaFile;
    }

    public String getNamePlaylist() {
        return namePlaylist;
    }

    public void setNamePlaylist(String namePlaylist) {
        this.namePlaylist = namePlaylist;
    }
}
