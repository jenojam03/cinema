import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CommonModule } from '@angular/common';
import { SharedDataService } from '../services/shareddata.service';
import { ProiezioneService, Proiezione, Posto } from '../services/proiezione.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-proiezione',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './proiezione.component.html',
  styleUrls: ['./proiezione.component.css']
})
export class ProiezioneComponent implements OnInit {
  proiezione?: Proiezione;
  postiDisponibili: Posto[] = [];
  selectedPosti: Posto[] = [];
  postiTotali: Posto[] = [];
  isLoading = true;
  errorMessage = '';

  constructor(
    private route: ActivatedRoute,
    private proiezioneService: ProiezioneService,
    private router: Router,
    private sharedDataService: SharedDataService
  ) {}

  ngOnInit(): void {
    const idProiezione = Number(this.route.snapshot.paramMap.get('id'));
    if (idProiezione) {
      this.loadProiezione(idProiezione);
      this.loadTuttiIPosti(idProiezione);
      //this.loadPosti(idProiezione);
    }
  }

  loadProiezione(id: number): void {
    this.proiezioneService.getProiezione(id).subscribe({
      next: data => {
        this.proiezione = data;
      },
      error: err => {
        console.error(err);
        this.errorMessage = 'Errore nel caricamento della proiezione.';
      }
    });
  }

  loadPosti(idProiezione: number): void {
    this.isLoading = true;
    this.proiezioneService.getPostiDisponibili(idProiezione).subscribe({
      next: data => {
        this.postiDisponibili = data;
        this.isLoading = false;
      },
      error: err => {
        console.error(err);
        this.errorMessage = 'Errore nel caricamento dei posti disponibili.';
        this.isLoading = false;
      }
    });
  }

  loadTuttiIPosti(idProiezione: number): void {
    this.isLoading = true; // Inizia il caricamento per i posti
    this.proiezioneService.getPosti(idProiezione).subscribe({ // ✅ Chiama getPosti
      next: data => {
        this.postiTotali = data; // ✅ Assegna a postiTotali
        this.isLoading = false; // Termina il caricamento
      },
      error: err => {
        console.error(err);
        this.errorMessage = 'Errore nel caricamento dei posti.';
        this.isLoading = false; // Termina il caricamento anche in caso di errore
      }
    });
  }

  selezionaPosto(posto: Posto): void {
    if (posto.occupato) {
      return;
    }
    const index = this.selectedPosti.findIndex(p => p.id === posto.id);
    if (index > -1) {
      this.selectedPosti.splice(index, 1); // Rimuovi se già selezionato
    } else {
      this.selectedPosti.push(posto); // Aggiungi se non selezionato
    }
  }

  isPostoSelezionato(posto: Posto): boolean {
    return posto.occupato;
  }


  procediAllAcquisto(): void {
    if (this.selectedPosti.length === 0) {
      alert('Seleziona almeno un posto per procedere.');
      return;
    }
    if (this.proiezione) {
      this.sharedDataService.setPostiSelezionati(this.selectedPosti);
      this.router.navigate(['/acquista', this.proiezione.id]);
    } else {
      alert('Impossibile procedere. Dettagli proiezione non disponibili.');
    }
  }
}
