import { NgModule } from '@angular/core';

import { CommonModule } from '@angular/common';
import { NgbRatingModule } from '@ng-bootstrap/ng-bootstrap';
import { CardComponent } from './card/card.component';
import { DeckComponent } from './deck/deck.component';
import { MapComponent } from './map/map.component';
import { MaterialModule } from './material/material.module';
import { SpinnerComponent } from './spinner/spinner.component';
import { RatingComponent } from './rating/rating.component';
import { ListComponent } from './list/list.component';

@NgModule({
	declarations: [
		MapComponent,
		SpinnerComponent,
	    CardComponent,
	    DeckComponent,
     RatingComponent,
     ListComponent
	],
	imports: [
		MaterialModule,
		CommonModule,
		NgbRatingModule
	],
	exports: [
		MapComponent,
		SpinnerComponent,
		CardComponent,
    	DeckComponent,
		RatingComponent,
		ListComponent
	]
})
export class SharedModule { }
