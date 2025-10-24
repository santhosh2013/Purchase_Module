import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent {
  currentYear: number = new Date().getFullYear();

  constructor(private router: Router) {}

  navigateToPurchaseRequests(): void {
    this.router.navigate(['/purchase-requests']);
  }

  navigateToNegotiations(): void {
    this.router.navigate(['/negotiations']);
  }

  navigateToPurchaseOrders(): void {
    this.router.navigate(['/purchase-orders']);
  }
}
