import { Injectable } from '@angular/core';
import {Film, PagedResponse} from './film.service';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Proiezione {
  id: number;
  dataOra: string;
  numeroPosti: number;
  film: Film;
}

export interface Posto {
  id: number;
  fila: string;
  numero: number;
  occupato: boolean;
}

@Injectable({
  providedIn: 'root'
})
export class ProiezioneService {

  private baseUrl = 'http://localhost:9090/proiezioni';

  constructor(private httpClient: HttpClient) { }

  getProiezioni(page: number, size: number): Observable<PagedResponse<Proiezione>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    return this.httpClient.get<PagedResponse<Proiezione>>(`${this.baseUrl}/future`, { params });
  }

  getProiezioniByData(data: string, page: number, size: number): Observable<PagedResponse<Proiezione>> {
    const params = new HttpParams()
      .set('data', data)
      .set('page', page.toString())
      .set('size', size.toString());
    return this.httpClient.get<PagedResponse<Proiezione>>(`${this.baseUrl}/byDate`, { params });
  }

  getProiezioniBetweenDates(inizio: string, fine: string, page: number, size: number): Observable<PagedResponse<Proiezione>> {
    const params = new HttpParams()
      .set('inizio', inizio)
      .set('fine', fine)
      .set('page', page.toString())
      .set('size', size.toString());
    return this.httpClient.get<PagedResponse<Proiezione>>(`${this.baseUrl}/betweenDatesPaged`, { params });
  }

  getPostiDisponibili(idProiezione: number): Observable<Posto[]> {
    return this.httpClient.get<Posto[]>(`${this.baseUrl}/${idProiezione}/posti_disponibili`);
  }

  getPosti(idProiezione: number): Observable<Posto[]> {
    return this.httpClient.get<Posto[]>(`${this.baseUrl}/${idProiezione}/posti`);
  }

  getProiezione(idProiezione: number): Observable<Proiezione> {
    return this.httpClient.get<Proiezione>(`${this.baseUrl}/${idProiezione}`);
  }
}
