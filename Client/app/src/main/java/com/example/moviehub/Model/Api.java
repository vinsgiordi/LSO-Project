package com.example.moviehub.Model;

import android.content.Context;
import android.widget.Toast;


import com.example.moviehub.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;


public class Api {
    Context context;
    Retrofit retrofitMovie = new Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/movie/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    Retrofit retrofitTv = new Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/tv/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    String api_key;

    public void getRecommandedMovies(Listener listener, String language) {
        CallMoviesApi callMoviesApi = retrofitMovie.create(CallMoviesApi.class);
        Call<ApiResponse> call = callMoviesApi.callPopularMovie(api_key, language);

        try {
            call.enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                    if(!response.isSuccessful()) {
                        Toast.makeText(context, "Error!", Toast.LENGTH_SHORT).show();
                    }
                    listener.onFetchMovie(response.body().getMovies(), response.message());
                }

                @Override
                public void onFailure(Call<ApiResponse> call, Throwable t) {
                    listener.onError("Request Failed!");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getUpcomingMovies(Listener listener, String language) {
        CallMoviesApi callMoviesApi = retrofitMovie.create(CallMoviesApi.class);
        Call<ApiResponse> call = callMoviesApi.callUpcomingMovie(api_key, language);

        try {
            call.enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                    if(!response.isSuccessful()) {
                        Toast.makeText(context, "Error!", Toast.LENGTH_SHORT).show();
                    }
                    listener.onFetchMovie(response.body().getMovies(), response.message());
                }

                @Override
                public void onFailure(Call<ApiResponse> call, Throwable t) {
                    listener.onError("Request Failed!");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getRecommandedTv(Listener listener, String language) {
        CallMoviesApi callMoviesApi = retrofitTv.create(CallMoviesApi.class);
        Call<ApiResponse> call = callMoviesApi.callUpcomingTv(api_key, language);

        try {
            call.enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                    if(!response.isSuccessful()) {
                        Toast.makeText(context, "Error!", Toast.LENGTH_SHORT).show();
                    }
                    listener.onFetchMovie(response.body().getMovies(), response.message());
                }

                @Override
                public void onFailure(Call<ApiResponse> call, Throwable t) {
                    listener.onError("Request Failed!");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getSearchResults(Listener listener, String language, String search) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org/3/search/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        CallMoviesApi callMoviesApi = retrofit.create(CallMoviesApi.class);
        Call<ApiResponse> call = callMoviesApi.callSearch(api_key, language, search);
        System.out.println("********" + search);

        try {
            call.enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                    if(!response.isSuccessful()) {
                        Toast.makeText(context, "Error!", Toast.LENGTH_SHORT).show();
                    }
                    listener.onFetchMovie(response.body().getMovies(), response.message());
                }

                @Override
                public void onFailure(Call<ApiResponse> call, Throwable t) {
                    listener.onError("Request Failed!");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Api(Context context) {
        this.context = context;
        api_key = context.getString(R.string.api_key);
    }

    public interface CallMoviesApi {
        @GET("popular")
        Call<ApiResponse> callPopularMovie(
                @Query("api_key") String api_key,
                @Query("language") String language
        );

        @GET("upcoming")
        Call<ApiResponse> callUpcomingMovie(
                @Query("api_key") String api_key,
                @Query("language") String language
        );

        @GET("popular")
        Call<ApiResponse> callUpcomingTv(
                @Query("api_key") String api_key,
                @Query("language") String language
        );

        @GET("movie")
        Call<ApiResponse> callSearch(
                @Query("api_key") String api_key,
                @Query("language") String language,
                @Query("query") String search
        );
    }
}
