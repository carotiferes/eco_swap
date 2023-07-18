import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FormDonacionComponent } from './form-donacion.component';

describe('FormDonacionComponent', () => {
  let component: FormDonacionComponent;
  let fixture: ComponentFixture<FormDonacionComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [FormDonacionComponent]
    });
    fixture = TestBed.createComponent(FormDonacionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
