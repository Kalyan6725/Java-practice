import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-contact',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="contact-container container py-5">
      <h2>Contact Us</h2>
      <p>Get in touch with us for any queries or assistance.</p>
      <div class="row mt-4">
        <div class="col-md-6">
          <h4>Contact Information</h4>
          <p><i class="bi bi-telephone"></i> +91 1234567890</p>
          <p><i class="bi bi-envelope"></i> support@loanmanagement.com</p>
          <p><i class="bi bi-geo-alt"></i> 123 Business Street, City, State</p>
        </div>
        <div class="col-md-6">
          <h4>Send us a message</h4>
          <form>
            <div class="mb-3">
              <input type="text" class="form-control" placeholder="Name">
            </div>
            <div class="mb-3">
              <input type="email" class="form-control" placeholder="Email">
            </div>
            <div class="mb-3">
              <textarea class="form-control" rows="4" placeholder="Message"></textarea>
            </div>
            <button type="submit" class="btn btn-primary">Send Message</button>
          </form>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .contact-container {
      min-height: calc(100vh - 120px);
    }
  `]
})
export class ContactComponent {
}
