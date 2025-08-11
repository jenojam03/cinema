import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CatalogofilmComponent } from './catalogofilm.component';

describe('CatalogofilmComponent', () => {
  let component: CatalogofilmComponent;
  let fixture: ComponentFixture<CatalogofilmComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CatalogofilmComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CatalogofilmComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
