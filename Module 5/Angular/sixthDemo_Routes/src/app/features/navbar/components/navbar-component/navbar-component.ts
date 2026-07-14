import { Component } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { NavigateThroughJs } from '../navigate-through-js/navigate-through-js';

@Component({
  selector: 'app-navbar-component',
  imports: [RouterLink, RouterLinkActive, NavigateThroughJs],
  templateUrl: './navbar-component.html',
  styleUrl: './navbar-component.css',
})
export class NavbarComponent {
  
}
