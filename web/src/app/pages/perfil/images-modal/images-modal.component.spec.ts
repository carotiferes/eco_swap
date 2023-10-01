import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ImagesModalComponent } from './images-modal.component';

describe('ImagesModalComponent', () => {
  let component: ImagesModalComponent;
  let fixture: ComponentFixture<ImagesModalComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ImagesModalComponent]
    });
    fixture = TestBed.createComponent(ImagesModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
