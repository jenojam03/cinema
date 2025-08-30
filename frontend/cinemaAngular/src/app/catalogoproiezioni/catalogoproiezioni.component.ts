import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Proiezione, ProiezioneService } from '../services/proiezione.service';
import { Router } from '@angular/router';

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
  oggi: string = '';
  //paginazione
  currentPage = 0;
  totalPages = 0;
  pageSize = 15;

  constructor(private proiezioneService: ProiezioneService,
              private router: Router) {}

  ngOnInit(): void {
    this.caricaProiezioni();
    const oggiDate = new Date();
    const anno = oggiDate.getFullYear();
    const mese = (oggiDate.getMonth() + 1).toString().padStart(2, '0');
    const giorno = oggiDate.getDate().toString().padStart(2, '0');
    this.oggi = `${anno}-${mese}-${giorno}`;
  }

  caricaProiezioni(page: number = 0): void {
    this.isLoading = true;
    this.errorMessage = '';
    this.proiezioneService.getProiezioni(page, this.pageSize).subscribe({
      next: (data) => {
        this.proiezioni = data.content;
        this.currentPage = data.number;
        this.totalPages = data.totalPages;
        this.isLoading = false;
      },
      error: (err) => {
        this.errorMessage = 'Errore nel caricamento delle proiezioni';
        this.isLoading = false;
        console.error(err);
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

  filtra(page: number = 0): void {
    this.errorMessage = '';
    if (this.filtroDataInizio && this.filtroDataFine) {
      const dataInizio = new Date(this.filtroDataInizio);
      const dataFine = new Date(this.filtroDataFine);
      if (dataInizio > dataFine) {
        this.errorMessage = 'La data di inizio non può essere successiva alla data di fine.';
        this.proiezioni = [];
        this.totalPages = 0;
        return;
      }
      const oggi = new Date();

      //perchè sennò conta anche i minuti e se selezioni oggi dà errore
      const oggiSoloData = new Date(oggi.getFullYear(), oggi.getMonth(), oggi.getDate());
      const dataInizioSoloData = new Date(dataInizio.getFullYear(), dataInizio.getMonth(), dataInizio.getDate());
      const dataFineSoloData = new Date(dataFine.getFullYear(), dataFine.getMonth(), dataFine.getDate());

      if (dataInizioSoloData < oggiSoloData) {
        this.errorMessage = 'Non puoi selezionare date passate';
        return;
      }
      if (dataFineSoloData < oggiSoloData) {
        this.errorMessage = 'Non puoi selezionare date passate';
        return;
      }
    }

    const dataInizioFormattata = this.formattaData(this.filtroDataInizio);
    const dataFineFormattata = this.formattaData(this.filtroDataFine);

    this.isLoading = true;

    if (dataInizioFormattata && dataFineFormattata) {
      this.proiezioneService.getProiezioniBetweenDates(dataInizioFormattata, dataFineFormattata, page, this.pageSize).subscribe({
        next: (data) => {
          this.proiezioni = data.content;
          this.currentPage = data.number;
          this.totalPages = data.totalPages;
          this.isLoading = false;
        },
        error: (err) => {
          this.errorMessage = 'Nessuna proiezione trovata per il periodo selezionato';
          this.proiezioni = [];
          this.totalPages = 0;
          this.isLoading = false;
          console.error(err);
        }
      });
    } else if (dataInizioFormattata) {
      this.isLoading = true;
      this.proiezioneService.getProiezioniByData(dataInizioFormattata, page, this.pageSize).subscribe({
        next: (data) => {
          this.proiezioni = data.content;
          this.currentPage = data.number;
          this.totalPages = data.totalPages;
          this.isLoading = false;
        },
        error: (err) => {
          this.errorMessage = 'Nessuna proiezione trovata per la data selezionata';
          this.proiezioni = [];
          this.totalPages = 0;
          this.isLoading = false;
          console.error(err);
        }
      });
    } else {
      this.caricaProiezioni(page);
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

  goToPage(page: number): void {
    if (page >= 0 && page < this.totalPages) {
      if (this.filtroDataInizio || this.filtroDataFine) {
        //se ci sono filtri li applica
        this.filtra(page);
      } else {
        this.caricaProiezioni(page);
      }
    }
  }

  getPageNumbers(): number[] {
    return Array.from({ length: this.totalPages }, (_, i) => i);
  }
}
