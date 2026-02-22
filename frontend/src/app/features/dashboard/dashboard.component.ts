import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';
import { Role } from '../../core/models/role.enum';
import { ManagerDashboardComponent } from './manager-dashboard/manager-dashboard.component';
import { DriverDashboardComponent } from './driver-dashboard/driver-dashboard.component';
import { ClientDashboardComponent } from './client-dashboard/client-dashboard.component';

@Component({
    selector: 'app-dashboard',
    standalone: true,
    imports: [
        CommonModule,
        RouterModule,
        ManagerDashboardComponent,
        DriverDashboardComponent,
        ClientDashboardComponent
    ],
    template: `
    <app-manager-dashboard *ngIf="isManager()"></app-manager-dashboard>
    <app-driver-dashboard *ngIf="isDriver()"></app-driver-dashboard>
    <app-client-dashboard *ngIf="isClient() || isRecipient()"></app-client-dashboard>
  `
})
export class DashboardComponent implements OnInit {
    constructor(private authService: AuthService) { }

    ngOnInit(): void { }

    isManager(): boolean {
        return this.authService.hasRole(Role.MANAGER);
    }

    isDriver(): boolean {
        return this.authService.hasRole(Role.DRIVER);
    }

    isClient(): boolean {
        return this.authService.hasRole(Role.CLIENT);
    }

    isRecipient(): boolean {
        return this.authService.hasRole(Role.RECIPIENT);
    }
}
