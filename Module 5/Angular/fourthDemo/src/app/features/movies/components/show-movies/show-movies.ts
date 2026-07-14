import { Component, inject } from '@angular/core';
import { MovieService } from '../../service/movie-service';
import { AsyncPipe } from '@angular/common';

@Component({
  selector: 'app-show-movies',
  imports: [AsyncPipe],
  templateUrl: './show-movies.html',
  styleUrl: './show-movies.css',
})
export class ShowMovies {
  movieService: MovieService = inject(MovieService);

}
