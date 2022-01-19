package com.example.myplayerv.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.myplayerv.entities.MediaFiles;

import java.util.List;

@Dao
public interface MediaFiles_DAO {
    @Query("SELECT * FROM MediaFiles")
    List<MediaFiles> getAll();

    @Insert
    void insertAll(MediaFiles... mediaFiles);

    @Delete
    void delete(MediaFiles mediaFiles);

    @Query("SELECT mediafiles.id,title,displayName,size,duration,path,dateAdded FROM mediafiles,Playlist WHERE mediafiles.id = idMediaFile AND namePlaylist LIKE :query")
    List<MediaFiles> getPlaylist(String query);
}
