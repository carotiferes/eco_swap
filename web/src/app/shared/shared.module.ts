import { NgModule } from '@angular/core';

import { MapComponent } from './map/map.component';
import { SpinnerComponent } from './spinner/spinner.component';
import { MaterialModule } from './material/material.module';
import { CardDonacionComponent } from './card-donacion/card-donacion.component';
import { CommonModule } from '@angular/common';
import { DeckPublicacionesComponent } from './deck-publicaciones/deck-publicaciones.component';
import { CardComponent } from './card/card.component';
import { DeckComponent } from './deck/deck.component';
import { NgbRatingModule } from '@ng-bootstrap/ng-bootstrap';

@NgModule({
	declarations: [
		MapComponent,
		SpinnerComponent,
		CardDonacionComponent,
  		DeckPublicacionesComponent,
	    CardComponent,
	    DeckComponent
	],
	imports: [
		MaterialModule,
		CommonModule,
		NgbRatingModule
	],
	exports: [
		MapComponent,
		SpinnerComponent,
		CardDonacionComponent,
		DeckPublicacionesComponent,
		CardComponent,
    	DeckComponent
	]
})
export class SharedModule { }
