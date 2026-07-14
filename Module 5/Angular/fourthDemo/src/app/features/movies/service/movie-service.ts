import { Service } from '@angular/core';
import MovieDTO from '../../../dto/MovieDTO';
import { BehaviorSubject } from 'rxjs';

@Service()
export class MovieService {
    movies:MovieDTO[] =[
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
    private movies$= new BehaviorSubject<MovieDTO[]>(this.movies);

    getMovies(): BehaviorSubject<MovieDTO[]> {
        return this.movies$;
    }

    addMovie(movie: MovieDTO): void {
        const currentMovies = this.movies$.getValue();
        this.movies$.next([...currentMovies, movie]);
    }

    deleteMovie(id: number): void {
        const currentMovies = this.movies$.getValue();
        const filteredMovies = currentMovies.filter(movie => movie.id !== id);
        this.movies$.next(filteredMovies);
    }

    updateMovie(updatedMovie: MovieDTO): void {
        const currentMovies = this.movies$.getValue();
        const index = currentMovies.findIndex(movie => movie.id === updatedMovie.id);
        if (index > -1) {
            currentMovies[index] = updatedMovie;
            this.movies$.next([...currentMovies]);
        }
    }
}
