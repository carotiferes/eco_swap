import { TestBed } from '@angular/core/testing';

import { TruequesService } from './trueques.service';

describe('TruequesService', () => {
  let service: TruequesService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TruequesService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
