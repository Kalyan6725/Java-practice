import { Component, inject } from '@angular/core';
import { Router, RouterLink, RouterLinkActive } from '@angular/router';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-navbar',
  imports: [RouterLink, RouterLinkActive, FormsModule],
  templateUrl: './navbar.html',
  styleUrl: './navbar.css',
})
export class Navbar {
  private readonly router = inject(Router);
  searchTerm = '';

  onSearch(): void {
    const term = this.searchTerm.trim();
    if (term) {
      this.router.navigate(['/search'], { queryParams: { title: term } });
      this.searchTerm = '';
    }
  }
}
