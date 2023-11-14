import { CurrencyPipe } from '@angular/common';
import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { CardModel } from 'src/app/models/card.model';
import { PublicacionModel } from 'src/app/models/publicacion.model';
import { TruequeModel } from 'src/app/models/trueque.model';
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
	cardPublicacionOrigen: CardModel;
	screenWidth: number;

	constructor(@Inject(MAT_DIALOG_DATA) public data: any, private truequeService: TruequesService,
	public dialogRef: MatDialogRef<TrocarModalComponent>, private currencyPipe: CurrencyPipe){
		console.log(data);
		this.publicacionOrigen = data.publicacion;
		this.cardPublicacionOrigen = {
			id: this.publicacionOrigen.idPublicacion,
			imagen: this.publicacionOrigen.parsedImagenes? this.publicacionOrigen.parsedImagenes[0] : 'no_image',
			titulo: this.publicacionOrigen.titulo,
			valorPrincipal: `${this.currencyPipe.transform(this.publicacionOrigen.valorTruequeMin)} - ${this.currencyPipe.transform(this.publicacionOrigen.valorTruequeMax)}`,
			fecha: this.publicacionOrigen.fechaPublicacion,
			usuario: {
				id: this.publicacionOrigen.particularDTO.usuarioDTO.idUsuario,
				imagen: this.publicacionOrigen.particularDTO.usuarioDTO.avatar,
				nombre: this.publicacionOrigen.particularDTO.nombre + ' ' + this.publicacionOrigen.particularDTO.apellido,
				puntaje: this.publicacionOrigen.particularDTO.puntaje,
				localidad: this.publicacionOrigen.particularDTO.direcciones[0].localidad
			},
			action: 'detail',
			buttons: [],
			estado: this.publicacionOrigen.estadoTrueque,
		}
		this.getMisPublicaciones()
		this.screenWidth = (window.innerWidth > 0) ? window.innerWidth : screen.width;
	}

	getMisPublicaciones(){
		this.truequeService.getMisPublicacionesForTrueque(this.publicacionOrigen.idPublicacion).subscribe({
			next: (res: any) => {
				console.log(res);
				this.misPublicaciones = res;
				this.misPublicaciones.map(item => {
					item.parsedImagenes = item.imagenes.split('|')
				})
				this.misPublicaciones = this.misPublicaciones.filter(publicacion =>  // saco las publicaciones que ya tiene propuestas
					!this.data.trueques.some((trueque: TruequeModel) => trueque.publicacionDTOpropuesta.idPublicacion == publicacion.idPublicacion)
				)
			},
			error: (error) => {
				console.log(error);
				
			},
			complete: () => this.parsePublicaciones()
		})
	}

	parsePublicaciones() {
		this.publicacionesCardList.splice(0);
		const auxList: CardModel[] = [];
		
		this.misPublicaciones.sort((a, b) => new Date(b.fechaPublicacion).getTime() - new Date(a.fechaPublicacion).getTime());
		for (const publicacion of this.misPublicaciones) {
			const item: CardModel = {
				id: publicacion.idPublicacion,
				imagen: publicacion.parsedImagenes? publicacion.parsedImagenes[0] : 'no_image',
				titulo: publicacion.titulo,
				valorPrincipal: `${this.currencyPipe.transform(publicacion.valorTruequeMin)} - ${this.currencyPipe.transform(publicacion.valorTruequeMax)}`,
				fecha: publicacion.fechaPublicacion,
				usuario: {
					id: publicacion.particularDTO.usuarioDTO.idUsuario,
					imagen: publicacion.particularDTO.usuarioDTO.avatar,
					nombre: publicacion.particularDTO.nombre + ' ' + publicacion.particularDTO.apellido,
					puntaje: publicacion.particularDTO.puntaje,
					localidad: publicacion.particularDTO.direcciones[0].localidad
				},
				action: 'select',
				buttons: [],
				estado: publicacion.estadoTrueque,
			}
			auxList.push(item);
		}
		this.publicacionesCardList = auxList;
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
