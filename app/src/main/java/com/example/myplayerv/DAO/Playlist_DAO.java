package com.example.myplayerv.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.myplayerv.entities.Playlist;

import java.util.List;

@Dao
public interface Playlist_DAO {
    @Query("SELECT * FROM playlist")
    List<Playlist> getAll();

    @Insert
    void insertAll(Playlist... playlists);

    @Query("SELECT DISTINCT namePlaylist FROM playlist")
    List<String> getList();


    @Delete
    void delete(Playlist playlist);
}
