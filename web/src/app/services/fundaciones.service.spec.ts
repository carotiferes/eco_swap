import { TestBed } from '@angular/core/testing';

import { FundacionesService } from './fundaciones.service';

describe('FundacionesService', () => {
  let service: FundacionesService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FundacionesService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
