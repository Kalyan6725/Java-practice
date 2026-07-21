import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-footer',
  standalone: true,
  imports: [RouterLink],
  template: `
    <footer class="footer">
      <div class="container inner">
        <a class="brand" routerLink="/">
          <span class="mark"><span class="tri"></span><span class="star">&#10022;</span></span>
          <span class="name"><b>N<span class="dot">O</span>RTHERN</b><span>ARC</span></span>
        </a>
        <div class="muted" style="font-size:.85rem">
          &copy; {{ year }} Northern Arc Loan Management.
        </div>
        <div class="actions">
          <a routerLink="/contact">Contact</a>
          <a href="#">Privacy</a>
          <a href="#">Terms</a>
        </div>
      </div>
    </footer>
  `,
})
export class FooterComponent {
  readonly year = new Date().getFullYear();
}
