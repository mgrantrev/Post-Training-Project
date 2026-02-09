import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { AuthService } from './auth.service';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class CustomerService {
  constructor(private http: HttpClient, private auth: AuthService) { }

  private getHeaders(): HttpHeaders {
    return new HttpHeaders({
      'Authorization': `Bearer ${this.auth.getToken()}`
    });
  }

  getTracks(customerId: number): Observable<any[]> {
    return this.http.get<any[]>(`/api/customers/${customerId}/tracks`, { headers: this.getHeaders() });
  }

  getTickets(customerId: number): Observable<any[]> {
    return this.http.get<any[]>(`/api/customers/${customerId}/tickets`, { headers: this.getHeaders() });
  }

  getAllTickets(): Observable<any[]> {
    return this.http.get<any[]>('/api/employees/tickets', { headers: this.getHeaders() });
  }

  createTicket(customerId: number, ticket: any): Observable<any> {
    return this.http.post<any>(`/api/customers/${customerId}/tickets`, ticket, { headers: this.getHeaders() });
  }

  addResponse(ticketId: number, response: any): Observable<any> {
    return this.http.post<any>(`/api/employees/tickets/${ticketId}/responses`, response, { headers: this.getHeaders() });
  }

  closeTicket(ticketId: number): Observable<any> {
    return this.http.put<any>(`/api/employees/tickets/${ticketId}/close`, {}, { headers: this.getHeaders() });
  }
}
