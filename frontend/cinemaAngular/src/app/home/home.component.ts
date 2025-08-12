import {Component, OnInit} from '@angular/core';
import {CommonModule} from '@angular/common';
import {OAuthService, UserInfo} from 'angular-oauth2-oidc';
import {Router} from '@angular/router';
import {FormsModule} from '@angular/forms';
import {Subscription} from 'rxjs';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent implements OnInit {

  constructor(
    private oauthService: OAuthService,
    private router: Router) { }

    userName: string = '';
    private authEventsSubscription!: Subscription;


    ngOnInit(): void {
      this.checkLoginStatus();
      this.authEventsSubscription = this.oauthService.events.subscribe(event => {
        if (event.type === 'token_received' || event.type === 'token_refreshed' || event.type === 'logout') {
          this.checkLoginStatus();
        }
      });
    }

  private checkLoginStatus(): void {
    if (this.oauthService.hasValidAccessToken()) {
      this.oauthService.loadUserProfile().then((info: any) => {
        this.userName = info.name || 'Utente';
      }).catch(err => {
        console.error('Errore nel caricamento del profilo utente:', err);
        this.userName = 'Utente';
      });
    } else {
      this.userName = 'Utente';
    }
  }

  isLoggedIn(): boolean {
    return this.oauthService.hasValidAccessToken();
  }

  vaiAlLogin(): void {
    this.oauthService.initCodeFlow();
    //redireziona a keycloak se non autenticato
  }

  logout(): void {
    this.oauthService.logOut();
    this.router.navigate(['/']);
  }

  vaiAlCatalogoFilm(): void {
    this.router.navigate(['/film']);
  }

  vaiAlCatalogoProiezioni(): void {
    this.router.navigate(['/proiezioni']);
  }

}
