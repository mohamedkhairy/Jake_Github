package khairy.com.jakegithub.database;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import khairy.com.jakegithub.database.dao.GithubDao;
import khairy.com.jakegithub.database.entity.GithubModel;

@Database(entities = {GithubModel.class}, version = 1)
public abstract class GithubDatabase extends RoomDatabase {

    private static GithubDatabase instance;

    public abstract GithubDao githubDao();

    public static synchronized GithubDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    GithubDatabase.class, "github_database")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
