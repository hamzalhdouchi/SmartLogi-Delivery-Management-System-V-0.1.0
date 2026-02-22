import { Injectable } from '@angular/core';
import { Subject, Observable } from 'rxjs';

export interface Notification {
    message: string;
    type: 'success' | 'error' | 'warning' | 'info';
    id?: number;
}

@Injectable({
    providedIn: 'root'
})
export class NotificationService {
    private messageSubject = new Subject<Notification>();
    private notificationId = 0;

    message$: Observable<Notification> = this.messageSubject.asObservable();

    success(message: string): void {
        this.show(message, 'success');
    }

    error(message: string): void {
        this.show(message, 'error');
    }

    warning(message: string): void {
        this.show(message, 'warning');
    }

    info(message: string): void {
        this.show(message, 'info');
    }

    private show(message: string, type: Notification['type']): void {
        this.messageSubject.next({
            message,
            type,
            id: ++this.notificationId
        });
    }
}
