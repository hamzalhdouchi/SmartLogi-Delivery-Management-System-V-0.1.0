import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { Subject, takeUntil } from 'rxjs';
import { BaseChartDirective } from 'ng2-charts';
import { ChartConfiguration, ChartData, ChartType } from 'chart.js';
import { ParcelService } from '../../parcels/services/parcel.service';
import { Parcel, ParcelStatistics, ParcelStatus } from '../../parcels/models/parcel.model';

@Component({
    selector: 'app-manager-dashboard',
    standalone: true,
    imports: [CommonModule, RouterModule, BaseChartDirective],
    templateUrl: './manager-dashboard.component.html',
    styleUrls: ['./manager-dashboard.component.css']
})
export class ManagerDashboardComponent implements OnInit, OnDestroy {
    statistics: ParcelStatistics = {
        total: 0,
        pending: 0,
        inProgress: 0,
        delivered: 0,
        delayed: 0,
        cancelled: 0
    };
    recentParcels: Parcel[] = [];
    private destroy$ = new Subject<void>();

    // Chart Configuration
    public doughnutChartType: ChartType = 'doughnut';
    public doughnutChartData: ChartData<'doughnut'> = {
        labels: ['En attente', 'En cours', 'Livrés', 'En retard', 'Annulés'],
        datasets: [{
            data: [0, 0, 0, 0, 0],
            backgroundColor: [
                '#ffc107',
                '#17a2b8',
                '#28a745',
                '#dc3545',
                '#6c757d'
            ]
        }]
    };

    public doughnutChartOptions: ChartConfiguration['options'] = {
        responsive: true,
        plugins: {
            legend: {
                position: 'bottom'
            }
        }
    };

    constructor(private parcelService: ParcelService) { }

    ngOnInit(): void {
        this.loadStatistics();
        this.loadRecentParcels();
    }

    loadStatistics(): void {
        this.parcelService.getStatistics()
            .pipe(takeUntil(this.destroy$))
            .subscribe({
                next: (stats) => {
                    this.statistics = stats;
                    this.updateChart();
                },
                error: (error) => console.error('Error loading statistics:', error)
            });
    }

    loadRecentParcels(): void {
        this.parcelService.getAllParcels(0, 5)
            .pipe(takeUntil(this.destroy$))
            .subscribe({
                next: (response) => {
                    this.recentParcels = response.content;
                },
                error: (error) => console.error('Error loading recent parcels:', error)
            });
    }

    private updateChart(): void {
        this.doughnutChartData.datasets[0].data = [
            this.statistics.pending,
            this.statistics.inProgress,
            this.statistics.delivered,
            this.statistics.delayed,
            this.statistics.cancelled
        ];
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
