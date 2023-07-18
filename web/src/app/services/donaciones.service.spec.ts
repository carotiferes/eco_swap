import { TestBed } from '@angular/core/testing';

import { DonacionesService } from './donaciones.service';

describe('DonacionesService', () => {
  let service: DonacionesService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DonacionesService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
