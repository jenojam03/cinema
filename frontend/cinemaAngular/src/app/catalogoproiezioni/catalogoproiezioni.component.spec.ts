import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CatalogoproiezioniComponent } from './catalogoproiezioni.component';

describe('CatalogoproiezioniComponent', () => {
  let component: CatalogoproiezioniComponent;
  let fixture: ComponentFixture<CatalogoproiezioniComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CatalogoproiezioniComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CatalogoproiezioniComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
