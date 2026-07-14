import { Component ,inject} from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-navigate-through-js',
  imports: [FormsModule],
  templateUrl: './navigate-through-js.html',
  styleUrl: './navigate-through-js.css',
})
export class NavigateThroughJs {
  private router = inject(Router);
  public navName: string = '';
  navigateToPerson() {

    if(this.navName.trim().toLowerCase() == 'about') {
      this.router.navigate(['/about']);
      this.navName = '';
    }
    else if(this.navName.trim().toLowerCase() == 'services') {
      this.router.navigate(['/services']);
      this.navName = '';
    }
    else if(this.navName.trim().toLowerCase() == 'contact') {
      this.router.navigate(['/contact']);
      this.navName = '';
    }
    else if(this.navName.trim().toLowerCase() == 'home') {
      this.router.navigate(['/home']);
      this.navName = '';
    }
    else {
      alert('Please enter a valid nav icon name');
      this.navName = '';
    }
  }
}
