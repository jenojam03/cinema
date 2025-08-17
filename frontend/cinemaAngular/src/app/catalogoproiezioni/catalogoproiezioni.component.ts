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

  private formattaData(data: string): string {
    if (!data) return '';
    const d = new Date(data);
    const giorno = d.getDate().toString().padStart(2, '0');
    const mese = (d.getMonth() + 1).toString().padStart(2, '0');
    const anno = d.getFullYear();
    return `${giorno}-${mese}-${anno}`;
  }

  filtra(): void {
    this.errorMessage = '';
    if (this.filtroDataInizio && this.filtroDataFine) {
      const dataInizio = new Date(this.filtroDataInizio);
      const dataFine = new Date(this.filtroDataFine);
      if (dataInizio > dataFine) {
        this.errorMessage = 'La data di inizio non può essere successiva alla data di fine.';
        this.proiezioni = [];
        return;
      }
    }

    const dataInizioFormattata = this.formattaData(this.filtroDataInizio);
    const dataFineFormattata = this.formattaData(this.filtroDataFine);



    if (dataInizioFormattata && dataFineFormattata) {
      this.isLoading = true;
      this.proiezioneService.getProiezioniBetweenDates(dataInizioFormattata, dataFineFormattata).subscribe({
        next: (data) => {
          this.proiezioni = data;
          this.isLoading = false;
        },
        error: () => {
          this.errorMessage = 'Errore nel filtraggio delle proiezioni';
          this.isLoading = false;
        }
      });
    } else if (dataInizioFormattata) {
      this.isLoading = true;
      this.proiezioneService.getProiezioniByData(dataInizioFormattata).subscribe({
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

  resetFiltri(): void {
    this.filtroDataInizio = '';
    this.filtroDataFine = '';
    this.caricaProiezioni();
  }

  vaiADettaglioProiezione(idProiezione: number): void {
    this.router.navigate(['/proiezioni', idProiezione]);
  }
}
