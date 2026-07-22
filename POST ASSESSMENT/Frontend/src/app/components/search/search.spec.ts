import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';
import { Search } from './search';
import { Track } from '../../models/track';

describe('Search', () => {
  let httpMock: HttpTestingController;
  const baseUrl = 'http://localhost:8080/music/platform/v1/tracks';

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Search],
      providers: [
        provideHttpClient(),
        provideHttpClientTesting(),
        { provide: ActivatedRoute, useValue: { queryParams: of({ title: 'Song A' }) } },
      ],
    }).compileComponents();
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => httpMock.verify());

  it('should GET a track by title from the query param (search endpoint)', () => {
    const fixture = TestBed.createComponent(Search);
    fixture.detectChanges();

    const req = httpMock.expectOne((r) => r.url === `${baseUrl}/search`);
    expect(req.request.method).toBe('GET');
    expect(req.request.params.get('title')).toBe('Song A');

    const found: Track = { id: 1, title: 'SONG A', albumName: 'AL', releaseDate: '2024-01-01', playCount: 5 };
    req.flush(found);

    expect(fixture.componentInstance.track()).toEqual(found);
  });
});
