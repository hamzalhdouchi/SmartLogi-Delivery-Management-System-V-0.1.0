import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { Subject, takeUntil } from 'rxjs';
import { AuthService } from '../../../core/services/auth.service';
import { User } from '../../../core/models/user.model';
import { Role } from '../../../core/models/role.enum';

@Component({
    selector: 'app-navbar',
    standalone: true,
    imports: [CommonModule, RouterModule],
    templateUrl: './navbar.component.html',
    styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit, OnDestroy {
    currentUser: User | null = null;
    isCollapsed = true;
    private destroy$ = new Subject<void>();

    constructor(public authService: AuthService) { }

    ngOnInit(): void {
        this.currentUser = this.authService.getCurrentUser();
    }

    toggleNavbar(): void {
        this.isCollapsed = !this.isCollapsed;
    }

    logout(): void {
        this.authService.logout();
    }

    hasRole(role: string): boolean {
        return this.authService.hasRole(role);
    }

    isManager(): boolean {
        return this.hasRole(Role.MANAGER);
    }

    isDriver(): boolean {
        return this.hasRole(Role.DRIVER);
    }

    isClient(): boolean {
        return this.hasRole(Role.CLIENT);
    }

    get userDisplayName(): string {
        if (this.currentUser) {
            return `${this.currentUser.firstName} ${this.currentUser.lastName}`;
        }
        return 'Utilisateur';
    }

    ngOnDestroy(): void {
        this.destroy$.next();
        this.destroy$.complete();
    }
}
