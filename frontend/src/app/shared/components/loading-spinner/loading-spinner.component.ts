import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LoadingService } from '../../../core/services/loading.service';

@Component({
    selector: 'app-loading-spinner',
    standalone: true,
    imports: [CommonModule],
    template: `
    <div class="spinner-overlay" *ngIf="loadingService.loading$ | async">
      <div class="spinner-container">
        <div class="spinner-border text-primary" role="status">
          <span class="visually-hidden">Chargement...</span>
        </div>
        <p class="mt-2 text-muted">Chargement en cours...</p>
      </div>
    </div>
  `,
    styles: [`
    .spinner-overlay {
      position: fixed;
      top: 0;
      left: 0;
      width: 100%;
      height: 100%;
      background-color: rgba(255, 255, 255, 0.8);
      display: flex;
      justify-content: center;
      align-items: center;
      z-index: 9999;
    }

    .spinner-container {
      text-align: center;
    }

    .spinner-border {
      width: 3rem;
      height: 3rem;
    }
  `]
})
export class LoadingSpinnerComponent {
    constructor(public loadingService: LoadingService) { }
}
