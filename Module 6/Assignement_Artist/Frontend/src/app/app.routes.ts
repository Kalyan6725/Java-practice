import { Routes } from '@angular/router';
import { ShowArtists } from './features/artist/components/show-artists/show-artists';
import { AddArtist } from './features/artist/components/add-artist/add-artist';
import { ShowArtist } from './features/artist/components/show-artist/show-artist';

export const routes: Routes = [
  {
    path: '',
    component: ShowArtists
  },
  {
    path: 'artist/:id',
    component: ShowArtist
  },
  {
    path: 'add-artist',
    component: AddArtist
  },
];
