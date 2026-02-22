import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Subject, takeUntil } from 'rxjs';
import { NotificationService, Notification } from '../../../core/services/notification.service';

@Component({
    selector: 'app-notification',
    standalone: true,
    imports: [CommonModule],
    template: `
    <div class="notification-container">
      <div
        *ngFor="let notification of notifications; trackBy: trackById"
        class="alert alert-dismissible fade show"
        [ngClass]="getAlertClass(notification.type)"
        role="alert">
        <i [ngClass]="getIconClass(notification.type)" class="me-2"></i>
        {{ notification.message }}
        <button
          type="button"
          class="btn-close"
          (click)="removeNotification(notification)"
          aria-label="Close">
        </button>
      </div>
    </div>
  `,
    styles: [`
    .notification-container {
      position: fixed;
      top: 80px;
      right: 20px;
      z-index: 9998;
      max-width: 400px;
      width: 100%;
    }

    .alert {
      animation: slideIn 0.3s ease-out;
      margin-bottom: 10px;
      box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
    }

    @keyframes slideIn {
      from {
        transform: translateX(100%);
        opacity: 0;
      }
      to {
        transform: translateX(0);
        opacity: 1;
      }
    }
  `]
})
export class NotificationComponent implements OnInit, OnDestroy {
    notifications: Notification[] = [];
    private destroy$ = new Subject<void>();

    constructor(private notificationService: NotificationService) { }

    ngOnInit(): void {
        this.notificationService.message$
            .pipe(takeUntil(this.destroy$))
            .subscribe(notification => {
                this.notifications.push(notification);
                // Auto-dismiss after 5 seconds
                setTimeout(() => this.removeNotification(notification), 5000);
            });
    }

    removeNotification(notification: Notification): void {
        this.notifications = this.notifications.filter(n => n.id !== notification.id);
    }

    trackById(index: number, notification: Notification): number {
        return notification.id || index;
    }

    getAlertClass(type: string): string {
        const classes: { [key: string]: string } = {
            success: 'alert-success',
            error: 'alert-danger',
            warning: 'alert-warning',
            info: 'alert-info'
        };
        return classes[type] || 'alert-info';
    }

    getIconClass(type: string): string {
        const icons: { [key: string]: string } = {
            success: 'fas fa-check-circle',
            error: 'fas fa-exclamation-circle',
            warning: 'fas fa-exclamation-triangle',
            info: 'fas fa-info-circle'
        };
        return icons[type] || 'fas fa-info-circle';
    }

    ngOnDestroy(): void {
        this.destroy$.next();
        this.destroy$.complete();
    }
}
