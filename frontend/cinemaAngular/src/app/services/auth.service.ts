import { Injectable } from '@angular/core';
import { OAuthService } from 'angular-oauth2-oidc';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private oauthService: OAuthService) {}

  isAdmin(): boolean {
    const token = this.oauthService.getAccessToken();
    if (!token) {
      return false;
    }

    const payload = this.decodeJwtPayload(token);
    return payload?.realm_access?.roles?.includes('admin') ?? false;
  }

  isCliente(): boolean {
    const token = this.oauthService.getAccessToken();
    if (!token) {
      return false;
    }

    const payload = this.decodeJwtPayload(token);
    return payload?.realm_access?.roles?.includes('cliente') ?? false;
  }

  private decodeJwtPayload(token: string): any {
    try {
      const base64Payload = token.split('.')[1];
      const payload = atob(base64Payload.replace(/-/g, '+').replace(/_/g, '/'));
      return JSON.parse(payload);
    } catch (e) {
      console.error('Errore nella decodifica del token', e);
      return null;
    }
  }

  get isLoggedIn(): boolean {
    return this.oauthService.hasValidAccessToken();
  }

  get userRoles(): string[] {
    const token = this.oauthService.getAccessToken();
    if (!token) {
      return [];
    }
    const payload = this.decodeJwtPayload(token);
    return payload?.realm_access?.roles || [];
  }
}
