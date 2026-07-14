import { Component } from '@angular/core';
import { ShowMovies } from '../show-movies/show-movies';
import { AddMovie } from '../add-movie/add-movie';
import { UpdateMovie } from '../update-movie/update-movie';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-movies-component',
  imports: [ShowMovies, AddMovie, UpdateMovie, CommonModule],
  templateUrl: './movies-component.html',
  styleUrl: './movies-component.css',
})
export class MoviesComponent {
  isAddMode: boolean = true;

  toggleMode() {
    this.isAddMode = !this.isAddMode;
  }
}
