import { Service, signal, WritableSignal } from '@angular/core';
import MovieDTO from '../../../dto/MovieDTO';

@Service()
export class MovieService {
    moviesList:MovieDTO[] =[
        {
            id: 1,
            title: 'Inception',
            leadActor: 'Leonardo DiCaprio',
            director: 'Christopher Nolan',
            releaseYear: 2010
        },
        {
            id: 2,
            title: 'The Dark Knight',
            leadActor: 'Christian Bale',
            director: 'Christopher Nolan',
            releaseYear: 2008
        },
        {
            id: 3,
            title: 'Pulp Fiction',
            leadActor: 'John Travolta',
            director: 'Quentin Tarantino',
            releaseYear: 1994
        }
    ]
    private movies: WritableSignal<MovieDTO[]> = signal(this.moviesList);

    getMovies(): WritableSignal<MovieDTO[]> {
        return this.movies;
    }

    addMovie(movie: MovieDTO): void {
        const currentMovies = this.movies();
        this.movies.set([...currentMovies, movie]);
    }

    deleteMovie(id: number): void {
        const currentMovies = this.movies();
        const filteredMovies = currentMovies.filter(movie => movie.id !== id);
        this.movies.set(filteredMovies);
    }

    updateMovie(updatedMovie: MovieDTO): void {
        const currentMovies = this.movies();
        const index = currentMovies.findIndex(movie => movie.id === updatedMovie.id);
        if(index==-1){
            alert("Movie not found with id :"+updatedMovie.id);
        }
        if (index > -1) {
            currentMovies[index] = updatedMovie;
            this.movies.set([...currentMovies]);
        }
    }
}
