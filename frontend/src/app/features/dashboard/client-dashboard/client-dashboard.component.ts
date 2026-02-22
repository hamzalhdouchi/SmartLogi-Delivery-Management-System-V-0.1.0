import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Router } from '@angular/router';
import { Subject, takeUntil } from 'rxjs';
import { ParcelService } from '../../parcels/services/parcel.service';
import { Parcel, ParcelStatus } from '../../parcels/models/parcel.model';

@Component({
    selector: 'app-client-dashboard',
    standalone: true,
    imports: [CommonModule, RouterModule],
    templateUrl: './client-dashboard.component.html',
    styleUrls: ['./client-dashboard.component.css']
})
export class ClientDashboardComponent implements OnInit, OnDestroy {
    myParcels: Parcel[] = [];
    private destroy$ = new Subject<void>();

    constructor(
        private parcelService: ParcelService,
        private router: Router
    ) { }

    ngOnInit(): void {
        this.loadMyParcels();
    }

    loadMyParcels(): void {
        this.parcelService.getMyParcels()
            .pipe(takeUntil(this.destroy$))
            .subscribe({
                next: (parcels) => {
                    this.myParcels = parcels;
                },
                error: (error) => console.error('Error loading parcels:', error)
            });
    }

    createNewDelivery(): void {
        this.router.navigate(['/parcels/new']);
    }

    trackParcel(parcel: Parcel): void {
        this.router.navigate(['/parcels', parcel.id]);
    }

    getStatusBadgeClass(status: ParcelStatus): string {
        const classes: { [key: string]: string } = {
            PENDING: 'bg-warning',
            ASSIGNED: 'bg-info',
            COLLECTED: 'bg-info',
            IN_TRANSIT: 'bg-primary',
            OUT_FOR_DELIVERY: 'bg-primary',
            DELIVERED: 'bg-success',
            FAILED: 'bg-danger',
            CANCELLED: 'bg-secondary',
            RETURNED: 'bg-secondary'
        };
        return classes[status] || 'bg-secondary';
    }

    getStatusLabel(status: ParcelStatus): string {
        const labels: { [key: string]: string } = {
            PENDING: 'En attente',
            ASSIGNED: 'Assigné',
            COLLECTED: 'Collecté',
            IN_TRANSIT: 'En transit',
            OUT_FOR_DELIVERY: 'En livraison',
            DELIVERED: 'Livré',
            FAILED: 'Échec',
            CANCELLED: 'Annulé',
            RETURNED: 'Retourné'
        };
        return labels[status] || status;
    }

    ngOnDestroy(): void {
        this.destroy$.next();
        this.destroy$.complete();
    }
}
