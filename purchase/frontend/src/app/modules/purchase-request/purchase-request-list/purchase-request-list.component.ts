import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { PurchaseRequestService } from '../../../core/services/purchase-request.service';
import { PurchaseRequestDTO } from '../../../core/models/purchase-request.model';
import { NegotiationService } from '../../../core/services/negotiation.service';

@Component({
  selector: 'app-purchase-request-list',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './purchase-request-list.component.html',
  styleUrl: './purchase-request-list.component.css'
})
export class PurchaseRequestListComponent implements OnInit {
  purchaseRequests: PurchaseRequestDTO[] = [];
  filteredRequests: PurchaseRequestDTO[] = [];
  loading = false;

  // Filter properties
  filterStatus: string = '';
  filterEventName: string = '';
  filterVendorName: string = '';


  // Sort properties
  sortOrder: 'asc' | 'desc' = 'desc'; // Default: newest first

  constructor(
    private purchaseRequestService: PurchaseRequestService,
    private negotiationService: NegotiationService,
    private router: Router,
    private toastr: ToastrService
  ) { }

  ngOnInit(): void {
    this.loadPurchaseRequests();
  }

  loadPurchaseRequests(): void {
    this.loading = true;
    this.purchaseRequestService.getAllPurchaseRequests().subscribe({
      next: (data) => {
        this.purchaseRequests = data;
        this.filteredRequests = data;
        this.sortByDate(); // Apply default sort
        this.loading = false;
      },
      error: (error) => {
        console.error('Error loading purchase requests:', error);
        this.toastr.error('Failed to load purchase requests', 'Error');
        this.loading = false;
      }
    });
  }

    applyFilters(): void {
    this.filteredRequests = this.purchaseRequests.filter(request => {
      const matchesStatus = !this.filterStatus || request.prstatus === this.filterStatus;
      const matchesEventName = !this.filterEventName || 
        request.eventname.toLowerCase().includes(this.filterEventName.toLowerCase());
      const matchesVendorName = !this.filterVendorName || 
        request.vendorname.toLowerCase().includes(this.filterVendorName.toLowerCase());
      return matchesStatus && matchesEventName && matchesVendorName;
    });
    this.sortByDate(); // Re-apply sort after filtering
  }


    clearFilters(): void {
    this.filterStatus = '';
    this.filterEventName = '';
    this.filterVendorName = '';
    this.filteredRequests = this.purchaseRequests;
    this.sortByDate(); // Re-apply sort
    this.toastr.info('Filters cleared', 'Info');
  }


  sortByDate(): void {
    this.filteredRequests.sort((a, b) => {
      const dateA = new Date(a.requestdate).getTime();
      const dateB = new Date(b.requestdate).getTime();

      if (this.sortOrder === 'asc') {
        return dateA - dateB; // Oldest first
      } else {
        return dateB - dateA; // Newest first
      }
    });
  }

  toggleSortOrder(): void {
    this.sortOrder = this.sortOrder === 'asc' ? 'desc' : 'asc';
    this.sortByDate();
  }

  createNew(): void {
    this.router.navigate(['/purchase-requests/new']);
  }

  viewPurchaseRequest(id: number): void {
    this.router.navigate(['/purchase-requests/view', id]);
  }

  editPurchaseRequest(id: number): void {
    this.router.navigate(['/purchase-requests/edit', id]);
  }

  deletePurchaseRequest(id: number): void {
    if (confirm('⚠️ Are you sure you want to delete this purchase request?\n\nThis action cannot be undone.')) {
      this.toastr.info('Deleting purchase request...', 'Processing');

      this.purchaseRequestService.deletePurchaseRequest(id).subscribe({
        next: () => {
          this.toastr.success('Purchase request deleted successfully!', 'Success');
          this.loadPurchaseRequests();
        },
        error: (error) => {
          console.error('Error deleting purchase request:', error);
          this.toastr.error('Failed to delete purchase request', 'Error');
        }
      });
    }
  }

  createNegotiationFromPR(prid: number): void {
    this.toastr.info('Creating negotiation...', 'Processing');

    this.negotiationService.createNegotiationFromPR(prid).subscribe({
      next: (negotiation) => {
        this.toastr.success('Negotiation created successfully!', 'Success');
        this.router.navigate(['/negotiations/edit', negotiation.negotiationid]);
      },
      error: (error) => {
        console.error('Error creating negotiation:', error);
        this.toastr.error(error.error || 'Failed to create negotiation', 'Error');
      }
    });
  }

  getStatusClass(status: string): string {
    switch (status) {
      case 'APPROVED': return 'bg-success';
      case 'PENDING': return 'bg-warning';
      case 'REJECTED': return 'bg-danger';
      default: return 'bg-secondary';
    }
  }
}
