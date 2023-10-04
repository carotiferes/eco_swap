import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ColectasComponent } from './colectas.component';
import { ColectasRoutingModule } from './colectas-routing.module';
import { MaterialModule } from 'src/app/shared/material/material.module';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { HeaderModule } from 'src/app/layout/header/header.module';
import { SharedModule } from 'src/app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

@NgModule({
  declarations: [ColectasComponent],
  imports: [
    CommonModule,
	ColectasRoutingModule,
	MaterialModule,
	NgbModule,
	HeaderModule,
	SharedModule,
	FormsModule,
	ReactiveFormsModule,
  ],
  exports: [ColectasComponent]
})
export class ColectasModule { }