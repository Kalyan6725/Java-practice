import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideRouter } from '@angular/router';
import { AddTrack } from './add-track';
import { Track } from '../../models/track';

describe('AddTrack', () => {
  let httpMock: HttpTestingController;
  const baseUrl = 'http://localhost:8080/music/platform/v1/tracks';

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AddTrack],
      providers: [provideHttpClient(), provideHttpClientTesting(), provideRouter([])],
    }).compileComponents();
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => httpMock.verify());

  it('should create the component', () => {
    const fixture = TestBed.createComponent(AddTrack);
    expect(fixture.componentInstance).toBeTruthy();
  });

  it('should POST a new track on submit', () => {
    const fixture = TestBed.createComponent(AddTrack);
    const component = fixture.componentInstance;
    component.model = { title: 'X', albumName: 'Y', releaseDate: '2024-01-01', playCount: 3 };

    component.onSubmit();

    const req = httpMock.expectOne(baseUrl);
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(component.model);

    const created: Track = { id: 9, title: 'X', albumName: 'Y', releaseDate: '2024-01-01', playCount: 3 };
    req.flush(created);
  });
});
