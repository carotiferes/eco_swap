import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ColectaComponent } from './colecta.component';
import { ColectaRoutingModule } from './colecta-routing.module';
import { MaterialModule } from 'src/app/shared/material/material.module';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { HeaderModule } from 'src/app/layout/header/header.module';
import { SharedModule } from 'src/app/shared/shared.module';

@NgModule({
  declarations: [ColectaComponent],
  imports: [
    CommonModule,
	ColectaRoutingModule,
	MaterialModule,
	NgbModule,
	HeaderModule,
	SharedModule
  ],
  exports: [ColectaComponent]
})
export class ColectaModule { }