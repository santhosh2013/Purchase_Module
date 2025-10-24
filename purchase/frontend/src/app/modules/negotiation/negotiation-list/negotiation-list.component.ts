import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { NegotiationService } from '../../../core/services/negotiation.service';
import { NegotiationDTO } from '../../../core/models/negotiation.model';

@Component({
  selector: 'app-negotiation-list',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './negotiation-list.component.html',
  styleUrl: './negotiation-list.component.css'
})
export class NegotiationListComponent implements OnInit {
  negotiations: NegotiationDTO[] = [];
  filteredNegotiations: NegotiationDTO[] = [];
  loading = false;

  // Filter properties
  filterStatus: string = '';
  filterEventId: string = '';
  filterVendorName: string = '';

  // Sort properties
  sortOrder: 'asc' | 'desc' = 'desc'; // Default: newest first

  constructor(
    private negotiationService: NegotiationService,
    private router: Router,
    private toastr: ToastrService
  ) {}

  ngOnInit(): void {
    this.loadNegotiations();
  }

  loadNegotiations(): void {
    this.loading = true;
    this.negotiationService.getAllNegotiations().subscribe({
      next: (data) => {
        this.negotiations = data;
        this.filteredNegotiations = data;
        this.sortByDate(); // Apply default sort
        this.loading = false;
      },
      error: (error) => {
        console.error('Error loading negotiations:', error);
        this.toastr.error('Failed to load negotiations', 'Error');
        this.loading = false;
      }
    });
  }

  applyFilters(): void {
    this.filteredNegotiations = this.negotiations.filter(negotiation => {
      const matchesStatus = !this.filterStatus || negotiation.negotiationstatus === this.filterStatus;
      const matchesEventId = !this.filterEventId || negotiation.eventid === Number(this.filterEventId);
      const matchesVendorName = !this.filterVendorName || 
        negotiation.vendorname.toLowerCase().includes(this.filterVendorName.toLowerCase());
      return matchesStatus && matchesEventId && matchesVendorName;
    });
    this.sortByDate(); // Re-apply sort after filtering
  }

  clearFilters(): void {
    this.filterStatus = '';
    this.filterEventId = '';
    this.filterVendorName = '';
    this.filteredNegotiations = this.negotiations;
    this.sortByDate(); // Re-apply sort
    this.toastr.info('Filters cleared', 'Info');
  }

  sortByDate(): void {
    this.filteredNegotiations.sort((a, b) => {
      const dateA = new Date(a.negotiationdate).getTime();
      const dateB = new Date(b.negotiationdate).getTime();
      
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
    this.router.navigate(['/negotiations/new']);
  }

  viewNegotiation(id: number): void {
    this.router.navigate(['/negotiations/view', id]);
  }

  editNegotiation(id: number): void {
    this.router.navigate(['/negotiations/edit', id]);
  }

  deleteNegotiation(id: number): void {
    if (confirm('Are you sure you want to delete this negotiation?')) {
      this.toastr.info('Deleting negotiation...', 'Processing');
      
      this.negotiationService.deleteNegotiation(id).subscribe({
        next: () => {
          this.toastr.success('Negotiation deleted successfully!', 'Success');
          this.loadNegotiations();
        },
        error: (error) => {
          console.error('Error deleting negotiation:', error);
          this.toastr.error('Failed to delete negotiation', 'Error');
        }
      });
    }
  }

  calculateSavings(initial: number, final: number): number {
    return initial - final;
  }

  getStatusClass(status: string): string {
    switch (status) {
      case 'COMPLETED': return 'bg-success';
      case 'Completed': return 'bg-success';
      case 'IN_PROGRESS': return 'bg-primary';
      case 'PENDING': return 'bg-warning';
      case 'Pending': return 'bg-warning';
      case 'REJECTED': return 'bg-danger';
      case 'CANCELLED': return 'bg-danger';
      case 'Cancelled': return 'bg-danger';
      default: return 'bg-secondary';
    }
  }
}
