import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MainCardColectaComponent } from './main-card-colecta.component';

describe('MainCardColectaComponent', () => {
  let component: MainCardColectaComponent;
  let fixture: ComponentFixture<MainCardColectaComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [MainCardColectaComponent]
    });
    fixture = TestBed.createComponent(MainCardColectaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
