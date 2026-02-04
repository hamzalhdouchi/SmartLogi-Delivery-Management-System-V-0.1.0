import { Routes } from '@angular/router';
import { AuthGuard } from './core/guards/auth.guard';
import { RoleGuard } from './core/guards/role.guard';

export const routes: Routes = [
    { path: '', redirectTo: '/dashboard', pathMatch: 'full' },

    { path: 'login', loadComponent: () => import('./features/auth/login/login.component').then(m => m.LoginComponent) },
    { path: 'register', loadComponent: () => import('./features/auth/register/register.component').then(m => m.RegisterComponent) },
    { path: 'oauth/callback', loadComponent: () => import('./features/auth/oauth-callback/oauth-callback.component').then(m => m.OAuthCallbackComponent) },
   {
        path: 'dashboard',
        loadComponent: () => import('./features/dashboard/dashboard.component').then(m => m.DashboardComponent),
        canActivate: [AuthGuard]
    },

    // Parcels (protected)
    {
        path: 'parcels',
        loadChildren: () => import('./features/parcels/parcels.routes').then(m => m.PARCELS_ROUTES),
        canActivate: [AuthGuard]
    },

    // Clients (Manager only)
    {
        path: 'clients',
        loadChildren: () => import('./features/clients/clients.routes').then(m => m.CLIENTS_ROUTES),
        canActivate: [AuthGuard, RoleGuard],
        data: { roles: ['MANAGER'] }
    },

    // Drivers (Manager only)
    {
        path: 'drivers',
        loadChildren: () => import('./features/drivers/drivers.routes').then(m => m.DRIVERS_ROUTES),
        canActivate: [AuthGuard, RoleGuard],
        data: { roles: ['MANAGER'] }
    },

    // Zones (Manager only)
    {
        path: 'zones',
        loadChildren: () => import('./features/zones/zones.routes').then(m => m.ZONES_ROUTES),
        canActivate: [AuthGuard, RoleGuard],
        data: { roles: ['MANAGER'] }
    },

    // Fallback
    { path: '**', redirectTo: '/dashboard' }
];
