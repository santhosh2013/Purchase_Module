import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { PurchaseOrderService } from '../../../core/services/purchase-order.service';
import { PurchaseOrderDTO, PurchaseOrderStatus } from '../../../core/models/purchase-order.model';

@Component({
  selector: 'app-purchase-order-form',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './purchase-order-form.component.html',
  styleUrl: './purchase-order-form.component.css'
})
export class PurchaseOrderFormComponent implements OnInit {
  purchaseOrder: PurchaseOrderDTO = {
    eventid: 0,
    eventname: '',
    vendorid: 0,
    vendorname: '',
    cdsid: '',
    orderdate: '',
    orderamountINR: 0,
    orderamountdollar: 0,
    po_status: 'PENDING',
    prid: 0,
    negotiationid: 0
  };

  isEditMode = false;
  isViewMode = false;
  purchaseOrderId: number | null = null;
  statusOptions = Object.values(PurchaseOrderStatus);
  submitted = false;
  exchangeRate = 83.5; // INR to USD rate

  constructor(
    private purchaseOrderService: PurchaseOrderService,
    private router: Router,
    private route: ActivatedRoute,
    private toastr: ToastrService
  ) { }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      if (params['id']) {
        this.purchaseOrderId = +params['id'];
        
        // Check if we're in view mode or edit mode
        const urlSegments = this.route.snapshot.url;
        this.isViewMode = urlSegments.some(segment => segment.path === 'view');
        this.isEditMode = urlSegments.some(segment => segment.path === 'edit');
        
        this.loadPurchaseOrder(this.purchaseOrderId);
      }
    });

    if (!this.isEditMode && !this.isViewMode) {
      const today = new Date();
      this.purchaseOrder.orderdate = today.toISOString().split('T')[0];
    }
  }

  loadPurchaseOrder(id: number): void {
    this.purchaseOrderService.getPurchaseOrderById(id).subscribe({
      next: (data) => {
        this.purchaseOrder = data;
        if (this.purchaseOrder.orderdate) {
          this.purchaseOrder.orderdate = new Date(this.purchaseOrder.orderdate)
            .toISOString().split('T')[0];
        }
      },
      error: (error) => {
        console.error('Error loading purchase order:', error);
        this.toastr.error('Failed to load purchase order', 'Error');
        this.goBack();
      }
    });
  }

  onINRChange(): void {
    if (this.purchaseOrder.orderamountINR > 0) {
      this.purchaseOrder.orderamountdollar =
        this.purchaseOrder.orderamountINR / this.exchangeRate;
    }
  }

  onUSDChange(): void {
    if (this.purchaseOrder.orderamountdollar > 0) {
      this.purchaseOrder.orderamountINR =
        this.purchaseOrder.orderamountdollar * this.exchangeRate;
    }
  }

  onSubmit(): void {
    if (this.isViewMode) {
      return; // No action in view mode
    }

    this.submitted = true;

    // Convert 0 to undefined for optional fields to send null to backend
    const orderToSubmit = { ...this.purchaseOrder };
    if (orderToSubmit.prid === 0) {
      orderToSubmit.prid = undefined;
    }
    if (orderToSubmit.negotiationid === 0) {
      orderToSubmit.negotiationid = undefined;
    }

    if (this.isEditMode && this.purchaseOrderId) {
      // Update existing purchase order
      this.toastr.info('Updating purchase order...', 'Processing');

      this.purchaseOrderService.updatePurchaseOrder(this.purchaseOrderId, orderToSubmit).subscribe({
        next: () => {
          this.toastr.success('Purchase order updated successfully!', 'Success');
          this.goBack();
        },
        error: (error) => {
          console.error('Error updating purchase order:', error);
          
          // Extract detailed error message
          let errorMessage = 'Failed to update purchase order';
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
      // Create new purchase order
      this.toastr.info('Creating purchase order...', 'Processing');

      this.purchaseOrderService.createPurchaseOrder(orderToSubmit).subscribe({
        next: () => {
          this.toastr.success('Purchase order created successfully!', 'Success');
          this.goBack();
        },
        error: (error) => {
          console.error('Error creating purchase order:', error);
          
          // Extract detailed error message from server response
          let errorMessage = 'Failed to create purchase order';
          
          if (error.error) {
            if (typeof error.error === 'string') {
              errorMessage = error.error;
            } else if (error.error.message) {
              errorMessage = error.error.message;
            }
          } else if (error.message) {
            errorMessage = error.message;
          }
          
          // Check for specific error messages and make them user-friendly
          if (errorMessage.includes('500')) {
            // Server error
            this.toastr.error('Server error occurred. Please check all fields and try again.', 'Server Error', { 
              timeOut: 7000,
              closeButton: true,
              progressBar: true
            });
          } else {
            // Generic error
            this.toastr.error(errorMessage, 'Creation Failed', { 
              timeOut: 7000,
              closeButton: true,
              progressBar: true
            });
          }
        }
      });
    }
  }

  goBack(): void {
    this.router.navigate(['/purchase-orders']);
  }
}
