import { NgModule } from '@angular/core';

import { MapComponent } from './map/map.component';
import { SpinnerComponent } from './spinner/spinner.component';
import { MaterialModule } from './material/material.module';
import { CardDonacionComponent } from './card-donacion/card-donacion.component';
import { CommonModule } from '@angular/common';
import { DeckPublicacionesComponent } from './deck-publicaciones/deck-publicaciones.component';

@NgModule({
	declarations: [
		MapComponent,
		SpinnerComponent,
		CardDonacionComponent,
  		DeckPublicacionesComponent
	],
	imports: [
		MaterialModule,
		CommonModule
	],
	exports: [
		MapComponent,
		SpinnerComponent,
		CardDonacionComponent,
		DeckPublicacionesComponent
	]
})
export class SharedModule { }
