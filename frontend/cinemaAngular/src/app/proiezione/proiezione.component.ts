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


  loadTuttiIPosti(idProiezione: number): void {
    this.isLoading = true;
    this.proiezioneService.getPosti(idProiezione).subscribe({
      next: data => {
        this.postiTotali = data;
        this.isLoading = false;
      },
      error: err => {
        console.error(err);
        this.errorMessage = 'Errore nel caricamento dei posti.';
        this.isLoading = false;
      }
    });
  }

  selezionaPosto(posto: Posto): void {
    if (posto.occupato) {
      return;
    }
    const index = this.selectedPosti.findIndex(p => p.id === posto.id);
    if (index > -1) {
      this.selectedPosti.splice(index, 1);
    } else {
      this.selectedPosti.push(posto);
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
      this.errorMessage = 'Impossibile procedere. Dettagli proiezione non disponibili.';
    }
  }
}
