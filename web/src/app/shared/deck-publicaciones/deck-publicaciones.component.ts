import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Router } from '@angular/router';
import { PublicacionModel } from 'src/app/models/publicacion.model';
import { TruequesService } from 'src/app/services/trueques.service';

@Component({
	selector: 'app-deck-publicaciones',
	templateUrl: './deck-publicaciones.component.html',
	styleUrls: ['./deck-publicaciones.component.scss']
})
export class DeckPublicacionesComponent {

	@Input() publicacionesToShow: PublicacionModel[] = [];
	@Input() origin: 'publicaciones' | 'intercambio' | 'propuestas' | 'mis-publicaciones' = 'publicaciones';
	@Input() buttons: {name: string, icon: string, color: string, status: string}[] = [];
	cardSelected?: number;

	@Output() selectedCard = new EventEmitter<any>();
	@Output() changeStatus = new EventEmitter<any>();

	constructor(private truequesService: TruequesService, private router: Router) {
		console.log(this.publicacionesToShow, this.buttons);
		
	}

	getImagen(img: string) {
		return this.truequesService.getImagen(img)
	}

	clicked(publicacion: PublicacionModel) {
		if(this.origin == 'publicaciones' || this.origin == 'mis-publicaciones') this.goToPublicacion(publicacion);
		else {
			this.cardSelected = publicacion.idPublicacion;
			this.selectedCard.emit(this.cardSelected);
		}
	}

	goToPublicacion(publicacion: PublicacionModel){
		this.router.navigate(['publicacion/'+publicacion.idPublicacion])
	}

	changeEstadoPublicacion(publicacion: PublicacionModel, newStatus: string) {
		this.changeStatus.emit({publicacion, newStatus});
	}
}
