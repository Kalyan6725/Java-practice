import { Component, inject } from '@angular/core';
import { MovieService } from '../../service/movie-service';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-add-movie',
  imports: [FormsModule],
  templateUrl: './add-movie.html',
  styleUrl: './add-movie.css',
})
export class AddMovie {
  public id:number =0;
  public title:string = '';
  public director:string = '';
  public leadActor:string = '';
  public releaseYear:number = 0;
  movieService: MovieService = inject(MovieService);

  addMovie() {
    if(this.id === 0 || this.title.trim() === '' || this.director.trim() === '' || this.leadActor.trim() === '' || this.releaseYear === 0) {
        alert('Please fill in all fields before adding a movie.');
        return;
      }
    const newMovie = {
      id: this.id,
      title: this.title,
      director: this.director,
      leadActor: this.leadActor,
      releaseYear: this.releaseYear,
    };
    this.movieService.addMovie(newMovie);
    // Reset the form fields after adding the movie
    this.id = 0;
    this.title = '';
    this.director = '';
    this.leadActor = '';
    this.releaseYear = 0;
  }

}
