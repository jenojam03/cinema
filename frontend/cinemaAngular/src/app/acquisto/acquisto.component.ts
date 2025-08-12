// src/app/components/acquisto/acquisto.component.ts

import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { BigliettoService, AcquistoRequest } from '../services/biglietto.service';
import { ProiezioneService, Posto, Proiezione } from '../services/proiezione.service';
import { OAuthService } from 'angular-oauth2-oidc';

@Component({
  selector: 'app-acquisto',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './acquisto.component.html',
  styleUrls: ['./acquisto.component.css']
})
export class AcquistoComponent implements OnInit {
  proiezioneId: number = 0;
  proiezione?: Proiezione;
  postiDisponibili: Posto[] = [];

  postiSelezionati: number[] = [];
  prezzoBiglietto: number = 8.50; // Prezzo fisso per esempio

  isLoading = true;
  isProcessing = false;
  errorMessage = '';
  successMessage = '';

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private bigliettoService: BigliettoService,
    private proiezioneService: ProiezioneService,
    private oauthService: OAuthService
  ) {}

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      const id = params.get('id');
      if (id) {
        this.proiezioneId = +id;
        this.loadProiezioneEPosti(this.proiezioneId);
      } else {
        this.errorMessage = 'ID proiezione non fornito.';
        this.isLoading = false;
      }
    });
  }

  loadProiezioneEPosti(id: number): void {
    this.isLoading = true;
    this.proiezioneService.getProiezione(id).subscribe({
      next: data => {
        this.proiezione = data;
        this.proiezioneService.getPostiDisponibili(id).subscribe({
          next: posti => {
            this.postiDisponibili = posti;
            this.isLoading = false;
          },
          error: () => {
            this.errorMessage = 'Errore nel caricamento dei posti.';
            this.isLoading = false;
          }
        });
      },
      error: () => {
        this.errorMessage = 'Errore nel caricamento della proiezione.';
        this.isLoading = false;
      }
    });
  }

  togglePosto(postoId: number): void {
    const index = this.postiSelezionati.indexOf(postoId);
    if (index > -1) {
      this.postiSelezionati.splice(index, 1);
    } else {
      this.postiSelezionati.push(postoId);
    }
  }

  getPrezzoTotale(): number {
    return this.postiSelezionati.length * this.prezzoBiglietto;
  }

  confermaAcquisto(): void {
    if (!this.oauthService.hasValidAccessToken()) {
      alert('Devi essere loggato per effettuare un acquisto.');
      this.oauthService.initCodeFlow();
      return;
    }

    if (this.postiSelezionati.length === 0) {
      alert('Seleziona almeno un posto.');
      return;
    }

    this.isProcessing = true;
    this.errorMessage = '';

    const acquistoRequest: AcquistoRequest = {
      idProiezione: this.proiezioneId,
      idPosti: this.postiSelezionati
    };

    this.bigliettoService.acquistaBiglietti(acquistoRequest).subscribe({
      next: () => {
        this.successMessage = 'Acquisto effettuato con successo!';
        this.isProcessing = false;
        // Reindirizza l'utente o aggiorna la UI
      },
      error: (err) => {
        this.errorMessage = 'Errore durante l\'acquisto. Riprova più tardi.';
        this.isProcessing = false;
        console.error('Errore acquisto:', err);
      }
    });
  }
}
