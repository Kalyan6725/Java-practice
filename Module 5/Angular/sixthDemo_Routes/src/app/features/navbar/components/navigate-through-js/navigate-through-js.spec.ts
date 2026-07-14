import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NavigateThroughJs } from './navigate-through-js';

describe('NavigateThroughJs', () => {
  let component: NavigateThroughJs;
  let fixture: ComponentFixture<NavigateThroughJs>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [NavigateThroughJs],
    }).compileComponents();

    fixture = TestBed.createComponent(NavigateThroughJs);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
