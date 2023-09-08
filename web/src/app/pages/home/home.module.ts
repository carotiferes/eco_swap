import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HomeComponent } from './home.component';
import { HomeRoutingModule } from './home-routing.module';
import { MaterialModule } from 'src/app/shared/material/material.module';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { HeaderModule } from 'src/app/layout/header/header.module';

@NgModule({
  declarations: [HomeComponent],
  imports: [
    CommonModule,
	HomeRoutingModule,
	MaterialModule,
	NgbModule,
	HeaderModule
  ],
  exports: [HomeComponent]
})
export class HomeModule { }
