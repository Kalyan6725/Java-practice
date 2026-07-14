import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink, RouterLinkActive } from '@angular/router';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [CommonModule, RouterLink, RouterLinkActive],
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent {
  @Input() userType: 'guest' | 'customer' | 'admin' = 'guest';
  @Input() userName: string = 'Guest';
  @Input() notificationCount: number = 0;

  isCollapsed = true;

  toggleMenu() {
    this.isCollapsed = !this.isCollapsed;
  }

  logout() {
    // Logout logic will be implemented when integrating with backend
    console.log('Logout clicked');
  }
}
