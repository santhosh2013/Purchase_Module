import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NegotiationFormComponent } from './negotiation-form.component';

describe('NegotiationFormComponent', () => {
  let component: NegotiationFormComponent;
  let fixture: ComponentFixture<NegotiationFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [NegotiationFormComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(NegotiationFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
