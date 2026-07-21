import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ShowArtist } from './show-artist';

describe('ShowArtist', () => {
  let component: ShowArtist;
  let fixture: ComponentFixture<ShowArtist>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ShowArtist],
    }).compileComponents();

    fixture = TestBed.createComponent(ShowArtist);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
