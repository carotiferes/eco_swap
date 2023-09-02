import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PerfilComponent } from './perfil.component';
import { PerfilRoutingModule } from './perfil-routing.module';
import { MaterialModule } from 'src/app/shared/material/material.module';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { HeaderModule } from 'src/app/layout/header/header.module';
import { PhoneNumberPipe } from 'src/app/pipes/phone-number.pipe';
import { PipesModule } from 'src/app/pipes/pipes.module';

@NgModule({
  declarations: [PerfilComponent],
  imports: [
    CommonModule,
	PerfilRoutingModule,
	MaterialModule,
	NgbModule,
	HeaderModule,
	PipesModule
  ],
  exports: [PerfilComponent]
})
export class PerfilModule { }