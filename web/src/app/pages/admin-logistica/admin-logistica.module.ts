import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AdminLogisticaComponent } from './admin-logistica.component';
import { HeaderModule } from 'src/app/layout/header/header.module';
import { AdminLogisticaRoutingModule } from './admin-logistica-routing.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MaterialModule } from 'src/app/shared/material/material.module';



@NgModule({
  declarations: [
    AdminLogisticaComponent
  ],
  imports: [
    CommonModule,
	AdminLogisticaRoutingModule,
	HeaderModule,
	FormsModule,
	ReactiveFormsModule,
	MaterialModule
  ]
})
export class AdminLogisticaModule { }
