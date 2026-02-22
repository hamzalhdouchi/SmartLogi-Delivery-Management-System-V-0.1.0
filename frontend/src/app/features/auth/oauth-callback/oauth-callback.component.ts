import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, ActivatedRoute, RouterModule } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';
import { NotificationService } from '../../../core/services/notification.service';

@Component({
  selector: 'app-oauth-callback',
  standalone: true,
  imports: [CommonModule, RouterModule],
  template: `
    <div class="callback-container">
      <div class="callback-card">
        <div class="spinner-border text-primary" role="status" *ngIf="!error">
          <span class="visually-hidden">Chargement...</span>
        </div>
        <p class="mt-3" *ngIf="!error">Authentification en cours...</p>
        
        <div *ngIf="error" class="text-center">
          <i class="fas fa-exclamation-circle text-danger fa-3x mb-3"></i>
          <p class="text-danger">{{ error }}</p>
          <a routerLink="/login" class="btn btn-primary">Retour à la connexion</a>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .callback-container {
      min-height: 100vh;
      display: flex;
      align-items: center;
      justify-content: center;
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    }

    .callback-card {
      background: white;
      border-radius: 16px;
      padding: 60px;
      text-align: center;
      box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
    }

    .spinner-border {
      width: 3rem;
      height: 3rem;
    }
  `]
})
export class OAuthCallbackComponent implements OnInit {
  error: string | null = null;

  constructor(
    private route: ActivatedRoute,
    private authService: AuthService,
    private router: Router,
    private notificationService: NotificationService
  ) { }

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      const code = params['code'];
      const provider = params['provider'] || this.detectProvider(params);
      const error = params['error'];
      const returnUrl = params['returnUrl'] || '/dashboard';

      if (error) {
        this.error = 'Erreur d\'authentification OAuth2';
        this.notificationService.error(this.error);
        return;
      }

      if (code && provider) {
        this.authService.exchangeOAuthCode(code, provider).subscribe({
          next: () => {
            this.notificationService.success('Connexion réussie !');
            this.router.navigate([returnUrl]);
          },
          error: (err) => {
            console.error('OAuth2 error:', err);
            this.error = 'Échec de l\'authentification';
            this.notificationService.error(this.error);
          }
        });
      } else {
        this.router.navigate(['/login']);
      }
    });
  }

  private detectProvider(params: any): string {
    if (params['state']?.includes('google')) return 'google';
    if (params['state']?.includes('github')) return 'github';
    return 'unknown';
  }
}
