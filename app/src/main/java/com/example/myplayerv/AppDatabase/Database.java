package com.example.myplayerv.AppDatabase;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.myplayerv.DAO.MediaFiles_DAO;
import com.example.myplayerv.DAO.Playlist_DAO;
import com.example.myplayerv.entities.MediaFiles;
import com.example.myplayerv.entities.Playlist;

@androidx.room.Database(entities = {MediaFiles.class, Playlist.class},version = 1)
public abstract class Database extends RoomDatabase {
    private static final String DATABASE_NAME = "PLAYLISTMEDIA.db";
    private static Database instance;

    public static synchronized Database getInstance(Context context)
    {
        if(instance==null)
        {
            instance= Room.databaseBuilder(context.getApplicationContext(),Database.class,DATABASE_NAME)
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }
    public abstract MediaFiles_DAO mediaFilesDao();
    public abstract Playlist_DAO playlistDao();
}
