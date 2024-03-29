import { NgModule } from '@angular/core';

import { CommonModule, DatePipe } from '@angular/common';
import { NgbRatingModule } from '@ng-bootstrap/ng-bootstrap';
import { CardComponent } from './card/card.component';
import { DeckComponent } from './deck/deck.component';
import { MapComponent } from './map/map.component';
import { MaterialModule } from './material/material.module';
import { SpinnerComponent } from './spinner/spinner.component';
import { RatingComponent } from './rating/rating.component';
import { ListComponent } from './list/list.component';
import { EnvioModalComponent } from './envio-modal/envio-modal.component';
import { ChatComponent } from './chat/chat.component';

@NgModule({
	declarations: [
		MapComponent,
		SpinnerComponent,
		CardComponent,
		DeckComponent,
		RatingComponent,
		ListComponent,
		EnvioModalComponent,
		ChatComponent
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
		ListComponent,
		EnvioModalComponent,
		ChatComponent
	],
	providers: [DatePipe]
})
export class SharedModule { }
