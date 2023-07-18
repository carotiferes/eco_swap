import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ColectasComponent } from './colectas.component';

describe('ColectasComponent', () => {
  let component: ColectasComponent;
  let fixture: ComponentFixture<ColectasComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ColectasComponent]
    });
    fixture = TestBed.createComponent(ColectasComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
