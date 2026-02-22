import { Routes } from '@angular/router';
import { ParcelListComponent } from './parcel-list/parcel-list.component';
import { ParcelDetailComponent } from './parcel-detail/parcel-detail.component';

export const PARCELS_ROUTES: Routes = [
    { path: '', component: ParcelListComponent },
    { path: ':id', component: ParcelDetailComponent }
];
