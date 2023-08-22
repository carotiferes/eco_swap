import { TestBed } from '@angular/core/testing';

import { ParticularesService } from './particulares.service';

describe('ParticularesService', () => {
  let service: ParticularesService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ParticularesService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
