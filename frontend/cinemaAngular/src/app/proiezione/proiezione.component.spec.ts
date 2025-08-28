import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProiezioneComponent } from './proiezione.component';

describe('ProiezioneComponent', () => {
  let component: ProiezioneComponent;
  let fixture: ComponentFixture<ProiezioneComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProiezioneComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ProiezioneComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
