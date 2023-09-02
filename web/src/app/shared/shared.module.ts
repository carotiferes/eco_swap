import { NgModule } from '@angular/core';

import { MapComponent } from './map/map.component';
import { SpinnerComponent } from './spinner/spinner.component';
import { MaterialModule } from './material/material.module';
import { CardDonacionComponent } from './card-donacion/card-donacion.component';
import { CommonModule } from '@angular/common';

@NgModule({
	declarations: [
		MapComponent,
		SpinnerComponent,
		CardDonacionComponent
	],
	imports: [
		MaterialModule,
		CommonModule
	],
	exports: [
		MapComponent,
		SpinnerComponent,
		CardDonacionComponent
	]
})
export class SharedModule { }
