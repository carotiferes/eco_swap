import { AfterViewInit, Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { PublicacionModel } from 'src/app/models/publicacion.model';
import { TruequesService } from 'src/app/services/trueques.service';

@Component({
	selector: 'app-trocar-modal',
	templateUrl: './trocar-modal.component.html',
	styleUrls: ['./trocar-modal.component.scss']
})
export class TrocarModalComponent {

	publicacionOrigen: PublicacionModel;
	imagenPublicacion: string = '';

	loading: boolean = false;

	misPublicaciones: PublicacionModel[] = [];

	constructor(@Inject(MAT_DIALOG_DATA) public data: any, private truequeService: TruequesService){
		console.log(data);
		this.publicacionOrigen = data.publicacion;
		this.getMisPublicaciones()
	}

	getMisPublicaciones(){
		this.truequeService.getMisPublicaciones().subscribe({
			next: (res: any) => {
				console.log(res);
				this.misPublicaciones = res;
				this.misPublicaciones.map(item => {
					item.parsedImagenes = item.imagenes.split('|')
				})
			},
			error: (error) => {
				console.log(error);
				
			}
		})
	}

	getImage(image: any) {
		return this.truequeService.getImagen(image)
	}
}
