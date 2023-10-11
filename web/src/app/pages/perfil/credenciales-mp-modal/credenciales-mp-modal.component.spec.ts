import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CredencialesMpModalComponent } from './credenciales-mp-modal.component';

describe('CredencialesMpModalComponent', () => {
  let component: CredencialesMpModalComponent;
  let fixture: ComponentFixture<CredencialesMpModalComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CredencialesMpModalComponent]
    });
    fixture = TestBed.createComponent(CredencialesMpModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
