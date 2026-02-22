import { Injectable } from '@angular/core';
import {
    CanActivate,
    ActivatedRouteSnapshot,
    Router
} from '@angular/router';
import { AuthService } from '../services/auth.service';
import { NotificationService } from '../services/notification.service';

@Injectable({
    providedIn: 'root'
})
export class RoleGuard implements CanActivate {
    constructor(
        private authService: AuthService,
        private router: Router,
        private notificationService: NotificationService
    ) { }

    canActivate(route: ActivatedRouteSnapshot): boolean {
        const requiredRoles = route.data['roles'] as string[];

        if (!requiredRoles || requiredRoles.length === 0) {
            return true;
        }

        if (this.authService.hasAnyRole(requiredRoles)) {
            return true;
        }

        this.notificationService.warning('Vous n\'avez pas les permissions pour accéder à cette page.');
        this.router.navigate(['/dashboard']);
        return false;
    }
}
