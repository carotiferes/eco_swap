import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormColectaComponent } from './form-colecta.component';
import { FormColectaRoutingModule } from './form-colecta-routing.module';
import { MaterialModule } from 'src/app/shared/material/material.module';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { HeaderModule } from 'src/app/layout/header/header.module';
import { SharedModule } from 'src/app/shared/shared.module';

@NgModule({
  declarations: [FormColectaComponent],
  imports: [
    CommonModule,
	FormColectaRoutingModule,
	MaterialModule,
	NgbModule,
	HeaderModule,
	SharedModule
  ],
  exports: [FormColectaComponent]
})
export class FormColectaModule { }