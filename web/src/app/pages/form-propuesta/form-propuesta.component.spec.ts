import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FormPropuestaComponent } from './form-propuesta.component';

describe('FormPropuestaComponent', () => {
  let component: FormPropuestaComponent;
  let fixture: ComponentFixture<FormPropuestaComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [FormPropuestaComponent]
    });
    fixture = TestBed.createComponent(FormPropuestaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
