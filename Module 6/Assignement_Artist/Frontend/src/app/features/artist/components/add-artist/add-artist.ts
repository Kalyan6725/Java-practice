import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { ArtistService } from '../../services/artist-service';

@Component({
  selector: 'app-add-artist',
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './add-artist.html',
  styleUrl: './add-artist.css',
})
export class AddArtist {
  private artistService = inject(ArtistService);
  private router = inject(Router);

  firstName: string = '';
  lastName: string = '';

  onSubmit(form: any) {
    if (form.valid) {
      this.artistService.createArtist({
        firstName: this.firstName,
        lastName: this.lastName
      });
      
      // Reset form and navigate back
      form.reset();
      this.firstName = '';
      this.lastName = '';
      this.router.navigate(['/']);
    }
  }
}
