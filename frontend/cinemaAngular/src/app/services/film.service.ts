import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Proiezione} from './proiezione.service';
import {Observable} from 'rxjs';

export interface Film {
  id: number;
  titolo: string;
  durata: number;
  genere: string;
  regista: string;

}

@Injectable({
  providedIn: 'root'
})
export class FilmService {

  private baseUrl = 'http://localhost:9090/film';

  constructor(private httpClient: HttpClient) { }

  getFilm(): Observable<Film[]> {
    return this.httpClient.get<Film[]>(`${this.baseUrl}`);
  }

  getFilmById(id: number): Observable<Film> {
    return this.httpClient.get<Film>(`${this.baseUrl}/${id}`);
  }

  getFilmByTitolo(titolo: string): Observable<Film[]> {
    return this.httpClient.get<Film[]>(`${this.baseUrl}/search/nome/{titolo}`);
  }
  getFilmByAutore(autore: string): Observable<Film[]> {
    return this.httpClient.get<Film[]>(`${this.baseUrl}/search/autore/{autore}`);
  }
  getFilmByGenere(titolo: string): Observable<Film[]> {
    return this.httpClient.get<Film[]>(`${this.baseUrl}/search/genere/{genere}`);
  }

  getProiezioniByFilmId(id: number): Observable<Proiezione[]> {
    return this.httpClient.get<Proiezione[]>(`${this.baseUrl}/${id}/proiezioni`);
  }

}
