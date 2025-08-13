import { Injectable } from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree} from '@angular/router';
import { OAuthService } from 'angular-oauth2-oidc';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AdminGuard implements CanActivate {
  constructor(private oauthService: OAuthService, private router: Router) {}

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {

    const userRoles = this.getUserRoles();

    if (this.isLoggedIn() && userRoles.includes('admin')) {
      return true;
    } else {
      // Reindirizza l'utente non autorizzato alla home
      this.router.navigate(['/']);
      return false;
    }
  }

  private isLoggedIn(): boolean {
    return this.oauthService.hasValidAccessToken();
  }

  private getUserRoles(): string[] {
    const claims = this.oauthService.getIdentityClaims() as any;
    // Controlla se le claim contengono un campo "roles" (o "realm_access.roles")
    // e restituisci i ruoli. Potrebbe essere necessario adattare il percorso.
    return claims?.realm_access?.roles || [];
  }
}
