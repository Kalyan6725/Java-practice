import { Routes } from '@angular/router';
import { Main } from './components/main/main';
import { ShowAll } from './components/show-all/show-all';
import { AddTrack } from './components/add-track/add-track';
import { Search } from './components/search/search';

export const routes: Routes = [
  {
    path: '',
    component: Main,
    children: [
      { path: '', component: ShowAll },
      { path: 'add', component: AddTrack },
      { path: 'search', component: Search },
    ],
  },
];
