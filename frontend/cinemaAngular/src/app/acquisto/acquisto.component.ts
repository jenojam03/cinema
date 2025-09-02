import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { BigliettoService, AcquistoRequest } from '../services/biglietto.service';
import { ProiezioneService, Posto, Proiezione } from '../services/proiezione.service';
import { OAuthService } from 'angular-oauth2-oidc';
import { SharedDataService } from '../services/shareddata.service';

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
  postiSelezionati: Posto[] = [];

  prezzoAcquisto: number = 0;
  prezzoBase: number = 0;

  isLoading = true;
  isProcessing = false;
  errorMessage = '';
  successMessage = '';

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private bigliettoService: BigliettoService,
    private proiezioneService: ProiezioneService,
    private oauthService: OAuthService,
    private sharedDataService: SharedDataService
  ) {}

  ngOnInit(): void {
    this.postiSelezionati = this.sharedDataService.getPostiSelezionati();

    const savedPosti = sessionStorage.getItem('postiSelezionati');
    if (savedPosti) {
      this.postiSelezionati = JSON.parse(savedPosti);
      sessionStorage.removeItem('postiSelezionati');
    }
    this.route.paramMap.subscribe(params => {
      const id = params.get('id');
      if (id) {
        this.proiezioneId = +id;
        if (this.postiSelezionati.length > 0) {
          this.loadProiezioneEPosti(this.proiezioneId);
          this.calcolaPrezzo();
          this.getPrezzoBase();
        } else {
          //se non ci sono posti selezionati reindirizza
          this.router.navigate(['/proiezioni', this.proiezioneId]);
          this.isLoading = false;
        }
      } else {
        this.errorMessage = 'ID proiezione non fornito.';
        this.isLoading = false;
      }
    });

  }

  calcolaPrezzo(): void {
    if (!this.proiezioneId || this.postiSelezionati.length === 0) {
      this.prezzoAcquisto = 0;
      return;
    }

    this.bigliettoService.getPrezzo(this.postiSelezionati.length, this.proiezioneId)
      .subscribe({
        next: prezzo => this.prezzoAcquisto = prezzo,
        error: () => this.prezzoAcquisto = 0
      });
  }

  getPrezzoBase():void {
    this.bigliettoService.getPrezzoBase(this.proiezioneId)
      .subscribe({
        next: prezzo => this.prezzoBase = prezzo,
        error: () => this.prezzoBase = 0
      });
  }


  loadProiezioneEPosti(id: number): void {
    this.isLoading = true;
    this.proiezioneService.getProiezione(id).subscribe({
      next: data => {
        this.proiezione = data;
        this.proiezioneService.getPostiDisponibili(id).subscribe({
          next: posti => {
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


  confermaAcquisto(): void {
    if (!this.oauthService.hasValidAccessToken()) {
      alert('Devi essere loggato per effettuare un acquisto.');
      sessionStorage.setItem('returnUrl', this.router.url);
      sessionStorage.setItem('postiSelezionati', JSON.stringify(this.postiSelezionati));

      this.oauthService.initCodeFlow(this.router.url);
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
      idPosti: this.postiSelezionati.map(posto => posto.id)
    };

    this.bigliettoService.acquistaBiglietti(acquistoRequest).subscribe({
      next: () => {
        this.successMessage = 'Acquisto effettuato con successo!';
        this.isProcessing = false;
        alert(this.successMessage);
        this.router.navigate(['/cliente']);
      },
      error: (err) => {
        this.errorMessage = 'Errore durante l\'acquisto. Riprova più tardi.';
        this.isProcessing = false;
        console.error('Errore acquisto:', err);
      }
    });
  }
}
