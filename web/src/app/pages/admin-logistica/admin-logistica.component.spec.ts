import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminLogisticaComponent } from './admin-logistica.component';

describe('AdminLogisticaComponent', () => {
  let component: AdminLogisticaComponent;
  let fixture: ComponentFixture<AdminLogisticaComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AdminLogisticaComponent]
    });
    fixture = TestBed.createComponent(AdminLogisticaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
