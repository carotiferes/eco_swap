import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormPublicacionComponent } from './form-publicacion.component';
import { FormPublicacionRoutingModule } from './form-publicacion-routing.module';
import { MaterialModule } from 'src/app/shared/material/material.module';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { HeaderModule } from 'src/app/layout/header/header.module';
import { SharedModule } from 'src/app/shared/shared.module';

@NgModule({
  declarations: [FormPublicacionComponent],
  imports: [
    CommonModule,
	FormPublicacionRoutingModule,
	MaterialModule,
	NgbModule,
	HeaderModule,
	SharedModule
  ],
  exports: [FormPublicacionComponent]
})
export class FormPublicacionModule { }