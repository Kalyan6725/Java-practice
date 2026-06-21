package com.northernArc.SpringBootDemo.dao;

import com.northernArc.SpringBootDemo.entity.Movie;
import java.util.List;

public interface MovieDao {
    public void save(Movie movie);
    public List<Movie> getAllMovies();
    public Movie getMovieById(int id);
    public void update(Movie movie);
    public void delete(int id);
}
