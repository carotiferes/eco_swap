import { TestBed } from '@angular/core/testing';

import { ShowErrorService } from './show-error.service';

describe('ShowErrorService', () => {
  let service: ShowErrorService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ShowErrorService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
