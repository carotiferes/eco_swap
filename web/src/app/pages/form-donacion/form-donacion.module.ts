import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormDonacionComponent } from './form-donacion.component';
import { FormDonacionRoutingModule } from './form-donacion-routing.module';
import { MaterialModule } from 'src/app/shared/material/material.module';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { HeaderModule } from 'src/app/layout/header/header.module';
import { SharedModule } from 'src/app/shared/shared.module';

@NgModule({
  declarations: [FormDonacionComponent],
  imports: [
    CommonModule,
	FormDonacionRoutingModule,
	MaterialModule,
	NgbModule,
	HeaderModule,
	SharedModule
  ],
  exports: [FormDonacionComponent]
})
export class FormDonacionModule { }