import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DeckPublicacionesComponent } from './deck-publicaciones.component';

describe('DeckPublicacionesComponent', () => {
  let component: DeckPublicacionesComponent;
  let fixture: ComponentFixture<DeckPublicacionesComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DeckPublicacionesComponent]
    });
    fixture = TestBed.createComponent(DeckPublicacionesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
