import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PublicacionComponent } from './publicacion.component';
import { PublicacionRoutingModule } from './publicacion-routing.module';
import { MaterialModule } from 'src/app/shared/material/material.module';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { HeaderModule } from 'src/app/layout/header/header.module';
import { SharedModule } from 'src/app/shared/shared.module';
import { MatCardModule } from '@angular/material/card';
import { TrocarModalComponent } from './trocar-modal/trocar-modal.component';
import { MainCardPublicacionComponent } from './main-card-publicacion/main-card-publicacion.component';

@NgModule({
  declarations: [PublicacionComponent, TrocarModalComponent, MainCardPublicacionComponent],
  imports: [
    CommonModule,
	PublicacionRoutingModule,
	MaterialModule,
	NgbModule,
	HeaderModule,
	SharedModule,
	MatCardModule
  ],
  exports: [PublicacionComponent, MainCardPublicacionComponent]
})
export class PublicacionModule { }
