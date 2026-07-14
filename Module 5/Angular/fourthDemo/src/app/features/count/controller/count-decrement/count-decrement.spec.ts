import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CountDecrement } from './count-decrement';

describe('CountDecrement', () => {
  let component: CountDecrement;
  let fixture: ComponentFixture<CountDecrement>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CountDecrement],
    }).compileComponents();

    fixture = TestBed.createComponent(CountDecrement);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
