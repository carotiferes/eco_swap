import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RegistroComponent } from './registro.component';
import { RegistroRoutingModule } from './registro-routing.module';
import { MaterialModule } from 'src/app/shared/material/material.module';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { HeaderModule } from 'src/app/layout/header/header.module';

@NgModule({
  declarations: [RegistroComponent],
  imports: [
    CommonModule,
	RegistroRoutingModule,
	MaterialModule,
	NgbModule,
	HeaderModule
  ],
  exports: [RegistroComponent]
})
export class RegistroModule { }