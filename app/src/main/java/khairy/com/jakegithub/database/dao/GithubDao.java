package khairy.com.jakegithub.database.dao;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import io.reactivex.Flowable;
import khairy.com.jakegithub.database.entity.GithubModel;

@Dao
public interface GithubDao {


    @Query("SELECT * from github_table")
    Flowable<List<GithubModel>> getAllRepo();

//    @Query("UPDATE github_table SET page = CASE id WHEN :id <= (:page*15) THEN :page  ELSE 1 END ")
//    void updateQuantity(int id , int page);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<GithubModel> repoList);
}
