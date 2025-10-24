import { Routes } from '@angular/router';
import { HomeComponent } from './shared/components/home/home.component';
import { NegotiationListComponent } from './modules/negotiation/negotiation-list/negotiation-list.component';
import { NegotiationFormComponent } from './modules/negotiation/negotiation-form/negotiation-form.component';
import { PurchaseOrderListComponent } from './modules/purchase-order/purchase-order-list/purchase-order-list.component';
import { PurchaseOrderFormComponent } from './modules/purchase-order/purchase-order-form/purchase-order-form.component';
import { PurchaseRequestListComponent } from './modules/purchase-request/purchase-request-list/purchase-request-list.component';
import { PurchaseRequestFormComponent } from './modules/purchase-request/purchase-request-form/purchase-request-form.component';

export const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'home', component: HomeComponent },
  
  // Negotiation Routes
  { path: 'negotiations', component: NegotiationListComponent },
  { path: 'negotiations/new', component: NegotiationFormComponent },
  { path: 'negotiations/edit/:id', component: NegotiationFormComponent },
  { path: 'negotiations/view/:id', component: NegotiationFormComponent },
  
  // Purchase Order Routes
  { path: 'purchase-orders', component: PurchaseOrderListComponent },
  { path: 'purchase-orders/new', component: PurchaseOrderFormComponent },
  { path: 'purchase-orders/edit/:id', component: PurchaseOrderFormComponent },
  { path: 'purchase-orders/view/:id', component: PurchaseOrderFormComponent },
  
  // Purchase Request Routes
  { path: 'purchase-requests', component: PurchaseRequestListComponent },
  { path: 'purchase-requests/new', component: PurchaseRequestFormComponent },
  { path: 'purchase-requests/edit/:id', component: PurchaseRequestFormComponent },
  { path: 'purchase-requests/view/:id', component: PurchaseRequestFormComponent },
  
  // Redirect unknown routes
  { path: '**', redirectTo: '' }
];
