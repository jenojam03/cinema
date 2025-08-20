import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Film, FilmService } from '../services/film.service';
import { FormsModule } from '@angular/forms';
import {Router} from '@angular/router';

@Component({
  selector: 'app-catalogo-film',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './catalogofilm.component.html',
  styleUrls: ['./catalogofilm.component.css']
})
export class CatalogoFilmComponent implements OnInit {

  film: Film[] = [];
  valore = '';
  errorMessage: string = '';
  criterio: 'titolo' | 'autore' | 'genere' = 'titolo';//di default è titolo
  isLoading = true;
  generiDisponibili: string[] = ['Animazione', 'Azione', 'Avventura','Biografico', 'Commedia', 'Crime', 'Drammatico', 'Fantasy', 'Fantascienza', 'Horror', 'Korean drama', 'Romance','Thriller','Western'];

  //paginazione
  currentPage = 0;
  pageSize = 12;
  totalPages = 0;

  constructor(private filmService: FilmService, private router: Router) {}

  ngOnInit(): void {
    this.caricaFilm();
  }

  caricaFilm(page: number = 0): void {
    this.isLoading = true;
    this.errorMessage = '';
    this.filmService.getFilm(page, this.pageSize).subscribe({
      next: (data:any) => {
        this.film = data.content;
        this.currentPage = data.number;
        this.totalPages = data.totalPages;
        this.isLoading = false;
      },
      error: err => {
        this.errorMessage = 'Errore nel caricamento dei film.';
        console.error(err);
        this.isLoading = false;
      }
    });
  }

  cerca(page: number = 0): void {
    if (!this.valore.trim()) {
      this.caricaFilm();
      return;
    }
    this.isLoading = true;
    let chiamata;
    switch (this.criterio) {
      case 'titolo':
        chiamata = this.filmService.getFilmByTitolo(this.valore, page, this.pageSize);
        break;
      case 'autore':
        chiamata= this.filmService.getFilmByAutore(this.valore, page, this.pageSize);
        break;
      case 'genere':
        chiamata = this.filmService.getFilmByGenere(this.valore, page, this.pageSize);
        break;
    }

    chiamata!.subscribe({
      next: (data:any) => {
        this.errorMessage = '';
        this.film = data.content;
        this.currentPage = data.number;
        this.totalPages = data.totalPages;
        this.isLoading = false;
      },
      error: err => {
        console.error(err);
        this.isLoading = false;
          this.errorMessage = 'Nessun film trovato.'
          this.film = [];

      }
    });
  }

  mostraDettagli(idFilm: number): void {
    this.router.navigate(['/film', idFilm]);
  }

  goToPage(page: number): void {
    if (page >= 0 && page < this.totalPages) {
      if (this.valore.trim()) {
        this.cerca(page);
      } else {
        this.caricaFilm(page);
      }
    }
  }

  getPageNumbers(): number[] {
    return Array.from({ length: this.totalPages }, (_, i) => i);
  }
}
