import { Routes } from '@angular/router';

import { Home } from './features/navbar/components/home/home';
import { Services } from './features/navbar/components/services/services';
import { About } from './features/navbar/components/about/about';
import { Contact } from './features/navbar/components/contact/contact';

export const routes: Routes = [
    {path: '',component: Home},
    {path: 'home',component: Home},
    {path: 'services',component: Services},
    {path: 'about',component: About},
    {path: 'contact',component: Contact},
    {path: '**',redirectTo: ''}
];
