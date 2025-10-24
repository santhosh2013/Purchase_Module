import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { PurchaseRequestService } from '../../../core/services/purchase-request.service';
import { PurchaseRequestDTO, PurchaseRequestStatus } from '../../../core/models/purchase-request.model';

@Component({
  selector: 'app-purchase-request-form',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './purchase-request-form.component.html',
  styleUrl: './purchase-request-form.component.css'
})
export class PurchaseRequestFormComponent implements OnInit {
  purchaseRequest: PurchaseRequestDTO = {
    eventid: 0,
    eventname: '',
    vendorid: 0,
    vendorname: '',
    cdsid: '',
    requestdate: '',
    allocatedamount: 0,
    prstatus: 'PENDING'
  };

  isEditMode = false;
  isViewMode = false;
  purchaseRequestId: number | null = null;
  statusOptions = Object.values(PurchaseRequestStatus);
  submitted = false;

  constructor(
    private purchaseRequestService: PurchaseRequestService,
    private router: Router,
    private route: ActivatedRoute,
    private toastr: ToastrService
  ) { }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      if (params['id']) {
        this.purchaseRequestId = +params['id'];
        
        // Check if we're in view mode or edit mode
        const urlSegments = this.route.snapshot.url;
        this.isViewMode = urlSegments.some(segment => segment.path === 'view');
        this.isEditMode = urlSegments.some(segment => segment.path === 'edit');
        
        this.loadPurchaseRequest(this.purchaseRequestId);
      }
    });

    if (!this.isEditMode && !this.isViewMode) {
      const today = new Date();
      this.purchaseRequest.requestdate = today.toISOString().split('T')[0];
    }
  }

  loadPurchaseRequest(id: number): void {
    this.purchaseRequestService.getPurchaseRequestById(id).subscribe({
      next: (data) => {
        this.purchaseRequest = data;
        if (this.purchaseRequest.requestdate) {
          this.purchaseRequest.requestdate = new Date(this.purchaseRequest.requestdate)
            .toISOString().split('T')[0];
        }
      },
      error: (error) => {
        console.error('Error loading purchase request:', error);
        this.toastr.error('Failed to load purchase request', 'Error');
        this.goBack();
      }
    });
  }

  onSubmit(): void {
    if (this.isViewMode) {
      return; // No action in view mode
    }

    this.submitted = true;

    if (this.isEditMode && this.purchaseRequestId) {
      // Update existing purchase request
      this.toastr.info('Updating purchase request...', 'Processing');

      this.purchaseRequestService.updatePurchaseRequest(this.purchaseRequestId, this.purchaseRequest).subscribe({
        next: () => {
          this.toastr.success('Purchase request updated successfully!', 'Success');
          this.goBack();
        },
        error: (error) => {
          console.error('Error updating purchase request:', error);
          
          // Extract detailed error message
          let errorMessage = 'Failed to update purchase request';
          if (error.error) {
            if (typeof error.error === 'string') {
              errorMessage = error.error;
            } else if (error.error.message) {
              errorMessage = error.error.message;
            }
          } else if (error.message) {
            errorMessage = error.message;
          }
          
          this.toastr.error(errorMessage, 'Update Failed', { 
            timeOut: 7000,
            closeButton: true,
            progressBar: true
          });
        }
      });
    } else {
      // Create new purchase request
      this.toastr.info('Creating purchase request...', 'Processing');

      this.purchaseRequestService.createPurchaseRequest(this.purchaseRequest).subscribe({
        next: () => {
          this.toastr.success('Purchase request created successfully!', 'Success');
          this.goBack();
        },
        error: (error) => {
          console.error('Error creating purchase request:', error);
          
          // Extract detailed error message from server response
          let errorMessage = 'Failed to create purchase request';
          
          if (error.error) {
            if (typeof error.error === 'string') {
              errorMessage = error.error;
            } else if (error.error.message) {
              errorMessage = error.error.message;
            }
          } else if (error.message) {
            errorMessage = error.message;
          }
          
          // Show error toaster - NO special handling, just show the message
          this.toastr.error(errorMessage, 'Error', { 
            timeOut: 7000,
            closeButton: true,
            progressBar: true
          });
        }
      });
    }
  }


  goBack(): void {
    this.router.navigate(['/purchase-requests']);
  }
}
