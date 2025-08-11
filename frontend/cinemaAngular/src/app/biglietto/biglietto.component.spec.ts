import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Biglietto } from './biglietto.component';

describe('Biglietto', () => {
  let component: Biglietto;
  let fixture: ComponentFixture<Biglietto>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Biglietto]
    })
    .compileComponents();

    fixture = TestBed.createComponent(Biglietto);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
