import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MainCardPublicacionComponent } from './main-card-publicacion.component';

describe('MainCardPublicacionComponent', () => {
  let component: MainCardPublicacionComponent;
  let fixture: ComponentFixture<MainCardPublicacionComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [MainCardPublicacionComponent]
    });
    fixture = TestBed.createComponent(MainCardPublicacionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
