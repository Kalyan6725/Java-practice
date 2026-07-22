import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ShowAll } from './show-all';

describe('ShowAll', () => {
  let component: ShowAll;
  let fixture: ComponentFixture<ShowAll>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ShowAll],
    }).compileComponents();

    fixture = TestBed.createComponent(ShowAll);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
