import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { PurchaseOrderDTO } from '../models/purchase-order.model';

@Injectable({
  providedIn: 'root'
})
export class PurchaseOrderService {
  private apiUrl = `${environment.apiUrl}/purchase-orders`;

  constructor(private http: HttpClient) { }

  getAllPurchaseOrders(): Observable<PurchaseOrderDTO[]> {
    return this.http.get<PurchaseOrderDTO[]>(this.apiUrl);
  }

  getPurchaseOrderById(id: number): Observable<PurchaseOrderDTO> {
    return this.http.get<PurchaseOrderDTO>(`${this.apiUrl}/${id}`);
  }

  createPurchaseOrder(purchaseOrder: PurchaseOrderDTO): Observable<PurchaseOrderDTO> {
    return this.http.post<PurchaseOrderDTO>(this.apiUrl, purchaseOrder);
  }

  updatePurchaseOrder(id: number, purchaseOrder: PurchaseOrderDTO): Observable<PurchaseOrderDTO> {
    return this.http.put<PurchaseOrderDTO>(`${this.apiUrl}/${id}`, purchaseOrder);
  }

  deletePurchaseOrder(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
