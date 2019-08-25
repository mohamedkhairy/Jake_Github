package khairy.com.jakegithub.network;


import java.util.List;

import io.reactivex.Flowable;
import khairy.com.jakegithub.database.entity.GithubModel;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface JakeApi {

    @GET("JakeWharton/repos")
    Flowable<List<GithubModel>> getRepo(@Query("page") int page, @Query("per_page") int perPage);
}
