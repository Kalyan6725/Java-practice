import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CountIncrement } from './count-increment';

describe('CountIncrement', () => {
  let component: CountIncrement;
  let fixture: ComponentFixture<CountIncrement>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CountIncrement],
    }).compileComponents();

    fixture = TestBed.createComponent(CountIncrement);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
