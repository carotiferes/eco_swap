import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NotFoundComponent } from './not-found.component';
import { NotFoundRoutingModule } from './not-found-routing.module';
import { MaterialModule } from 'src/app/shared/material/material.module';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { HeaderModule } from 'src/app/layout/header/header.module';

@NgModule({
  declarations: [NotFoundComponent],
  imports: [
    CommonModule,
	NotFoundRoutingModule,
	MaterialModule,
	NgbModule,
	HeaderModule
  ],
  exports: [NotFoundComponent]
})
export class NotFoundModule { }