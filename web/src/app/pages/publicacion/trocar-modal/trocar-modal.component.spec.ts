import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TrocarModalComponent } from './trocar-modal.component';

describe('TrocarModalComponent', () => {
  let component: TrocarModalComponent;
  let fixture: ComponentFixture<TrocarModalComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TrocarModalComponent]
    });
    fixture = TestBed.createComponent(TrocarModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
