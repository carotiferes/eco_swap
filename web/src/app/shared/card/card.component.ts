import { Component, Input } from '@angular/core';
import { Router } from '@angular/router';
import { CardModel } from 'src/app/models/card.model';
import { ColectaModel } from 'src/app/models/colecta.model';
import { DonacionModel } from 'src/app/models/donacion.model';
import { PublicacionModel } from 'src/app/models/publicacion.model';
import { TruequesService } from 'src/app/services/trueques.service';

@Component({
	selector: 'app-card',
	templateUrl: './card.component.html',
	styleUrls: ['./card.component.scss']
})
export class CardComponent {

	@Input() app: 'colectas' | 'donaciones' | 'publicaciones' = 'colectas'; 
	@Input() cardData?: CardModel; //ColectaModel | DonacionModel | PublicacionModel;

	isSelected: boolean = false;

	constructor(private truequesService: TruequesService, private router: Router) { }

	clicked(card: CardModel) {
		switch (card.action) {
			case 'select':
				this.isSelected = true;
				break;
			case 'access':
				const url = this.app == 'colectas' ? 'colecta/' : (this.app == 'publicaciones' ? 'publicacion/' : 'donacion/')
				this.router.navigateByUrl(url+card.id)
				break;
			default:
				break;
		}
	}

	getImagen(img: string) {
		return this.truequesService.getImagen(img)
	}
}
