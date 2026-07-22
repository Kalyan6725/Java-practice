import { Routes } from '@angular/router';

export const routes: Routes = [
    // show all for '/' path
    {
        path: '',
        loadComponent: () =>
            import('./features/components/show-all/show-all').then((m) => m.ShowAll),
    },
    // add for '/add' path
    {
        path: 'add',
        loadComponent: () =>
            import('./features/components/add-component/add-component').then((m) => m.AddComponent),
    },
    // view single record for '/weather/:id' path
    {
        path: 'weather/:id',
        loadComponent: () =>
            import('./features/components/show-component/show-component').then((m) => m.ShowComponent),
    },
];
