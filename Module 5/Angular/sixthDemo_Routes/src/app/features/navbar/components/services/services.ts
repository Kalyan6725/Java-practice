import { Component } from '@angular/core';
import { ShowEmployeesComponent } from '../../../employees/components/show-employees-component/show-employees-component';

@Component({
  selector: 'app-services',
  imports: [ShowEmployeesComponent],
  templateUrl: './services.html',
  styleUrl: './services.css',
})
export class Services {}
