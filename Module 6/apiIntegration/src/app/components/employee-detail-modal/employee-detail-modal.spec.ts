import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EmployeeDetailModal } from './employee-detail-modal';

describe('EmployeeDetailModal', () => {
  let component: EmployeeDetailModal;
  let fixture: ComponentFixture<EmployeeDetailModal>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EmployeeDetailModal]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EmployeeDetailModal);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
