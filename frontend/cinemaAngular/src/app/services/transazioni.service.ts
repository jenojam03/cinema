import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { OAuthService } from 'angular-oauth2-oidc';
import { Cliente } from './cliente.service';

export interface Transazione {
  id: number;
  data: string;
  importo: number;
  esito: boolean;
  cliente: Cliente;
}

@Injectable({
  providedIn: 'root'
})
export class TransazioniService {

  private baseUrl = 'http://localhost:9090/transazioni';

  constructor(private httpClient: HttpClient, private oauthService: OAuthService) { }

  private getHeaders(): HttpHeaders {
    const token = this.oauthService.getAccessToken();
    return new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });
  }

  getAllTransazioni(): Observable<Transazione[]> {
    const headers = this.getHeaders();
    return this.httpClient.get<Transazione[]>(`${this.baseUrl}`, { headers });
  }

  getTransazioniByClienteEmail(email: string): Observable<Transazione[]> {
    const headers = this.getHeaders();
    const params = new HttpParams().set('email', email);
    return this.httpClient.get<Transazione[]>(`${this.baseUrl}/cliente`, { headers, params });
  }

  getTransazioniByEsito(esito: boolean): Observable<Transazione[]> {
    const headers = this.getHeaders();
    const params = new HttpParams().set('esito', esito.toString());
    return this.httpClient.get<Transazione[]>(`${this.baseUrl}/esito`, { headers, params });
  }
}
