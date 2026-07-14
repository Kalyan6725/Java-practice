import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-reactive-forms-component',
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './reactive-forms-component.html',
  styleUrl: './reactive-forms-component.css',
})
export class ReactiveFormsComponent {
  form: FormGroup;

  constructor(private formBuilder: FormBuilder) {
    this.form = this.formBuilder.group({
      name: ['', [Validators.required, Validators.minLength(5)]],
      age: [0, [Validators.required, Validators.min(18)]],
      email: ['', [Validators.required, Validators.email]],
    });
  }

  onSubmit() {
    if (this.form.valid) {
      console.log('Reactive Form Submitted:', this.form.value);
      alert('Form submitted successfully!\n' + JSON.stringify(this.form.value, null, 2));
    }
  }
}
