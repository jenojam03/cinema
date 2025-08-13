import { Injectable } from '@angular/core';
import { Film } from './film.service';
import {HttpClient, HttpParams} from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Proiezione {
  id: number;
  dataOra: string;
  numeroPosti: number;
  film: Film;

}
export interface Posto {
  id: number;
  riga: string;
  colonna: number;
  occupato: boolean;
}

@Injectable({
  providedIn: 'root'
})
export class ProiezioneService {

  private baseUrl = 'http://localhost:9090/proiezioni';

  constructor(private httpClient: HttpClient) { }

  getProiezioni(): Observable<Proiezione[]> {
    return this.httpClient.get<Proiezione[]>(`${this.baseUrl}`);
  }

  getProiezioniByData(data: string): Observable<Proiezione[]> {
    const params = new HttpParams().set('data', data);
    return this.httpClient.get<Proiezione[]>(`${this.baseUrl}/byDate`,{params});
  }

  getProiezioniBetweenDates(inizio: string, fine: string): Observable<Proiezione[]> {
    const params = new HttpParams()
      .set('inizio', inizio)
      .set('fine', fine);

    return this.httpClient.get<Proiezione[]>(`${this.baseUrl}/betweenDates`, { params });
  }

  getPostiDisponibili(idProiezione: number): Observable<Posto[]> {
    return this.httpClient.get<Posto[]>(`${this.baseUrl}/${idProiezione}/posti_disponibili`);
  }

  getProiezione(idProiezione: number): Observable<Proiezione> {
    return this.httpClient.get<Proiezione>(`${this.baseUrl}/${idProiezione}`);
  }


}
