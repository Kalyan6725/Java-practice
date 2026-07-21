import { Component, EventEmitter, Input, Output } from '@angular/core';
import EmployeeDTO from '../../dto/EmployeeDTO';

@Component({
  selector: 'app-employee-detail-modal',
  imports: [],
  templateUrl: './employee-detail-modal.html',
  styleUrl: './employee-detail-modal.css',
})
export class EmployeeDetailModal {
@Input() employee: EmployeeDTO | null = null;
  @Output() close = new EventEmitter<void>();
}
