import { Routes } from '@angular/router';
import { ScanListComponent } from './components/scan-list/scan-list.component';
import { ScanCreateComponent } from './components/scan-create/scan-create.component';
import { ScanDetailComponent } from './components/scan-detail/scan-detail.component';
import { ScanSearchComponent } from './components/scan-search/scan-search.component';
import { NotFoundComponent } from './components/not-found/not-found.component';

export const routes: Routes = [
  { path: '', redirectTo: '/scans', pathMatch: 'full' },
  { path: 'scans', component: ScanListComponent },
  { path: 'scans/add', component: ScanCreateComponent },
  { path: 'scans/search', component: ScanSearchComponent },
  { path: 'scans/:id', component: ScanDetailComponent },
  { path: '**', component: NotFoundComponent }
];
