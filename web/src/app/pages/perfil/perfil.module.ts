import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PerfilComponent } from './perfil.component';
import { PerfilRoutingModule } from './perfil-routing.module';
import { MaterialModule } from 'src/app/shared/material/material.module';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { HeaderModule } from 'src/app/layout/header/header.module';
import { PhoneNumberPipe } from 'src/app/pipes/phone-number.pipe';
import { PipesModule } from 'src/app/pipes/pipes.module';
import { ImagesModalComponent } from './images-modal/images-modal.component';
import { CredencialesMpModalComponent } from './credenciales-mp-modal/credenciales-mp-modal.component';
import { ChangePasswordModalComponent } from './change-password-modal/change-password-modal.component';
import { SharedModule } from 'src/app/shared/shared.module';
import { OpinarModalComponent } from './opinar-modal/opinar-modal.component';

@NgModule({
  declarations: [PerfilComponent, ImagesModalComponent, CredencialesMpModalComponent, ChangePasswordModalComponent, OpinarModalComponent],
  imports: [
    CommonModule,
	PerfilRoutingModule,
	MaterialModule,
	NgbModule,
	HeaderModule,
	PipesModule,
	SharedModule
  ],
  exports: [PerfilComponent]
})
export class PerfilModule { }