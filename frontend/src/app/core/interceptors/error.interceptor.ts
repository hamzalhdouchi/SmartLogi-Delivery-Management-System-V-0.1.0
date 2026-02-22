import { Injectable } from '@angular/core';
import {
    HttpRequest,
    HttpHandler,
    HttpEvent,
    HttpInterceptor,
    HttpErrorResponse
} from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { Router } from '@angular/router';
import { NotificationService } from '../services/notification.service';
import { TokenService } from '../services/token.service';

@Injectable()
export class ErrorInterceptor implements HttpInterceptor {
    constructor(
        private router: Router,
        private notificationService: NotificationService,
        private tokenService: TokenService
    ) { }

    intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        return next.handle(request).pipe(
            catchError((error: HttpErrorResponse) => {
                let errorMessage = 'Une erreur est survenue';

                switch (error.status) {
                    case 400:
                        errorMessage = error.error?.message || 'Requête invalide';
                        this.notificationService.error(errorMessage);
                        break;

                    case 401:
                        errorMessage = 'Session expirée. Veuillez vous reconnecter.';
                        this.tokenService.removeToken();
                        this.router.navigate(['/login']);
                        this.notificationService.warning(errorMessage);
                        break;

                    case 403:
                        errorMessage = 'Accès refusé. Vous n\'avez pas les permissions nécessaires.';
                        this.notificationService.error(errorMessage);
                        break;

                    case 404:
                        errorMessage = error.error?.message || 'Ressource non trouvée';
                        this.notificationService.error(errorMessage);
                        break;

                    case 409:
                        errorMessage = error.error?.message || 'Conflit de données';
                        this.notificationService.error(errorMessage);
                        break;

                    case 500:
                        errorMessage = 'Erreur serveur. Veuillez réessayer plus tard.';
                        this.notificationService.error(errorMessage);
                        break;

                    case 0:
                        errorMessage = 'Impossible de contacter le serveur. Vérifiez votre connexion.';
                        this.notificationService.error(errorMessage);
                        break;

                    default:
                        this.notificationService.error(errorMessage);
                }

                console.error('HTTP Error:', error);
                return throwError(() => error);
            })
        );
    }
}
