import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { Subject, takeUntil } from 'rxjs';
import { ParcelService } from '../../parcels/services/parcel.service';
import { Parcel, ParcelStatus, Priority } from '../../parcels/models/parcel.model';

@Component({
    selector: 'app-driver-dashboard',
    standalone: true,
    imports: [CommonModule, RouterModule, FormsModule],
    templateUrl: './driver-dashboard.component.html',
    styleUrls: ['./driver-dashboard.component.css']
})
export class DriverDashboardComponent implements OnInit, OnDestroy {
    assignedParcels: Parcel[] = [];
    filteredParcels: Parcel[] = [];
    statusFilter = '';
    priorityFilter = '';
    private destroy$ = new Subject<void>();

    statusOptions = Object.values(ParcelStatus);
    priorityOptions = Object.values(Priority);

    constructor(private parcelService: ParcelService) { }

    ngOnInit(): void {
        this.loadAssignedParcels();
    }

    loadAssignedParcels(): void {
        this.parcelService.getMyParcels()
            .pipe(takeUntil(this.destroy$))
            .subscribe({
                next: (parcels) => {
                    this.assignedParcels = parcels;
                    this.applyFilters();
                },
                error: (error) => console.error('Error loading assigned parcels:', error)
            });
    }

    applyFilters(): void {
        this.filteredParcels = this.assignedParcels.filter(parcel => {
            const statusMatch = !this.statusFilter || parcel.status === this.statusFilter;
            const priorityMatch = !this.priorityFilter || parcel.priority === this.priorityFilter;
            return statusMatch && priorityMatch;
        });
    }

    quickStatusUpdate(parcelId: number, status: string): void {
        this.parcelService.updateStatus(parcelId, status as ParcelStatus)
            .pipe(takeUntil(this.destroy$))
            .subscribe({
                next: () => this.loadAssignedParcels(),
                error: (error) => console.error('Error updating status:', error)
            });
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

    getPriorityBadgeClass(priority: Priority): string {
        const classes: { [key: string]: string } = {
            LOW: 'bg-secondary',
            NORMAL: 'bg-primary',
            HIGH: 'bg-warning',
            URGENT: 'bg-danger'
        };
        return classes[priority] || 'bg-secondary';
    }

    ngOnDestroy(): void {
        this.destroy$.next();
        this.destroy$.complete();
    }
}
