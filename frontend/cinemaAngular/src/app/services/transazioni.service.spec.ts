import { TestBed } from '@angular/core/testing';

import { TransazioniService } from './transazioni.service';

describe('TransazioniService', () => {
  let service: TransazioniService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TransazioniService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
