import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BigliettoComponent } from './biglietto.component';

describe('BigliettoComponentComponent', () => {
  let component: BigliettoComponent;
  let fixture: ComponentFixture<BigliettoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BigliettoComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(BigliettoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
