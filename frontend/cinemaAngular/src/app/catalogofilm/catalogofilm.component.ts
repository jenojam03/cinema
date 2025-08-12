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

  constructor(private filmService: FilmService, private router: Router) {}

  ngOnInit(): void {
    this.caricaFilm();
  }

  caricaFilm(): void {
    this.isLoading = true;
    this.errorMessage = '';
    this.filmService.getFilm().subscribe({
      next: (data) => {
        this.film = data;
        this.isLoading = false;
      },
      error: err => {
        this.errorMessage = 'Errore nel caricamento dei film.';
        console.error(err);
        this.isLoading = false;
      }
    });
  }

  cerca(): void {
    if (!this.valore.trim()) {
      this.caricaFilm();
      return;
    }
    this.isLoading = true;
    let chiamata;
    switch (this.criterio) {
      case 'titolo':
        chiamata = this.filmService.getFilmByTitolo(this.valore);
        break;
      case 'autore':
        chiamata= this.filmService.getFilmByAutore(this.valore);
        break;
      case 'genere':
        chiamata = this.filmService.getFilmByGenere(this.valore);
        break;
    }

    chiamata!.subscribe({
      next: data => {
        this.film = data;
        this.isLoading = false;
      },
      error: err => {
        this.errorMessage = 'Errore nella ricerca.';
        console.error(err);
        this.isLoading = false;
      }
    });
  }
  mostraProiezioni(idFilm: number): void {
    this.router.navigate(['/film', idFilm, 'proiezioni']);
  }
  mostraDettagli(idFilm: number): void {
    this.router.navigate(['/film', idFilm]);
  }
}
