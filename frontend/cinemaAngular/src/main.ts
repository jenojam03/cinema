import { bootstrapApplication } from '@angular/platform-browser';
import {appConfig, authCodeFlowConfig} from './app/app.config';
import { AppComponent } from './app/app.component';
import {OAuthService} from 'angular-oauth2-oidc';


bootstrapApplication(AppComponent, appConfig)
  .catch((err) => console.error(err));
