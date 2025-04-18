import { TestBed } from '@angular/core/testing';

import { CommvaultService } from './commvault.service';
import {provideHttpClient} from "@angular/common/http";
import {provideHttpClientTesting} from "@angular/common/http/testing";

describe('CommvaultService', () => {
  let service: CommvaultService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideHttpClient(),
        provideHttpClientTesting(),
      ]
    });
    service = TestBed.inject(CommvaultService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
