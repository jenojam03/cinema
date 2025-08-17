// src/app/components/biglietto/biglietto.component.ts

import { Component, OnInit } from '@angular/core';
import { CommonModule, DatePipe } from '@angular/common';
import {ActivatedRoute, Router} from '@angular/router';
import { BigliettoService, Biglietto } from '../services/biglietto.service';

@Component({
  selector: 'app-biglietto',
  standalone: true,
  imports: [CommonModule, DatePipe],
  templateUrl: './biglietto.component.html',
  styleUrls: ['./biglietto.component.css']
})
export class BigliettoComponent implements OnInit {

  biglietto?: Biglietto;
  isLoading = true;
  errorMessage = '';
  id: number = 0;

  constructor(
    private route: ActivatedRoute,
    private bigliettoService: BigliettoService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      const bigliettoId = params.get('id');
      if (bigliettoId) {
        this.caricaDettagliBiglietto(+bigliettoId);
      } else {
        this.errorMessage = 'ID biglietto non fornito.';
        this.isLoading = false;
      }
    });
  }

  caricaDettagliBiglietto(id: number): void {
    this.isLoading = true;
    this.bigliettoService.getBiglietto(id).subscribe({
      next: (data) => {
        console.log('Dati ricevuti dal backend:', data);
        this.biglietto = data;
        this.id = id;
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Errore nel caricamento del biglietto:', err);
        this.errorMessage = 'Errore nel caricamento dei dettagli del biglietto.';
        this.isLoading = false;
      }
    });
  }
  annullaBiglietto(): void {
    if (!this.biglietto) return;
    if (confirm('Sei sicuro di voler annullare questo biglietto? Questa operazione non può essere annullata.')) {
      this.bigliettoService.annullaBiglietto(this.id).subscribe({
        next: () => {
          this.router.navigate(['/']);
          alert('Biglietto annullato con successo!');
        },
        error: (err) => {
          console.error('Errore durante l\'annullamento:', err);
          this.errorMessage = 'Impossibile annullare il biglietto. Riprova più tardi.';
        }
      });
    }
  }
}
