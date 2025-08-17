import { Routes } from '@angular/router';

import { HomeComponent } from './home/home.component';

import { CatalogoFilmComponent } from './catalogofilm/catalogofilm.component';
import { FilmComponent } from './film/film.component';


import { ProiezioneComponent } from './proiezione/proiezione.component';
import { CatalogoProiezioniComponent } from './catalogoproiezioni/catalogoproiezioni.component';

import { BigliettoComponent } from './biglietto/biglietto.component';
import {AcquistoComponent} from './acquisto/acquisto.component';

import { ClienteComponent } from './cliente/cliente.component';

import { TransazioniComponent } from './transazioni/transazioni.component';

import { AuthGuard } from './guards/auth.guard';
import { AdminGuard } from './guards/admin.guard';

export const routes: Routes = [

  //Home page
  {path: '', component: HomeComponent},

  //Film
  {path: "film", component: CatalogoFilmComponent},
  {path: "film/:id", component: FilmComponent},
  {path: "film/:id/proiezioni", component: CatalogoProiezioniComponent},

  //Proiezioni
  {path: "proiezioni", component: CatalogoProiezioniComponent},
  {path: "proiezioni/:id", component: ProiezioneComponent},

  //Biglietti
  { path: 'biglietti/:id', component: BigliettoComponent},

  //Area personale cliente
  { path: 'clienti/me', component: ClienteComponent, canActivate: [AuthGuard]},

  //Transazioni
  { path: 'transazioni', component: TransazioniComponent, canActivate: [AdminGuard]},

  //Acquisti
  { path: 'acquista/:id', component: AcquistoComponent},

  //gestione pagina inesistente
  { path: '**', redirectTo: '' },
];
