export interface Track {
  id: number;
  title: string;
  albumName: string;
  releaseDate: string;
  playCount: number;
}

export interface TrackRequest {
  title: string;
  albumName: string;
  releaseDate: string;
  playCount: number;
}
