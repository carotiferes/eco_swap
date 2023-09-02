import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ErrorComponent } from './error.component';
import { ErrorRoutingModule } from './error-routing.module';
import { MaterialModule } from 'src/app/shared/material/material.module';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { HeaderModule } from 'src/app/layout/header/header.module';
import { SharedModule } from 'src/app/shared/shared.module';

@NgModule({
  declarations: [ErrorComponent],
  imports: [
    CommonModule,
	ErrorRoutingModule,
	MaterialModule,
	NgbModule,
	HeaderModule,
	SharedModule
  ],
  exports: [ErrorComponent]
})
export class ErrorModule { }