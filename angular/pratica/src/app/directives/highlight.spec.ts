import { Highlight } from './highlight';
import { ElementRef } from '@angular/core';

describe('Highlight', () => {
  it('should create an instance', () => {
    const elementRefMock = { nativeElement: document.createElement('div') };
    const directive = new Highlight(elementRefMock as ElementRef);
    expect(directive).toBeTruthy();
  });
});
