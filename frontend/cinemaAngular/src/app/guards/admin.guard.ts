import { Injectable } from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree} from '@angular/router';
import { OAuthService } from 'angular-oauth2-oidc';
import {Observable} from 'rxjs';
import {AuthService} from '../services/auth.service';

@Injectable({
  providedIn: 'root'
})
export class AdminGuard implements CanActivate {
  constructor(private oauthService: OAuthService, private router: Router, private authService: AuthService) { }

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {


    if (this.isLoggedIn() && this.authService.isAdmin()) {
      return true;
    } else {
      //se l'utente non è autorizzato lo reindirizza alla home
      this.router.navigate(['/']);
      return false;
    }
  }

  private isLoggedIn(): boolean {
    return this.oauthService.hasValidAccessToken();
  }

}
