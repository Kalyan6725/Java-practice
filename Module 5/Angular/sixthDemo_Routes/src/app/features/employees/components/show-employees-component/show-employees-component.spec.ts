import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ShowEmployeesComponent } from './show-employees-component';

describe('ShowEmployeesComponent', () => {
  let component: ShowEmployeesComponent;
  let fixture: ComponentFixture<ShowEmployeesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ShowEmployeesComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(ShowEmployeesComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
