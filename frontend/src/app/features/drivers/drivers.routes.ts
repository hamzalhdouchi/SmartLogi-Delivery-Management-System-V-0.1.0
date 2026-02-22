import { Routes } from '@angular/router';

export const DRIVERS_ROUTES: Routes = [
    {
        path: '',
        loadComponent: () => import('./driver-list/driver-list.component').then(m => m.DriverListComponent)
    }
];
