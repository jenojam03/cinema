import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Proiezione } from './proiezione';

describe('Proiezione', () => {
  let component: Proiezione;
  let fixture: ComponentFixture<Proiezione>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Proiezione]
    })
    .compileComponents();

    fixture = TestBed.createComponent(Proiezione);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
