import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { OAuthService } from 'angular-oauth2-oidc';
import { Proiezione } from './proiezione.service';
import { Posto } from './proiezione.service';

export interface AcquistoRequest {
  idProiezione: number;
  idPosti: number[];
}


export interface Biglietto {
  idBiglietto: number;
  prezzo: number;
  posto: Posto;
  proiezione: Proiezione;
  stato: 'ATTIVO' | 'ANNULLATO' | 'RIMBORSATO';
  dataAcquisto: string;
}

@Injectable({
  providedIn: 'root'
})
export class BigliettoService {

  private baseUrl = 'http://localhost:9090/biglietti';

  constructor(private http: HttpClient, private oauthService: OAuthService) { }

  private getAuthHeaders(): HttpHeaders {
    const token = this.oauthService.getAccessToken();
    if (!token) {
      throw new Error('Token di autenticazione non disponibile.');
    }
    return new HttpHeaders({
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    });
  }

  acquistaBiglietti(request: AcquistoRequest): Observable<any> {
    const headers = this.getAuthHeaders();
    return this.http.post(`${this.baseUrl}/acquista`, request, { headers });
  }

  getMieiBiglietti(): Observable<Biglietto[]> {
    const headers = this.getAuthHeaders();
    return this.http.get<Biglietto[]>(`${this.baseUrl}/miei_biglietti`, { headers : headers });
  }

  annullaBiglietto(idBiglietto: number): Observable<any> {
    const headers = this.getAuthHeaders();
    return this.http.delete(`${this.baseUrl}/${idBiglietto}`, { headers : headers, responseType: 'text' });
  }

  getBiglietto(id: number): Observable<Biglietto> {
    const headers = this.getAuthHeaders();
    return this.http.get<Biglietto>(`${this.baseUrl}/${id}`, { headers : headers });
  }
}
