package khairy.com.jakegithub.database.dao;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import khairy.com.jakegithub.database.entity.GithubModel;

@Dao
public interface GithubDao {


    @Query("SELECT * from github_table")
    LiveData<List<GithubModel>> getAllRepo();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertAll(GithubModel... gitHubDtos);

    @Query("DELETE FROM github_table")
    void deleteAll();


}
