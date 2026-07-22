import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { ShowAll } from './show-all';
import { Track } from '../../models/track';

describe('ShowAll', () => {
  let httpMock: HttpTestingController;
  const baseUrl = 'http://localhost:8080/music/platform/v1/tracks';

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ShowAll],
      providers: [provideHttpClient(), provideHttpClientTesting()],
    }).compileComponents();
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => httpMock.verify());

  it('should create and load all tracks (GET)', () => {
    const fixture = TestBed.createComponent(ShowAll);
    fixture.detectChanges();

    const mock: Track[] = [
      { id: 1, title: 'A', albumName: 'AL', releaseDate: '2024-01-01', playCount: 1 },
    ];
    const req = httpMock.expectOne(baseUrl);
    expect(req.request.method).toBe('GET');
    req.flush(mock);

    expect(fixture.componentInstance.tracks()).toEqual(mock);
  });

  it('should delete a track (DELETE) and remove it from the list', () => {
    const fixture = TestBed.createComponent(ShowAll);
    fixture.detectChanges();

    httpMock.expectOne(baseUrl).flush([
      { id: 1, title: 'A', albumName: 'AL', releaseDate: '2024-01-01', playCount: 1 },
    ]);

    fixture.componentInstance.deleteTrack(1);
    const delReq = httpMock.expectOne(`${baseUrl}/1`);
    expect(delReq.request.method).toBe('DELETE');
    delReq.flush(null);

    expect(fixture.componentInstance.tracks().length).toBe(0);
  });
});
