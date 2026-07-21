import { Component, inject, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { ArtistService } from '../../services/artist-service';
import { Artist } from '../../models/artist.model';

@Component({
  selector: 'app-show-artist',
  imports: [CommonModule, RouterLink],
  templateUrl: './show-artist.html',
  styleUrl: './show-artist.css',
})
export class ShowArtist implements OnInit {
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private artistService = inject(ArtistService);
  
  artist = signal<Artist | null>(null);
  loading = signal(true);

  ngOnInit() {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.artistService.getArtistById(id).subscribe({
      next: (data) => {
        this.artist.set(data);
        this.loading.set(false);
      },
      error: (err) => {
        console.error('Error loading artist:', err);
        this.loading.set(false);
      }
    });
  }

  deleteArtist() {
    if (this.artist() && confirm('Are you sure you want to delete this artist?')) {
      this.artistService.deleteArtist(this.artist()!.id);
      this.router.navigate(['/']);
    }
  }
}
