import { Injectable } from '@angular/core';
import { KeycloakService } from 'keycloak-angular';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  constructor(private keycloakService: KeycloakService) {}

  login() {
    this.keycloakService.login();
  }

  logout() {
    this.keycloakService.logout(window.location.origin);
  }

  async isLoggedIn(): Promise<boolean> {
    return await this.keycloakService.isLoggedIn();
  }

  async getToken(): Promise<string> {
    return await this.keycloakService.getToken();
  }

  getUsername(): string | null {
    const profile = this.keycloakService.getKeycloakInstance().profile;
    return profile?.username ?? null;
  }

  getRoles(): string[] {
    return this.keycloakService.getUserRoles();
  }
}

