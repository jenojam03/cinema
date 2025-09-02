import { Injectable } from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot} from '@angular/router';
import {OAuthService} from 'angular-oauth2-oidc';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {
  constructor(private oauthService: OAuthService, private router: Router) {}

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
    if (this.oauthService.hasValidAccessToken()) {
      return true;
    } else {
      //se non è autenticato viene reindirizzato ad auntenticarsi
      this.oauthService.initCodeFlow(state.url);
      const returnUrl = sessionStorage.getItem('returnUrl');
      if (returnUrl) {
        //dopo login navigo alla pagina lasciata in sospeso
        this.router.navigateByUrl(returnUrl);
        sessionStorage.removeItem('returnUrl');
      }
      return false;
    }
  }
}
