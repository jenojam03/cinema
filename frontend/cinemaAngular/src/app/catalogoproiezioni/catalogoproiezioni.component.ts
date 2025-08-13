import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Proiezione, ProiezioneService } from '../services/proiezione.service';
import {ActivatedRoute, Router} from '@angular/router';

@Component({
  selector: 'app-catalogo-proiezioni',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './catalogoproiezioni.component.html',
  styleUrls: ['./catalogoproiezioni.component.css']
})
export class CatalogoProiezioniComponent implements OnInit {

  proiezioni: Proiezione[] = [];
  isLoading = true;
  errorMessage = '';

  filtroDataInizio = '';
  filtroDataFine = '';

  constructor(private proiezioneService: ProiezioneService,
              private router: Router) {}

  ngOnInit(): void {
    this.caricaProiezioni();
  }

  caricaProiezioni(): void {
    this.isLoading = true;
    this.errorMessage = '';
    this.proiezioneService.getProiezioni().subscribe({
      next: (data) => {
        this.proiezioni = data;
        this.isLoading = false;
      },
      error: () => {
        this.errorMessage = 'Errore nel caricamento delle proiezioni';
        this.isLoading = false;
      }
    });
  }

  filtra(): void {
    if (this.filtroDataInizio && this.filtroDataFine) {
      this.isLoading = true;
      this.proiezioneService.getProiezioniBetweenDates(this.filtroDataInizio, this.filtroDataFine).subscribe({
        next: (data) => {
          this.proiezioni = data;
          this.isLoading = false;
        },
        error: () => {
          this.errorMessage = 'Errore nel filtraggio delle proiezioni';
          this.isLoading = false;
        }
      });
    } else if (this.filtroDataInizio) {
      this.isLoading = true;
      this.proiezioneService.getProiezioniByData(this.filtroDataInizio).subscribe({
        next: (data) => {
          this.proiezioni = data;
          this.isLoading = false;
        },
        error: () => {
          this.errorMessage = 'Errore nel filtraggio delle proiezioni';
          this.isLoading = false;
        }
      });
    } else {
      this.caricaProiezioni();
    }
  }

  vaiADettaglioProiezione(idProiezione: number): void {
    this.router.navigate(['/proiezioni', idProiezione]);
  }
}
