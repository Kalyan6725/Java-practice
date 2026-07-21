import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ShowArtists } from './show-artists';

describe('ShowArtists', () => {
  let component: ShowArtists;
  let fixture: ComponentFixture<ShowArtists>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ShowArtists],
    }).compileComponents();

    fixture = TestBed.createComponent(ShowArtists);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
