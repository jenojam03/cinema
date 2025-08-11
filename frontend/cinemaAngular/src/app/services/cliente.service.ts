import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { OAuthService } from 'angular-oauth2-oidc';

export interface Cliente {
  id: number;
  nome: string;
  cognome: string;
  email: string;
}

@Injectable({
  providedIn: 'root'
})
export class ClienteService {

  private baseUrl = 'http://localhost:9090/clienti';

  constructor(private http: HttpClient, private oauthService: OAuthService) { }

  private getAuthHeaders(): HttpHeaders {
    const token = this.oauthService.getAccessToken();
    if (!token) {
      throw new Error('Token non disponibile. Utente non autenticato.');
    }
    return new HttpHeaders({
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    });
  }

  //si collega a clienti/me
  getCurrentUser(): Observable<Cliente> {
    return this.http.get<Cliente>(`${this.baseUrl}/me`, { headers: this.getAuthHeaders() });
  }

  //si collega a clienti/update
  updateUser(user: Cliente): Observable<Cliente> {
    return this.http.put<Cliente>(`${this.baseUrl}/update`, user, { headers: this.getAuthHeaders() });
  }
}
