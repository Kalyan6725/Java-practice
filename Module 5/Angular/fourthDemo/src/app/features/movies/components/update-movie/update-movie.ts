import { Component, inject } from '@angular/core';
import { MovieService } from '../../service/movie-service';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-update-movie',
  imports: [FormsModule],
  templateUrl: './update-movie.html',
  styleUrl: './update-movie.css',
})
export class UpdateMovie {
  public id: number = 0;
  public title: string = '';
  public director: string = '';
  public leadActor: string = '';
  public releaseYear: number = 0;
  movieService: MovieService = inject(MovieService);

  updateMovie() {
    if(this.id === 0 || this.title.trim() === '' || this.director.trim() === '' || this.leadActor.trim() === '' || this.releaseYear === 0) {
      alert('Please fill in all fields before updating a movie.');
      return;
    }
    const updatedMovie = {
      id: this.id,
      title: this.title,
      director: this.director,
      leadActor: this.leadActor,
      releaseYear: this.releaseYear,
    };
    this.movieService.updateMovie(updatedMovie);
    alert('Movie updated successfully!');
    // Reset the form fields after updating the movie
    this.id = 0;
    this.title = '';
    this.director = '';
    this.leadActor = '';
    this.releaseYear = 0;
  }
}
