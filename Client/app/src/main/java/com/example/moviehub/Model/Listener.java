package com.example.moviehub.Model;
import java.util.List;

public interface Listener<MovieApiResponse> {
    void onFetchMovie(List<Movie> list, String message);
    void onError(String message);
}
