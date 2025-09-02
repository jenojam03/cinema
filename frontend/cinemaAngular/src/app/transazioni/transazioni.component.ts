import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Transazione, TransazioniService } from '../services/transazioni.service';
import { OAuthService } from 'angular-oauth2-oidc';

@Component({
  selector: 'app-transazioni',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './transazioni.component.html',
  styleUrls: ['./transazioni.component.css']
})
export class TransazioniComponent implements OnInit {

  transazioni: Transazione[] = [];
  isLoading = true;
  errorMessage = '';

  //filtri
  filtroEmail = '';
  filtroEsito: 'tutte' | 'successo' | 'fallito' = 'tutte';

  constructor(
    private transazioniService: TransazioniService,
    private oauthService: OAuthService
  ) { }

  ngOnInit(): void {
    this.loadTransazioni();
  }

  loadTransazioni(): void {
    this.isLoading = true;
    this.errorMessage = '';
    this.transazioniService.getAllTransazioni().subscribe({
      next: (data) => {
        this.transazioni = data;
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Errore nel caricamento delle transazioni:', err);
        this.errorMessage = 'Non hai i permessi per visualizzare le transazioni.';
        this.isLoading = false;
      }
    });
  }

  applicaFiltri(): void {
    this.isLoading = true;
    this.errorMessage = '';

    if (this.filtroEmail.trim() !== '') {
      //filtra per email
      this.transazioniService.getTransazioniByClienteEmail(this.filtroEmail).subscribe({
        next: (data) => {
          this.transazioni = data;
          this.isLoading = false;
        },
        error: () => this.handleError()
      });
    } else if (this.filtroEsito !== 'tutte') {
      //filtra per esito
      const esitoBoolean = this.filtroEsito === 'successo';
      this.transazioniService.getTransazioniByEsito(esitoBoolean).subscribe({
        next: (data) => {
          this.transazioni = data;
          this.isLoading = false;
        },
        error: () => this.handleError()
      });
    } else {
      //carica tutto senza filtri
      this.loadTransazioni();
    }
  }

  resetFiltri(): void {
    this.filtroEmail = '';
    this.filtroEsito = 'tutte';
    this.loadTransazioni();
  }

  private handleError(): void {
    this.errorMessage = 'Errore durante la ricerca. Verifica i permessi.';
    this.isLoading = false;
  }
}
