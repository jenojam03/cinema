import { bootstrapApplication } from '@angular/platform-browser';
import { AppComponent } from './app.component';
import { provideRouter } from '@angular/router';
import { routes } from './app.routes';
import { AuthConfig, OAuthService, provideOAuthClient } from 'angular-oauth2-oidc';
import { APP_INITIALIZER, ApplicationConfig } from '@angular/core';
import { provideHttpClient } from '@angular/common/http';

export const authCodeFlowConfig: AuthConfig = {
  issuer: 'http://localhost:8080/realms/cinema-realm',
  redirectUri: window.location.origin,
  clientId: 'cinema-client',
  responseType: 'code',
  scope: 'openid profile email',
  showDebugInformation: true,
};

function initializeOAuth(oauthService: OAuthService): Promise<void> {
  return new Promise((resolve) => {
    oauthService.configure(authCodeFlowConfig);
    oauthService.setupAutomaticSilentRefresh();
    oauthService.loadDiscoveryDocumentAndLogin()
      .then(() => resolve());
  });
}

export const appConfig: ApplicationConfig = {
  providers: [
    provideRouter(routes),
    provideHttpClient(),
    provideOAuthClient(),
    {
      provide: APP_INITIALIZER,
      useFactory: (oauthService: OAuthService) => {
        return () => initializeOAuth(oauthService);
      },
      multi: true,
      deps: [OAuthService]
    }
  ]
};

bootstrapApplication(AppComponent, appConfig)
  .catch((err) => console.error(err));

