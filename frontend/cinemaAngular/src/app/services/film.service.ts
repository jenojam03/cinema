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

export interface PagedResponse<T> {
  content: T[];
  pageable: {
    pageNumber: number;
    pageSize: number;
    sort: {
      sorted: boolean;
      unsorted: boolean;
    };
    offset: number;
    paged: boolean;
    unpaged: boolean;
  };
  last: boolean;
  totalPages: number;
  totalElements: number;
  size: number;
  number: number;
  sort: {
    sorted: boolean;
    unsorted: boolean;
  };
  first: boolean;
  numberOfElements: number;
  empty: boolean;
}


@Injectable({
  providedIn: 'root'
})
export class FilmService {

  private baseUrl = 'http://localhost:9090/film';

  constructor(private httpClient: HttpClient) { }

  getFilm(page: number, size: number): Observable<PagedResponse<Film>> {
    return this.httpClient.get<PagedResponse<Film>>(`${this.baseUrl}/paged?page=${page}&size=${size}`);
  }

  getFilmById(id: number): Observable<Film> {
    return this.httpClient.get<Film>(`${this.baseUrl}/${id}`);
  }

  getFilmByTitolo(titolo: string, page: number, size: number): Observable<PagedResponse<Film>> {
    return this.httpClient.get<PagedResponse<Film>>(`${this.baseUrl}/search/paged/titolo/${titolo}?page=${page}&size=${size}`);
  }

  getFilmByAutore(autore: string, page: number, size: number): Observable<PagedResponse<Film>> {
    return this.httpClient.get<PagedResponse<Film>>(`${this.baseUrl}/search/paged/regista/${autore}?page=${page}&size=${size}`);
  }

  getFilmByGenere(genere: string, page: number, size: number): Observable<PagedResponse<Film>> {
    return this.httpClient.get<PagedResponse<Film>>(`${this.baseUrl}/search/paged/genere/${genere}?page=${page}&size=${size}`);
  }

  getProiezioniByFilmIdPaged(id: number, page: number, size: number): Observable<PagedResponse<Proiezione>> {
    return this.httpClient.get<PagedResponse<Proiezione>>(`${this.baseUrl}/${id}/proiezioni/paged?page=${page}&size=${size}`);
  }

  //proiezioni di un film non paginate perchè di solito sono poche
  getProiezioniByFilmId(id: number): Observable<Proiezione[]> {
    return this.httpClient.get<Proiezione[]>(`${this.baseUrl}/${id}/proiezioni`);
  }
}
