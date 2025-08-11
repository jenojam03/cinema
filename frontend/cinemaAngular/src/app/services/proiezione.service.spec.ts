import { TestBed } from '@angular/core/testing';

import { ProiezioneService } from './proiezione.service';

describe('ProiezioneService', () => {
  let service: ProiezioneService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ProiezioneService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
