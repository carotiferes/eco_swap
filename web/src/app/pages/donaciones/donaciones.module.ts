import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DonacionesComponent } from './donaciones.component';
import { DonacionesRoutingModule } from './donaciones-routing.module';
import { MaterialModule } from 'src/app/shared/material/material.module';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { HeaderModule } from 'src/app/layout/header/header.module';
import { SharedModule } from 'src/app/shared/shared.module';

@NgModule({
  declarations: [DonacionesComponent],
  imports: [
    CommonModule,
	DonacionesRoutingModule,
	MaterialModule,
	NgbModule,
	HeaderModule,
	SharedModule
  ],
  exports: [DonacionesComponent]
})
export class DonacionesModule { }