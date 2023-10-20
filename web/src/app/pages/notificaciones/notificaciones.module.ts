import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NotificacionesRoutingModule } from './notificaciones-routing.module';
import { NotificacionesComponent } from './notificaciones.component';
import { HeaderModule } from 'src/app/layout/header/header.module';
import { MaterialModule } from 'src/app/shared/material/material.module';

@NgModule({
  declarations: [NotificacionesComponent],
  imports: [
    CommonModule,
	MaterialModule,
	NotificacionesRoutingModule,
	HeaderModule
  ]
})
export class NotificacionesModule { }
