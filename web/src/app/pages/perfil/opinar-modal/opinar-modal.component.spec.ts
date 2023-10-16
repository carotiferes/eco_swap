import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OpinarModalComponent } from './opinar-modal.component';

describe('OpinarModalComponent', () => {
  let component: OpinarModalComponent;
  let fixture: ComponentFixture<OpinarModalComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [OpinarModalComponent]
    });
    fixture = TestBed.createComponent(OpinarModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
