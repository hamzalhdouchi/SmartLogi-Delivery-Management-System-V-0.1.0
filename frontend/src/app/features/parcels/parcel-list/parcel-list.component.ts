import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { Subject, takeUntil } from 'rxjs';
import { ParcelService } from '../services/parcel.service';
import { Parcel, ParcelStatus, Priority } from '../models/parcel.model';
import { AuthService } from '../../../core/services/auth.service';
import { Role } from '../../../core/models/role.enum';

@Component({
    selector: 'app-parcel-list',
    standalone: true,
    imports: [CommonModule, RouterModule, FormsModule],
    templateUrl: './parcel-list.component.html',
    styleUrls: ['./parcel-list.component.css']
})
export class ParcelListComponent implements OnInit, OnDestroy {
    parcels: Parcel[] = [];
    totalElements = 0;
    totalPages = 0;
    pageSize = 10;
    currentPage = 0;

    filters = {
        status: '',
        priority: '',
        search: ''
    };

    statusOptions = Object.values(ParcelStatus);
    priorityOptions = Object.values(Priority);

    private destroy$ = new Subject<void>();

    constructor(
        private parcelService: ParcelService,
        public authService: AuthService
    ) { }

    ngOnInit(): void {
        this.loadParcels();
    }

    loadParcels(): void {
        const filterParams: any = {};
        if (this.filters.status) filterParams.status = this.filters.status;
        if (this.filters.priority) filterParams.priority = this.filters.priority;
        if (this.filters.search) filterParams.search = this.filters.search;

        this.parcelService.getAllParcels(this.currentPage, this.pageSize, filterParams)
            .pipe(takeUntil(this.destroy$))
            .subscribe({
                next: (response) => {
                    this.parcels = response.content;
                    this.totalElements = response.totalElements;
                    this.totalPages = response.totalPages;
                },
                error: (error) => console.error('Error loading parcels:', error)
            });
    }

    onPageChange(page: number): void {
        this.currentPage = page;
        this.loadParcels();
    }

    applyFilters(): void {
        this.currentPage = 0;
        this.loadParcels();
    }

    resetFilters(): void {
        this.filters = { status: '', priority: '', search: '' };
        this.applyFilters();
    }

    isManager(): boolean {
        return this.authService.hasRole(Role.MANAGER);
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
            LOW: 'text-bg-secondary',
            NORMAL: 'text-bg-primary',
            HIGH: 'text-bg-warning',
            URGENT: 'text-bg-danger'
        };
        return classes[priority] || 'text-bg-secondary';
    }

    get pages(): number[] {
        return Array.from({ length: this.totalPages }, (_, i) => i);
    }

    ngOnDestroy(): void {
        this.destroy$.next();
        this.destroy$.complete();
    }
}
