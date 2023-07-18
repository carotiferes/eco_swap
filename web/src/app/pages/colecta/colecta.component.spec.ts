import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ColectaComponent } from './colecta.component';

describe('ColectaComponent', () => {
  let component: ColectaComponent;
  let fixture: ComponentFixture<ColectaComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ColectaComponent]
    });
    fixture = TestBed.createComponent(ColectaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
