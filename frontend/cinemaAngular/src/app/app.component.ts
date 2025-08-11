import {Component, OnInit, signal} from '@angular/core';
import {Router, RouterLink, RouterLinkActive, RouterOutlet} from '@angular/router';
import {HttpClient} from '@angular/common/http';
import {filter} from 'rxjs';
import {OAuthService} from 'angular-oauth2-oidc';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, RouterLink, RouterLinkActive],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent implements OnInit{
  title: string = 'Cinema PSW';


  constructor(
    private oauthService: OAuthService,
    private httpClient: HttpClient,
    private router: Router) {}

  ngOnInit(): void {
    this.oauthService.events
      .pipe(filter(e => e.type === 'token_received'))
      .subscribe(() => {
        console.log('Token ricevuto, facendo partire la chiamata...');
        this.clienteInit();

      });

    if (this.oauthService.hasValidAccessToken()) {
      console.log('Token già presente, facendo partire la chiamata...');
      this.clienteInit();

    }
  }

  clienteInit() {
    const token = this.oauthService.getAccessToken();
    this.httpClient.get<{ message: string }>('http://localhost:9090/home', {
      headers: {
        'Authorization': `Bearer ${token}`
      }
    }).subscribe(response => {
      console.log(response);
    });
  }

  logout() {
    this.oauthService.logOut();
  }

  goToFilm() {
    this.router.navigate(['/film']);
  }

  goToMyBiglietti() {
    this.router.navigate(['/biglietti/miei_biglietti']);
  }

  goToProfile() {
    this.router.navigate(['/clienti/me']);
  }

  goToProiezioni() {
    this.router.navigate(['/proiezioni']);
  }

  goToHome() { }
}
