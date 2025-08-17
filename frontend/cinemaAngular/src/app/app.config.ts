import { bootstrapApplication } from '@angular/platform-browser';
import { AppComponent } from './app.component';
import {provideRouter, Router} from '@angular/router';
import { routes } from './app.routes';
import { AuthConfig, OAuthService, provideOAuthClient } from 'angular-oauth2-oidc';
import { APP_INITIALIZER, ApplicationConfig } from '@angular/core';
import { provideHttpClient } from '@angular/common/http';

export const authCodeFlowConfig: AuthConfig = {
  issuer: 'http://localhost:8080/realms/myrealm',
  redirectUri: window.location.origin,
  clientId: 'cinema-client',
  responseType: 'code',
  scope: 'openid profile email offline_access',
  showDebugInformation: true,
  requireHttps: false
};

function initializeOAuth(oauthService: OAuthService, router: Router): Promise<void> {
  return new Promise((resolve) => {
    oauthService.configure(authCodeFlowConfig);
    oauthService.setupAutomaticSilentRefresh();

    oauthService.loadDiscoveryDocumentAndTryLogin().then(() => {
      if (oauthService.hasValidAccessToken()) {
        const returnUrl = sessionStorage.getItem('returnUrl');
        if (returnUrl) {
          console.log('Redirect a:', returnUrl);
          sessionStorage.removeItem('returnUrl');
          router.navigateByUrl(returnUrl);
        }
      }
      resolve();
    });
  });
}

export const appConfig: ApplicationConfig = {
  providers: [
    provideRouter(routes),
    provideHttpClient(),
    provideOAuthClient(),
    {
      provide: APP_INITIALIZER,
      useFactory: (oauthService: OAuthService, router: Router) => {
        return async () => {
          oauthService.configure(authCodeFlowConfig);
          oauthService.setupAutomaticSilentRefresh();

          await oauthService.loadDiscoveryDocumentAndTryLogin();

          if (oauthService.hasValidAccessToken()) {
            const returnUrl = sessionStorage.getItem('returnUrl');
            if (returnUrl) {
              sessionStorage.removeItem('returnUrl');
              router.navigateByUrl(returnUrl);
            }
          }
        };
      },
      multi: true,
      deps: [OAuthService,Router]
    }
  ]
};

bootstrapApplication(AppComponent, appConfig)
  .catch((err) => console.error(err));
