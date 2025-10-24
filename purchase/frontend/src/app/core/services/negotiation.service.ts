import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { NegotiationDTO } from '../models/negotiation.model';

@Injectable({
  providedIn: 'root'
})
export class NegotiationService {
  private apiUrl = `${environment.apiUrl}/negotiations`;

  constructor(private http: HttpClient) { }

  getAllNegotiations(): Observable<NegotiationDTO[]> {
    return this.http.get<NegotiationDTO[]>(this.apiUrl);
  }

  getNegotiationById(id: number): Observable<NegotiationDTO> {
    return this.http.get<NegotiationDTO>(`${this.apiUrl}/${id}`);
  }

  createNegotiation(negotiation: NegotiationDTO): Observable<NegotiationDTO> {
    return this.http.post<NegotiationDTO>(this.apiUrl, negotiation);
  }

  createNegotiationFromPR(prid: number): Observable<NegotiationDTO> {
    return this.http.post<NegotiationDTO>(`${this.apiUrl}/from-pr/${prid}`, {});
  }

  updateNegotiation(id: number, negotiation: NegotiationDTO): Observable<NegotiationDTO> {
    return this.http.put<NegotiationDTO>(`${this.apiUrl}/${id}`, negotiation);
  }

  deleteNegotiation(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
