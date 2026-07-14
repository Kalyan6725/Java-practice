import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-forms-component',
  imports: [FormsModule,CommonModule],
  templateUrl: './forms-component.html',
  styleUrl: './forms-component.css',
})
export class FormsComponent {
  customer: {
    name: string;
    email: string;
    age: number;
  } = {
    name: '',
    email: '',
    age: 0
  }

  onSubmit(form: any) {
    console.log('Form submitted:', this.customer);
  }
}
