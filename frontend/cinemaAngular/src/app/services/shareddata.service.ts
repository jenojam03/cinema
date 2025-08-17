import { Injectable } from '@angular/core';
import { Posto } from './proiezione.service';

@Injectable({
  providedIn: 'root'
})
export class SharedDataService {

  private postiSelezionati: Posto[] = [];

  constructor() { }

  setPostiSelezionati(posti: Posto[]): void {
    this.postiSelezionati = posti;
  }

  getPostiSelezionati(): Posto[] {
    return this.postiSelezionati;
  }
}
