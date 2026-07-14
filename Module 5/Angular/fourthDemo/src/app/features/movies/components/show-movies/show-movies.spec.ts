import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ShowMovies } from './show-movies';

describe('ShowMovies', () => {
  let component: ShowMovies;
  let fixture: ComponentFixture<ShowMovies>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ShowMovies],
    }).compileComponents();

    fixture = TestBed.createComponent(ShowMovies);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
