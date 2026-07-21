import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { ArtistService } from '../../services/artist-service';

@Component({
  selector: 'app-show-artists',
  imports: [CommonModule, RouterLink],
  templateUrl: './show-artists.html',
  styleUrl: './show-artists.css',
})
export class ShowArtists {
  artistService = inject(ArtistService);

  deleteArtist(id: number) {
    if (confirm('Are you sure you want to delete this artist?')) {
      this.artistService.deleteArtist(id);
    }
  }
}
