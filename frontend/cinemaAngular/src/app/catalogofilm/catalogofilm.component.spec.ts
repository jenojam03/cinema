import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CatalogoFilmComponent } from './catalogofilm.component';

describe('CatalogofilmComponent', () => {
  let component: CatalogoFilmComponent;
  let fixture: ComponentFixture<CatalogoFilmComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CatalogoFilmComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CatalogoFilmComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
