import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CountDecrementByValue } from './count-decrement-by-value';

describe('CountDecrementByValue', () => {
  let component: CountDecrementByValue;
  let fixture: ComponentFixture<CountDecrementByValue>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CountDecrementByValue],
    }).compileComponents();

    fixture = TestBed.createComponent(CountDecrementByValue);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
