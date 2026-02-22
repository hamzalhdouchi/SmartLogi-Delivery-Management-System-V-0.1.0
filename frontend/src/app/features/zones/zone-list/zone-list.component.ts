import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

@Component({
    selector: 'app-zone-list',
    standalone: true,
    imports: [CommonModule, RouterModule],
    template: `
    <div class="container-fluid py-4">
      <h2>Gestion des Zones</h2>
      <p class="text-muted">Cette fonctionnalité sera bientôt disponible.</p>
    </div>
  `
})
export class ZoneListComponent { }
