import { Component, signal } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [RouterLink, RouterLinkActive],
  templateUrl: './navbar.component.html'
})
export class NavbarComponent {
  isCollapsed = signal(true);

  toggleNav(): void {
    this.isCollapsed.set(!this.isCollapsed());
  }

  closeNav(): void {
    this.isCollapsed.set(true);
  }
}
