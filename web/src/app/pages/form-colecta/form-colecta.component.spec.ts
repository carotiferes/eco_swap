import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FormColectaComponent } from './form-colecta.component';

describe('FormColectaComponent', () => {
  let component: FormColectaComponent;
  let fixture: ComponentFixture<FormColectaComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [FormColectaComponent]
    });
    fixture = TestBed.createComponent(FormColectaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
