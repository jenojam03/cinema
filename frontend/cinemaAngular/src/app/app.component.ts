import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router, RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { OAuthService } from 'angular-oauth2-oidc';
import { Subscription } from 'rxjs';
import { filter } from 'rxjs/operators';
import { NgIf } from '@angular/common';


@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, RouterLink, RouterLinkActive, NgIf],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent implements OnInit, OnDestroy {
  title: string = 'Cinema PSW';
  private authEventsSubscription!: Subscription;

  constructor(
    private oauthService: OAuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.authEventsSubscription = this.oauthService.events
      .pipe(filter(e => e.type === 'token_received' || e.type === 'token_refreshed' || e.type === 'logout'))
      .subscribe(e => {
        console.log(`Authentication event: ${e.type}`);
      });
  }

  ngOnDestroy(): void {
    if (this.authEventsSubscription) {
      this.authEventsSubscription.unsubscribe();
    }
  }

  isLoggedIn(): boolean {
    return this.oauthService.hasValidAccessToken();
  }

}
