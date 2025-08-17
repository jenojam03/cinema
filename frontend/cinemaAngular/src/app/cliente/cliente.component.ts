import { Component, OnInit } from '@angular/core';
import { OAuthService } from 'angular-oauth2-oidc';
import {CommonModule, DatePipe} from '@angular/common';
import {Router, RouterModule} from '@angular/router';
import {Biglietto, BigliettoService} from '../services/biglietto.service';

@Component({
  selector: 'app-cliente',
  templateUrl: './cliente.component.html',
  styleUrls: ['./cliente.component.css'],
  standalone: true,
  imports: [CommonModule, DatePipe, RouterModule]
})
export class ClienteComponent implements OnInit {

  nome = '';
  cognome = '';
  email = '';
  mieiBiglietti: Biglietto[] = [];
  isLoadingBiglietti = true;
  errorMessage = '';

  constructor(protected oauthService: OAuthService,
              private bigliettoService: BigliettoService,
              private router: Router) {}

  ngOnInit(): void {
    if (this.oauthService.hasValidAccessToken()) {
      const claims: any = this.oauthService.getIdentityClaims();
      if (claims) {
        this.nome = claims['given_name'] || '';
        this.cognome = claims['family_name'] || '';
        this.email = claims['email'] || '';
        this.caricaMieiBiglietti();
      }
    }
  }

    caricaMieiBiglietti(): void {
      this.isLoadingBiglietti = true;
      this.errorMessage = '';

      this.bigliettoService.getMieiBiglietti().subscribe({
        next: (data) => {
          this.mieiBiglietti = data;
          this.isLoadingBiglietti = false;
        },
        error: (err) => {
          console.error('Errore nel caricamento dei biglietti:', err);
          this.errorMessage = 'Errore nel caricamento dei tuoi biglietti.';
          this.isLoadingBiglietti = false;
        }
      });
  }


  dettaglioBiglietto(idBiglietto: number): void {
    console.log('ID del biglietto:', idBiglietto);
    this.router.navigate(['/biglietti', idBiglietto]);
  }

  logout(): void {
    this.oauthService.logOut();
    this.router.navigate(['/']);
  }
}
