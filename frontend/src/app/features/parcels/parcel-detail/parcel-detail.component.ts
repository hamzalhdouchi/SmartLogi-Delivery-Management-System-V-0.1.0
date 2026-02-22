import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, ActivatedRoute, Router } from '@angular/router';
import { Subject, takeUntil } from 'rxjs';
import { ParcelService } from '../services/parcel.service';
import { Parcel, ParcelStatus } from '../models/parcel.model';
import { AuthService } from '../../../core/services/auth.service';
import { NotificationService } from '../../../core/services/notification.service';
import { Role } from '../../../core/models/role.enum';

@Component({
    selector: 'app-parcel-detail',
    standalone: true,
    imports: [CommonModule, RouterModule],
    templateUrl: './parcel-detail.component.html',
    styleUrls: ['./parcel-detail.component.css']
})
export class ParcelDetailComponent implements OnInit, OnDestroy {
    parcel: Parcel | null = null;
    isLoading = true;
    private destroy$ = new Subject<void>();

    constructor(
        private route: ActivatedRoute,
        private router: Router,
        private parcelService: ParcelService,
        private authService: AuthService,
        private notificationService: NotificationService
    ) { }

    ngOnInit(): void {
        this.route.params.subscribe(params => {
            const id = +params['id'];
            if (id) {
                this.loadParcel(id);
            }
        });
    }

    loadParcel(id: number): void {
        this.isLoading = true;
        this.parcelService.getParcelById(id)
            .pipe(takeUntil(this.destroy$))
            .subscribe({
                next: (parcel) => {
                    this.parcel = parcel;
                    this.isLoading = false;
                },
                error: (error) => {
                    console.error('Error loading parcel:', error);
                    this.isLoading = false;
                    this.notificationService.error('Colis non trouvé');
                    this.router.navigate(['/parcels']);
                }
            });
    }

    isManager(): boolean {
        return this.authService.hasRole(Role.MANAGER);
    }

    canUpdateStatus(): boolean {
        return this.authService.hasRole(Role.DRIVER) || this.authService.hasRole(Role.MANAGER);
    }

    updateStatus(status: string): void {
        if (!this.parcel?.id) return;

        this.parcelService.updateStatus(this.parcel.id, status as ParcelStatus)
            .pipe(takeUntil(this.destroy$))
            .subscribe({
                next: (updatedParcel) => {
                    this.parcel = updatedParcel;
                    this.notificationService.success('Statut mis à jour');
                },
                error: (error) => {
                    console.error('Error updating status:', error);
                    this.notificationService.error('Erreur lors de la mise à jour');
                }
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

    ngOnDestroy(): void {
        this.destroy$.next();
        this.destroy$.complete();
    }
}
