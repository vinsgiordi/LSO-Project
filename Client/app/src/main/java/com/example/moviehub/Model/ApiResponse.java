package com.example.moviehub.Model;



import java.io.Serializable;
import java.util.List;

public class ApiResponse implements Serializable {
    String status;
    int totalResults;
    List<Movie> results;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public List<Movie> getMovies() {
        return results;
    }

    public void setMovies(List<Movie> results) {
        this.results = results;
    }
}
