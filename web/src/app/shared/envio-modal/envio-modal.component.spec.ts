import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EnvioModalComponent } from './envio-modal.component';

describe('EnvioModalComponent', () => {
  let component: EnvioModalComponent;
  let fixture: ComponentFixture<EnvioModalComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [EnvioModalComponent]
    });
    fixture = TestBed.createComponent(EnvioModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
