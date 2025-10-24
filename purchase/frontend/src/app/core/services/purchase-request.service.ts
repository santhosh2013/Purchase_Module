import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { PurchaseRequestDTO } from '../models/purchase-request.model';

@Injectable({
  providedIn: 'root'
})
export class PurchaseRequestService {
  private apiUrl = `${environment.apiUrl}/purchase-requests`;

  constructor(private http: HttpClient) { }

  getAllPurchaseRequests(): Observable<PurchaseRequestDTO[]> {
    return this.http.get<PurchaseRequestDTO[]>(this.apiUrl);
  }

  getPurchaseRequestById(id: number): Observable<PurchaseRequestDTO> {
    return this.http.get<PurchaseRequestDTO>(`${this.apiUrl}/${id}`);
  }

  createPurchaseRequest(purchaseRequest: PurchaseRequestDTO): Observable<PurchaseRequestDTO> {
    return this.http.post<PurchaseRequestDTO>(this.apiUrl, purchaseRequest);
  }

  updatePurchaseRequest(id: number, purchaseRequest: PurchaseRequestDTO): Observable<PurchaseRequestDTO> {
    return this.http.put<PurchaseRequestDTO>(`${this.apiUrl}/${id}`, purchaseRequest);
  }

  deletePurchaseRequest(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
