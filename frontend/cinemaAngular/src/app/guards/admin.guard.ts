import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { KeycloakService } from 'keycloak-angular';

@Injectable({
  providedIn: 'root'
})
export class AdminGuard implements CanActivate {
  constructor(private keycloakService: KeycloakService, private router: Router) {}

  async canActivate(): Promise<boolean> {
    const roles = await this.keycloakService.getUserRoles();
    if (roles.includes('ROLE_ADMIN')) {
      return true;
    }
    this.router.navigate(['/']);
    return false;
  }
}
