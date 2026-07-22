import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TrackService } from './track';
import { Track, TrackRequest } from '../models/track';

describe('TrackService', () => {
  let service: TrackService;
  let httpMock: HttpTestingController;
  const baseUrl = 'http://localhost:8080/music/platform/v1/tracks';

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [TrackService, provideHttpClient(), provideHttpClientTesting()],
    });
    service = TestBed.inject(TrackService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  // GET /music/platform/v1/tracks
  it('getAllTracks() should GET the list of tracks', () => {
    const mockTracks: Track[] = [
      { id: 1, title: 'SONG A', albumName: 'Album A', releaseDate: '2024-01-01', playCount: 10 },
      { id: 2, title: 'SONG B', albumName: 'Album B', releaseDate: '2024-02-01', playCount: 20 },
    ];

    service.getAllTracks().subscribe((tracks) => {
      expect(tracks).toEqual(mockTracks);
      expect(tracks.length).toBe(2);
    });

    const req = httpMock.expectOne(baseUrl);
    expect(req.request.method).toBe('GET');
    req.flush(mockTracks);
  });

  // POST /music/platform/v1/tracks
  it('createTrack() should POST a new track', () => {
    const request: TrackRequest = {
      title: 'New Song',
      albumName: 'New Album',
      releaseDate: '2024-05-01',
      playCount: 0,
    };
    const created: Track = { id: 3, title: 'NEW SONG', albumName: 'New Album', releaseDate: '2024-05-01', playCount: 0 };

    service.createTrack(request).subscribe((track) => {
      expect(track).toEqual(created);
    });

    const req = httpMock.expectOne(baseUrl);
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(request);
    req.flush(created);
  });

  // DELETE /music/platform/v1/tracks/{trackId}
  it('deleteTrack() should DELETE a track by id', () => {
    service.deleteTrack(5).subscribe((response) => {
      expect(response).toBeNull();
    });

    const req = httpMock.expectOne(`${baseUrl}/5`);
    expect(req.request.method).toBe('DELETE');
    req.flush(null);
  });

  // GET /music/platform/v1/tracks/search?title=
  it('searchByTitle() should GET a track by title', () => {
    const found: Track = { id: 1, title: 'SONG A', albumName: 'Album A', releaseDate: '2024-01-01', playCount: 10 };

    service.searchByTitle('Song A').subscribe((track) => {
      expect(track).toEqual(found);
    });

    const req = httpMock.expectOne((r) => r.url === `${baseUrl}/search`);
    expect(req.request.method).toBe('GET');
    expect(req.request.params.get('title')).toBe('Song A');
    req.flush(found);
  });
});
