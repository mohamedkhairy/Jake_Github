package khairy.com.jakegithub.network;


import androidx.lifecycle.LiveData;

import java.util.List;

import khairy.com.jakegithub.database.entity.GithubModel;
import khairy.com.jakegithub.util.ApiResponse;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface JakeApi {


    @GET("JakeWharton/repos")
    LiveData<ApiResponse<List<GithubModel>>> getRepo(@Query("page") int page, @Query("per_page") int perPage);
}
