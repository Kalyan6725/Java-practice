import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CountIncrementByValue } from './count-increment-by-value';

describe('CountIncrementByValue', () => {
  let component: CountIncrementByValue;
  let fixture: ComponentFixture<CountIncrementByValue>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CountIncrementByValue],
    }).compileComponents();

    fixture = TestBed.createComponent(CountIncrementByValue);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
