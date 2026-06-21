package com.northernArc.SpringBootDemo.dao;

import com.northernArc.SpringBootDemo.entity.Movie;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
@Component
public class MovieDaoImpl implements MovieDao{
    List<Movie> movies = new ArrayList<>();
    @PostConstruct
    public void init(){
        //dumby data
        movies.add(new Movie(1,"The Shawshank Redemption", "Frank Darabont", 1994, "Drama", 9.3));
        movies.add(new Movie(2,"The Godfather", "Francis Ford Coppola", 1972, "Crime", 9.2));
        movies.add(new Movie(3,"The Dark Knight", "Christopher Nolan", 2008, "Action", 9.0));
        movies.add(new Movie(4,"Pulp Fiction", "Quentin Tarantino", 1994, "Crime", 8.9));
    }
    @PreDestroy
    public void destroy(){
        movies.clear();
    }

    @Override
    public void save(Movie movie) {
        movies.add(movie);
    }

    @Override
    public Movie getMovieById(int id) {
        for (Movie movie : movies) {
            if (movie.getId() == id) {
                return movie;
            }
        }
        return null;
    }
    @Override
    public List<Movie> getAllMovies() {
        return movies;
    }

    @Override
    public void update(Movie movie) {

        for (int i = 0; i < movies.size(); i++) {
            if (movies.get(i).getId() == movie.getId()) {
                movies.set(i, movie);
                return;
            }
        }
    }

    @Override
    public void delete(int id) {
        for (int i = 0; i < movies.size(); i++) {
            if (movies.get(i).getId() == id) {
                movies.remove(i);
                return;
            }
        }
    }
}
