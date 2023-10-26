import { TestBed } from '@angular/core/testing';

import { LogisticaService } from './logistica.service';

describe('LogisticaService', () => {
  let service: LogisticaService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(LogisticaService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
