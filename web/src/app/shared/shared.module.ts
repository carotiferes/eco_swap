import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { MapComponent } from './map/map.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { SpinnerComponent } from './spinner/spinner.component';
import { MaterialModule } from './material/material.module';
import { CardDonacionComponent } from './card-donacion/card-donacion.component';

@NgModule({
	declarations: [
		MapComponent,
		SpinnerComponent,
		CardDonacionComponent
	],
	imports: [
		BrowserModule,
		BrowserAnimationsModule,
		MaterialModule
	],
	exports: [
		MapComponent,
		SpinnerComponent,
		CardDonacionComponent
	]
})
export class SharedModule { }
