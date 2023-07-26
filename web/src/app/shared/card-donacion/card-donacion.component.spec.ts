import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CardDonacionComponent } from './card-donacion.component';

describe('CardDonacionComponent', () => {
  let component: CardDonacionComponent;
  let fixture: ComponentFixture<CardDonacionComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CardDonacionComponent]
    });
    fixture = TestBed.createComponent(CardDonacionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
