import {Component, OnInit} from '@angular/core';
import {CommonModule} from '@angular/common';
import {Film, FilmService} from '../services/film.service';
import {OAuthService} from 'angular-oauth2-oidc';
import {Router} from '@angular/router';

@Component({
  selector: 'app-home',
  imports: [CommonModule],
  templateUrl: './home.component.html',
  standalone: true,
  styleUrl: './home.component.css'
})
export class HomeComponent implements OnInit {

  catalogoFilm: Film[] = [];
  isLoading: boolean = true;
  errorMessage: string = '';

  constructor(
    private filmService: FilmService,
    private oauthService: OAuthService,
    private router: Router) { }

  ngOnInit(): void {
      this.loadFilm();
    }


  loadFilm(): void {
    this.filmService.getFilm().subscribe(
      film => {
        this.catalogoFilm = film;
        this.isLoading = false;
      },
      error => {
        console.error('Errore nel caricamento film:', error);
        this.errorMessage = 'Errore nel caricamento dei film.';
        this.isLoading = false;
      }
    );
  }

  mostraProiezioni(idFilm: number): void {
    this.router.navigate(['/film', idFilm, 'proiezioni']);
  }
}
