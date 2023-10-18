import { TestBed } from '@angular/core/testing';

import { OpinionesService } from './opiniones.service';

describe('OpinionesService', () => {
  let service: OpinionesService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(OpinionesService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
