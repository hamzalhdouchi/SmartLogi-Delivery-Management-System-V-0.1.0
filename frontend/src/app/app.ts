import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { CommonModule } from '@angular/common';
import { NavbarComponent } from './shared/components/navbar/navbar.component';
import { LoadingSpinnerComponent } from './shared/components/loading-spinner/loading-spinner.component';
import { NotificationComponent } from './shared/components/notification/notification.component';
import { AuthService } from './core/services/auth.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    CommonModule,
    RouterOutlet,
    NavbarComponent,
    LoadingSpinnerComponent,
    NotificationComponent
  ],
  template: `
    <app-navbar *ngIf="authService.isAuthenticated()"></app-navbar>
    <app-loading-spinner></app-loading-spinner>
    <app-notification></app-notification>
    <main>
      <router-outlet></router-outlet>
    </main>
  `,
  styles: [`
    main {
      min-height: calc(100vh - 60px);
    }
  `]
})
export class App {
  title = 'SmartLogi';

  constructor(public authService: AuthService) { }
}
