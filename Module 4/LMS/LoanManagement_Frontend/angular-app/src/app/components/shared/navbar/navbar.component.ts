import { Component, computed, inject } from '@angular/core';
import { RouterLink, RouterLinkActive, Router } from '@angular/router';
import { AuthService } from '../../../core/auth.service';

interface NavLink {
  path: string;
  label: string;
}

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [RouterLink, RouterLinkActive],
  template: `
    <nav class="navbar">
      <div class="inner">
        <a class="brand" [routerLink]="homeLink()">
          <span class="mark"><span class="tri"></span><span class="star">&#10022;</span></span>
          <span class="name"><b>N<span class="dot">O</span>RTHERN</b><span>ARC</span></span>
        </a>
        @if (area() !== 'public') {
          <span class="pill">{{ area() === 'staff' ? 'STAFF' : 'CUSTOMER' }}</span>
        }
        <div class="links">
          @for (link of links(); track link.path) {
            <a [routerLink]="link.path" routerLinkActive="active"
               [routerLinkActiveOptions]="{ exact: link.path === '/' }">{{ link.label }}</a>
          }
          <span style="width:8px"></span>
          @if (auth.isLoggedIn()) {
            <button class="btn btn-outline btn-sm" (click)="logout()">Logout</button>
          } @else {
            <a class="btn btn-outline btn-sm" routerLink="/login">Login</a>
            <a class="btn btn-primary btn-sm" routerLink="/register">Register</a>
          }
        </div>
      </div>
    </nav>
  `,
})
export class NavbarComponent {
  readonly auth = inject(AuthService);
  private readonly router = inject(Router);

  private readonly publicLinks: NavLink[] = [
    { path: '/', label: 'Home' },
    { path: '/loan-products', label: 'Loan Products' },
    { path: '/contact', label: 'Contact' },
  ];
  private readonly customerLinks: NavLink[] = [
    { path: '/customer/dashboard', label: 'Dashboard' },
    { path: '/loan-products', label: 'Products' },
    { path: '/customer/apply', label: 'Apply' },
    { path: '/customer/applications', label: 'Applications' },
    { path: '/customer/loans', label: 'My Loans' },
    { path: '/customer/profile', label: 'Profile' },
  ];
  private readonly staffLinks: NavLink[] = [
    { path: '/admin/dashboard', label: 'Dashboard' },
    { path: '/admin/customers', label: 'Customers' },
    { path: '/admin/loan-products', label: 'Products' },
    { path: '/admin/applications', label: 'Applications' },
    { path: '/admin/loan-accounts', label: 'Accounts' },
    { path: '/admin/payments', label: 'Payments' },
  ];

  readonly area = computed<'public' | 'customer' | 'staff'>(() => {
    if (!this.auth.isLoggedIn()) return 'public';
    return this.auth.isStaff() ? 'staff' : 'customer';
  });

  readonly links = computed<NavLink[]>(() => {
    switch (this.area()) {
      case 'staff':
        return this.staffLinks;
      case 'customer':
        return this.customerLinks;
      default:
        return this.publicLinks;
    }
  });

  readonly homeLink = computed(() => {
    switch (this.area()) {
      case 'staff':
        return '/admin/dashboard';
      case 'customer':
        return '/customer/dashboard';
      default:
        return '/';
    }
  });

  logout(): void {
    this.auth.logout();
    this.router.navigate(['/login']);
  }
}
