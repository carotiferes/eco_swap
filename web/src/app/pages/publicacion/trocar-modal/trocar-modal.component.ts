import { AfterViewInit, Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
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
				
			}
		})
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
}
