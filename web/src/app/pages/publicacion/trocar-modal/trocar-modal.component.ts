import { AfterViewInit, Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { CardModel } from 'src/app/models/card.model';
import { PublicacionModel } from 'src/app/models/publicacion.model';
import { TruequesService } from 'src/app/services/trueques.service';
import Swal from 'sweetalert2';

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
	cardSelected?: number;
	publicacionesCardList: CardModel[] = [];

	constructor(@Inject(MAT_DIALOG_DATA) public data: any, private truequeService: TruequesService,
	public dialogRef: MatDialogRef<TrocarModalComponent>){
		console.log(data);
		this.publicacionOrigen = data.publicacion;
		this.getMisPublicaciones()
	}

	getMisPublicaciones(){
		this.truequeService.getMisPublicaciones().subscribe({
			next: (res: any) => {
				console.log(res);
				this.misPublicaciones = res;
				this.misPublicaciones = this.misPublicaciones.filter(item => {
					let intervalo = Math.min(item.valorTruequeMax, this.publicacionOrigen.valorTruequeMax) - Math.max(item.valorTruequeMin, this.publicacionOrigen.valorTruequeMin)
					return intervalo > 0;
				});
				this.misPublicaciones.map(item => {
					item.parsedImagenes = item.imagenes.split('|')
				})
			},
			error: (error) => {
				console.log(error);
				
			},
			complete: () => this.parsePublicaciones()
		})
	}

	parsePublicaciones() {
		this.publicacionesCardList.splice(0)
		for (const publicacion of this.misPublicaciones) {
			const item: CardModel = {
				id: publicacion.idPublicacion,
				imagen: publicacion.parsedImagenes? publicacion.parsedImagenes[0] : 'no_image',
				titulo: publicacion.titulo,
				valorPrincipal: `$${publicacion.valorTruequeMin} - $${publicacion.valorTruequeMax}`,
				fecha: publicacion.fechaPublicacion,
				usuario: {
					imagen: 'assets/perfiles/perfiles-17.jpg',//publicacion.particularDTO.
					nombre: publicacion.particularDTO.nombre + ' ' + publicacion.particularDTO.apellido,
					puntaje: publicacion.particularDTO.puntaje,
					localidad: publicacion.particularDTO.direcciones[0].localidad
				},
				action: 'select',
				buttons: [],
				estado: publicacion.estadoTrueque,
			}
			this.publicacionesCardList.push(item);
		}
	}

	getImage(image: any) {
		return this.truequeService.getImagen(image)
	}

	selectCard(cardId: any) {
		this.cardSelected = cardId;
	}

	solicitarTrueque() {
		if(this.cardSelected) {
			this.truequeService.crearTrueque(this.publicacionOrigen.idPublicacion, this.cardSelected).subscribe({
				next: (res: any) => {
					console.log(res);
					Swal.fire(
						'¡Trueque creado!',
						'Se solicitó el intercambio, solo falta que lo acepten!',
						'success'
					).then(({isConfirmed}) => {
						this.dialogRef.close(isConfirmed);

					})
				}
			})

		}
	}

	close() {
		this.dialogRef.close();
	}
}
