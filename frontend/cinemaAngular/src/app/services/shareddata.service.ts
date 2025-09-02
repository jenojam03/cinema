import { Injectable } from '@angular/core';
import { Posto } from './proiezione.service';

@Injectable({
  providedIn: 'root'
})
export class SharedDataService {

  //per conservare i posti selezionati quando vengo rediretto dopo il login
  private postiSelezionati: Posto[] = [];

  constructor() { }

  setPostiSelezionati(posti: Posto[]): void {
    this.postiSelezionati = posti;
  }

  getPostiSelezionati(): Posto[] {
    return this.postiSelezionati;
  }
}
