import { Component, OnInit } from '@angular/core';
import { OAuthService } from 'angular-oauth2-oidc';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-cliente',
  templateUrl: './cliente.component.html',
  styleUrls: ['./cliente.component.css'],
  standalone: true,
  imports: [CommonModule]
})
export class ClienteComponent implements OnInit {

  nome = '';
  cognome = '';
  email = '';

  constructor(private oauthService: OAuthService) {}

  ngOnInit(): void {
    const claims: any = this.oauthService.getIdentityClaims();
    if (claims) {
      this.nome = claims['given_name'] || '';
      this.cognome = claims['family_name'] || '';
      this.email = claims['email'] || '';
    }
  }
}
