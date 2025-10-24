import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { PurchaseOrderService } from '../../../core/services/purchase-order.service';
import { PurchaseOrderDTO } from '../../../core/models/purchase-order.model';
import jsPDF from 'jspdf';
import autoTable from 'jspdf-autotable';

@Component({
  selector: 'app-purchase-order-list',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './purchase-order-list.component.html',
  styleUrl: './purchase-order-list.component.css'
})
export class PurchaseOrderListComponent implements OnInit {
  purchaseOrders: PurchaseOrderDTO[] = [];
  filteredOrders: PurchaseOrderDTO[] = [];
  loading = false;

    // Filter properties
  filterStatus: string = '';
  filterEventName: string = '';
  filterVendorName: string = '';


  // Sort properties
  sortOrder: 'asc' | 'desc' = 'desc'; // Default: newest first

  // Email configuration
  ldEmail: string = 'ak82@ford.com'; // L&D email
  vendorEmail: string = 'ka35@ford.com'; // Vendor email
  schedulerEmail: string = 'dn27@ford.com'; // Scheduler & Delivery email

  constructor(
    private purchaseOrderService: PurchaseOrderService,
    private router: Router,
    private toastr: ToastrService
  ) { }

  ngOnInit(): void {
    this.loadPurchaseOrders();
  }

  loadPurchaseOrders(): void {
    this.loading = true;
    this.purchaseOrderService.getAllPurchaseOrders().subscribe({
      next: (data) => {
        this.purchaseOrders = data;
        this.filteredOrders = data;
        this.sortByDate(); // Apply default sort
        this.loading = false;
      },
      error: (error) => {
        console.error('Error loading purchase orders:', error);
        this.toastr.error('Failed to load purchase orders', 'Error');
        this.loading = false;
      }
    });
  }

    applyFilters(): void {
    this.filteredOrders = this.purchaseOrders.filter(order => {
      const matchesStatus = !this.filterStatus || order.po_status === this.filterStatus;
      const matchesEventName = !this.filterEventName || 
        order.eventname.toLowerCase().includes(this.filterEventName.toLowerCase());
      const matchesVendorName = !this.filterVendorName || 
        order.vendorname.toLowerCase().includes(this.filterVendorName.toLowerCase());
      return matchesStatus && matchesEventName && matchesVendorName;
    });
    this.sortByDate(); // Re-apply sort after filtering
  }


    clearFilters(): void {
    this.filterStatus = '';
    this.filterEventName = '';
    this.filterVendorName = '';
    this.filteredOrders = this.purchaseOrders;
    this.sortByDate(); // Re-apply sort
    this.toastr.info('Filters cleared', 'Info');
  }


  sortByDate(): void {
    this.filteredOrders.sort((a, b) => {
      const dateA = new Date(a.orderdate).getTime();
      const dateB = new Date(b.orderdate).getTime();

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
    this.router.navigate(['/purchase-orders/new']);
  }

  viewPurchaseOrder(id: number): void {
    this.router.navigate(['/purchase-orders/view', id]);
  }

  editPurchaseOrder(id: number): void {
    this.router.navigate(['/purchase-orders/edit', id]);
  }

  deletePurchaseOrder(id: number): void {
    if (confirm('⚠️ Are you sure you want to delete this purchase order?\n\nThis action cannot be undone.')) {
      this.toastr.info('Deleting purchase order...', 'Processing');
      
      this.purchaseOrderService.deletePurchaseOrder(id).subscribe({
        next: () => {
          this.toastr.success('Purchase order deleted successfully!', 'Success');
          this.loadPurchaseOrders();
        },
        error: (error) => {
          console.error('Error deleting purchase order:', error);
          this.toastr.error('Failed to delete purchase order', 'Error');
        }
      });
    }
  }

  // Generate PDF - Common method
  private createPDF(order: PurchaseOrderDTO): jsPDF {
    const doc = new jsPDF();

    // Header
    doc.setFontSize(20);
    doc.setTextColor(40, 40, 40);
    doc.text('PURCHASE ORDER', 105, 20, { align: 'center' });

    // Company Info
    doc.setFontSize(10);
    doc.setTextColor(100, 100, 100);
    doc.text('Ford Motor Company', 105, 30, { align: 'center' });
    doc.text('Purchase Management System', 105, 35, { align: 'center' });

    // Line separator
    doc.setDrawColor(0, 123, 255);
    doc.setLineWidth(0.5);
    doc.line(20, 40, 190, 40);

    // PO Details
    doc.setFontSize(12);
    doc.setTextColor(40, 40, 40);
    doc.text(`PO Number: PO-${order.po_id}`, 20, 50);
    doc.text(`Date: ${new Date(order.orderdate).toLocaleDateString()}`, 20, 57);
    doc.text(`Status: ${order.po_status || 'PENDING'}`, 20, 64);

    doc.text(`Event ID: ${order.eventid}`, 120, 50);
    doc.text(`Vendor ID: ${order.vendorid}`, 120, 57);
    doc.text(`CDSID: ${order.cdsid}`, 120, 64);


    // Table with order details
    const inrAmount = order.orderamountINR || 0;
    const usdAmount = order.orderamountdollar || 0;

    autoTable(doc, {
      startY: 75,
      head: [['Description', 'Amount']],
      body: [
        ['Order Amount (INR)', `Rs. ${inrAmount.toLocaleString('en-IN', { minimumFractionDigits: 2, maximumFractionDigits: 2 })}`],
        ['Order Amount (USD)', `$ ${usdAmount.toLocaleString('en-US', { minimumFractionDigits: 2, maximumFractionDigits: 2 })}`],
        ['Purchase Request ID', order.prid ? order.prid.toString() : 'N/A'],
        ['Negotiation ID', order.negotiationid ? order.negotiationid.toString() : 'N/A'],
      ],
      theme: 'grid',
      headStyles: { fillColor: [40, 167, 69] },
      margin: { left: 20, right: 20 }
    });

    // Footer
    const finalY = (doc as any).lastAutoTable.finalY || 120;
    doc.setFontSize(10);
    doc.setTextColor(100, 100, 100);
    doc.text('This is a computer-generated document. No signature required.', 105, finalY + 20, { align: 'center' });
    doc.text(`Generated on: ${new Date().toLocaleString()}`, 105, finalY + 27, { align: 'center' });

    return doc;
  }

  // Download PDF - L&D
  downloadPDF_LD(order: PurchaseOrderDTO): void {
    const doc = this.createPDF(order);
    doc.save(`PO-${order.po_id}_LD.pdf`);
  }

  // View PDF - L&D
  viewPDF_LD(order: PurchaseOrderDTO): void {
    const doc = this.createPDF(order);
    window.open(doc.output('bloburl'), '_blank');
  }

  // Download PDF - Vendor
  downloadPDF_Vendor(order: PurchaseOrderDTO): void {
    const doc = this.createPDF(order);
    doc.save(`PO-${order.po_id}_Vendor.pdf`);
  }

  // View PDF - Vendor
  viewPDF_Vendor(order: PurchaseOrderDTO): void {
    const doc = this.createPDF(order);
    window.open(doc.output('bloburl'), '_blank');
  }

  // Email to L&D - Completed PO
  emailToLD_Completed(order: PurchaseOrderDTO): void {
    const inrAmount = order.orderamountINR || 0;
    const usdAmount = order.orderamountdollar || 0;

    const subject = `Purchase Order Completed - PO-${order.po_id}`;
    const body = `Dear L&D Team,

The Purchase Order PO-${order.po_id} has been COMPLETED.

Purchase Order Details:
-----------------------
PO Number: PO-${order.po_id}
Order Date: ${new Date(order.orderdate).toLocaleDateString()}
Event ID: ${order.eventid}
Vendor ID: ${order.vendorid}
Status: ${order.po_status || 'PENDING'}

Financial Details:
------------------
Order Amount (INR): Rs. ${inrAmount.toLocaleString('en-IN', { minimumFractionDigits: 2, maximumFractionDigits: 2 })}
Order Amount (USD): $ ${usdAmount.toLocaleString('en-US', { minimumFractionDigits: 2, maximumFractionDigits: 2 })}

Reference IDs:
--------------
Purchase Request ID: ${order.prid || 'N/A'}
Negotiation ID: ${order.negotiationid || 'N/A'}

Please download the PDF document separately and attach it to this email before sending.

Best Regards,
Purchase Management System`;

    const mailtoLink = `mailto:${this.ldEmail}?subject=${encodeURIComponent(subject)}&body=${encodeURIComponent(body)}`;
    window.location.href = mailtoLink;
  }

  // Email to L&D - Cancelled/Rejected PO
  emailToLD_Cancelled(order: PurchaseOrderDTO): void {
    const inrAmount = order.orderamountINR || 0;
    const usdAmount = order.orderamountdollar || 0;

    const subject = `Purchase Order ${order.po_status} - PO-${order.po_id}`;
    const body = `Dear L&D Team,

URGENT: Purchase Order PO-${order.po_id} has been ${order.po_status}.

Purchase Order Details:
-----------------------
PO Number: PO-${order.po_id}
Order Date: ${new Date(order.orderdate).toLocaleDateString()}
Event ID: ${order.eventid}
Vendor ID: ${order.vendorid}
Status: ${order.po_status || 'PENDING'}

Financial Details:
------------------
Order Amount (INR): Rs. ${inrAmount.toLocaleString('en-IN', { minimumFractionDigits: 2, maximumFractionDigits: 2 })}
Order Amount (USD): $ ${usdAmount.toLocaleString('en-US', { minimumFractionDigits: 2, maximumFractionDigits: 2 })}

Reference IDs:
--------------
Purchase Request ID: ${order.prid || 'N/A'}
Negotiation ID: ${order.negotiationid || 'N/A'}

Please review and take necessary action.

Best Regards,
Purchase Management System`;

    const mailtoLink = `mailto:${this.ldEmail}?subject=${encodeURIComponent(subject)}&body=${encodeURIComponent(body)}`;
    window.location.href = mailtoLink;
  }

  // Email to Vendor - Completed PO only
  emailToVendor(order: PurchaseOrderDTO): void {
    const inrAmount = order.orderamountINR || 0;
    const usdAmount = order.orderamountdollar || 0;

    const subject = `Purchase Order Issued - PO-${order.po_id}`;
    const body = `Dear Vendor,

Congratulations! Purchase Order PO-${order.po_id} has been issued.

Purchase Order Details:
-----------------------
PO Number: PO-${order.po_id}
Order Date: ${new Date(order.orderdate).toLocaleDateString()}
Event ID: ${order.eventid}
Vendor ID: ${order.vendorid}
Status: COMPLETED

Order Amount:
-------------
Amount (INR): Rs. ${inrAmount.toLocaleString('en-IN', { minimumFractionDigits: 2, maximumFractionDigits: 2 })}
Amount (USD): $ ${usdAmount.toLocaleString('en-US', { minimumFractionDigits: 2, maximumFractionDigits: 2 })}

Please download the PDF document separately and attach it to this email before sending.

Please proceed with the order as per the agreement.

Best Regards,
Purchase Management System
Ford Motor Company`;

    const mailtoLink = `mailto:${this.vendorEmail}?subject=${encodeURIComponent(subject)}&body=${encodeURIComponent(body)}`;
    window.location.href = mailtoLink;
  }

  // Email to Scheduler & Delivery - Completed PO only
  emailToScheduler(order: PurchaseOrderDTO): void {
    const inrAmount = order.orderamountINR || 0;
    const usdAmount = order.orderamountdollar || 0;

    const subject = `Purchase Order Issued - PO-${order.po_id}`;
    const body = `Dear Scheduler & Delivery Team,

Purchase Order PO-${order.po_id} has been issued and requires scheduling and delivery coordination.

Purchase Order Details:
-----------------------
PO Number: PO-${order.po_id}
Order Date: ${new Date(order.orderdate).toLocaleDateString()}
Event ID: ${order.eventid}
Vendor ID: ${order.vendorid}
Status: COMPLETED

Order Amount:
-------------
Amount (INR): Rs. ${inrAmount.toLocaleString('en-IN', { minimumFractionDigits: 2, maximumFractionDigits: 2 })}
Amount (USD): $ ${usdAmount.toLocaleString('en-US', { minimumFractionDigits: 2, maximumFractionDigits: 2 })}

Reference IDs:
--------------
Purchase Request ID: ${order.prid || 'N/A'}
Negotiation ID: ${order.negotiationid || 'N/A'}

Please download the PDF document separately and attach it to this email before sending.

Please coordinate with the vendor for scheduling and delivery of the order.

Best Regards,
Purchase Management System
Ford Motor Company`;

    const mailtoLink = `mailto:${this.schedulerEmail}?subject=${encodeURIComponent(subject)}&body=${encodeURIComponent(body)}`;
    window.location.href = mailtoLink;
  }

  getStatusClass(status: string): string {
    switch (status) {
      case 'COMPLETED': return 'bg-success';
      case 'PENDING': return 'bg-warning';
      case 'REJECTED': return 'bg-danger';
      default: return 'bg-secondary';
    }
  }
}
