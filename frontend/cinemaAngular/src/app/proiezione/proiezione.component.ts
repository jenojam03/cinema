import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CommonModule } from '@angular/common';
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
  isLoading = true;
  errorMessage = '';

  constructor(
    private route: ActivatedRoute,
    private proiezioneService: ProiezioneService,
    private router: Router
  ) {}

  ngOnInit(): void {
    const idProiezione = Number(this.route.snapshot.paramMap.get('id'));
    if (idProiezione) {
      this.loadProiezione(idProiezione);
      this.loadPosti(idProiezione);
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

  procediAllAcquisto(): void {
    if (this.proiezione) {
      this.router.navigate(['/acquista', this.proiezione.id]);
    } else {
      alert('Impossibile procedere. Dettagli proiezione non disponibili.');
    }
  }
}
