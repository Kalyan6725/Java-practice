import { Component, inject } from '@angular/core';
import { MovieService } from '../../service/movie-service';

@Component({
  selector: 'app-show-movies',
  imports: [],
  templateUrl: './show-movies.html',
  styleUrl: './show-movies.css',
})
export class ShowMovies {
  movieService: MovieService = inject(MovieService);

}
