import {Component, OnInit} from '@angular/core';
import {CommonModule} from '@angular/common';
import {Film, FilmService} from '../services/film.service';
import {Proiezione} from '../services/proiezione.service';
import {ActivatedRoute, Router} from '@angular/router';

@Component({
  selector: 'app-film',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './film.component.html',
  styleUrls: ['./film.component.css']
})
export class FilmComponent implements OnInit{

  film?: Film;
  proiezioni: Proiezione[] = [];
  isLoading = true;
  errorMessage = '';

  //Paginazione
  currentPage = 0;
  totalPages = 0;
  pageSize = 15;

  constructor(
    private route: ActivatedRoute,
    private filmService: FilmService,
    private router: Router
  ) {}

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    if (id) {
      this.loadFilm(id);
      this.loadProiezioni(id);
    }
  }

  loadFilm(id: number): void {
    this.filmService.getFilmById(id).subscribe({
      next: data => {
        this.film = data;
        this.isLoading = false;
      },
      error: err => {
        console.error(err);
        this.errorMessage = 'Errore nel caricamento del film.';
        this.isLoading = false;
      }
    });
  }

  loadProiezioni(id: number, page: number = 0): void {
    this.isLoading = true;
    this.filmService.getProiezioniByFilmId(id,page,this.pageSize).subscribe({
      next: data => {
        this.proiezioni = data;
        /*this.proiezioni = data.content;
        this.currentPage = data.number;
        this.totalPages = data.totalPages;*/
        this.isLoading = false;
      },
      error: err => {
        console.error(err);
        this.errorMessage = 'Errore nel caricamento delle proiezioni.';
      }
    });
  }

  vaiADettaglioProiezione(idProiezione: number) {
    this.router.navigate(['/proiezioni', idProiezione]);
  }

  goToPage(page: number): void {
    if (page >= 0 && page < this.totalPages) {
      const id = Number(this.route.snapshot.paramMap.get('id'))
      this.loadProiezioni(id, page);
    }
  }

  getPageNumbers() {
    return Array.from({ length: this.totalPages }, (_, i) => i);
  }
}
