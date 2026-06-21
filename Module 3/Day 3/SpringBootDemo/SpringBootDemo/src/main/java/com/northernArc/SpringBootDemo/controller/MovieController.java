package com.northernArc.SpringBootDemo.controller;

import com.northernArc.SpringBootDemo.dao.MovieDao;
import com.northernArc.SpringBootDemo.entity.Movie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Component
//@RequestMapping("/api/movies")
public class MovieController {
    @Autowired
    public MovieDao movieDao;

    @RequestMapping("/movies/add/{id}/{title}/{director}/{releaseYear}/{genre}/{rating}")
    public String addMovie(@PathVariable int id, @PathVariable String title, @PathVariable String director,
                           @PathVariable int releaseYear, @PathVariable String genre, @PathVariable double rating) {
        movieDao.save(new Movie(id, title, director, releaseYear, genre, rating));
        return "Movie added successfully";
    }

    @RequestMapping("/movies/{id}")
    public String findById(@PathVariable int id) {
        Movie movie = movieDao.getMovieById(id);
        if (movie != null) {
            return movie.toString();
        } else {
            return "Movie not found";
        }
    }
    @RequestMapping("movies")
    public List<Movie> findAllMovies() {
        return (movieDao.getAllMovies());
    }

    @RequestMapping("/movies/update/{id}/{title}/{director}/{releaseYear}/{genre}/{rating}")
    public String updateMovie(@PathVariable int id, @PathVariable String title, @PathVariable String director,
                              @PathVariable int releaseYear, @PathVariable String genre, @PathVariable double rating) {
        Movie movie = new Movie(id, title, director, releaseYear, genre, rating);
        movieDao.update(movie);
        return "Movie updated successfully";
    }
    @RequestMapping("/movies/delete/{id}")
    public String deleteMovieById(@PathVariable int id){
        movieDao.delete(id);
        return "Movie deleted successfully";
    }
}
