import { Component, Input } from '@angular/core';
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

	constructor(private truequesService: TruequesService, private router: Router) {}

	getImagen(img: string) {
		return this.truequesService.getImagen(img)
	}

	goToPublicacion(publicacion: PublicacionModel){
		this.router.navigate(['publicacion/'+publicacion.idPublicacion])
	}
}
