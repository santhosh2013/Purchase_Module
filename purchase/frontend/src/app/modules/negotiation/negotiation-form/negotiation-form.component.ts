import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { NegotiationService } from '../../../core/services/negotiation.service';
import { NegotiationDTO, NegotiationStatus } from '../../../core/models/negotiation.model';

@Component({
  selector: 'app-negotiation-form',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './negotiation-form.component.html',
  styleUrl: './negotiation-form.component.css'
})
export class NegotiationFormComponent implements OnInit {
  negotiation: NegotiationDTO = {
    eventid: 0,
    eventname: '',
    vendorid: 0,
    vendorname: '',
    cdsid: '',
    negotiationdate: '',
    initialquoteamount: 0,
    finalamount: 0,
    negotiationstatus: 'Pending',
    notes: ''
  };

  isEditMode = false;
  isViewMode = false;
  negotiationId: number | null = null;
  statusOptions = Object.values(NegotiationStatus);
  submitted = false;

  constructor(
    private negotiationService: NegotiationService,
    private router: Router,
    private route: ActivatedRoute,
    private toastr: ToastrService
  ) { }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      if (params['id']) {
        this.negotiationId = +params['id'];
        
        // Check if we're in view mode or edit mode
        const urlSegments = this.route.snapshot.url;
        this.isViewMode = urlSegments.some(segment => segment.path === 'view');
        this.isEditMode = urlSegments.some(segment => segment.path === 'edit');
        
        this.loadNegotiation(this.negotiationId);
      }
    });

    // Set default date to today
    if (!this.isEditMode && !this.isViewMode) {
      const today = new Date();
      this.negotiation.negotiationdate = today.toISOString().split('T')[0];
    }
  }

  loadNegotiation(id: number): void {
    this.negotiationService.getNegotiationById(id).subscribe({
      next: (data) => {
        this.negotiation = data;
        // Format date for input field
        if (this.negotiation.negotiationdate) {
          this.negotiation.negotiationdate = new Date(this.negotiation.negotiationdate)
            .toISOString().split('T')[0];
        }
      },
      error: (error) => {
        console.error('Error loading negotiation:', error);
        this.toastr.error('Failed to load negotiation', 'Error');
        this.goBack();
      }
    });
  }

  onSubmit(): void {
    this.submitted = true;

    if (this.isEditMode && this.negotiationId) {
      this.negotiationService.updateNegotiation(this.negotiationId, this.negotiation).subscribe({
        next: () => {
          this.toastr.success('Negotiation updated successfully!', 'Success');
          this.goBack();
        },
        error: (error) => {
          console.error('Error updating negotiation:', error);
          this.toastr.error('Failed to update negotiation', 'Error');
        }
      });
    } else {
      this.negotiationService.createNegotiation(this.negotiation).subscribe({
        next: () => {
          this.toastr.success('Negotiation created successfully!', 'Success');
          this.goBack();
        },
        error: (error) => {
          console.error('Error creating negotiation:', error);
          this.toastr.error('Failed to create negotiation', 'Error');
        }
      });
    }
  }

  goBack(): void {
    this.router.navigate(['/negotiations']);
  }

  calculateSavings(): number {
    return this.negotiation.initialquoteamount - this.negotiation.finalamount;
  }
}
