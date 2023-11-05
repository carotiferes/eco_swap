import { NgModule } from '@angular/core';
import { CommonModule, CurrencyPipe } from '@angular/common';
import { PublicacionesComponent } from './publicaciones.component';
import { PublicacionesRoutingModule } from './publicaciones-routing.module';
import { MaterialModule } from 'src/app/shared/material/material.module';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { HeaderModule } from 'src/app/layout/header/header.module';
import { SharedModule } from 'src/app/shared/shared.module';

@NgModule({
  declarations: [PublicacionesComponent],
  imports: [
    CommonModule,
	PublicacionesRoutingModule,
	MaterialModule,
	NgbModule,
	HeaderModule,
	SharedModule
  ],
  exports: [PublicacionesComponent],
  providers: [CurrencyPipe]
})
export class PublicacionesModule { }
