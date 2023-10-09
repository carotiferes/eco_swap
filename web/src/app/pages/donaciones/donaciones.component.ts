import { AfterViewInit, Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { CardModel } from 'src/app/models/card.model';
import { DonacionModel } from 'src/app/models/donacion.model';
import { AuthService } from 'src/app/services/auth.service';
import { DonacionesService } from 'src/app/services/donaciones.service';
import { ShowErrorService } from 'src/app/services/show-error.service';
import { MapComponent } from 'src/app/shared/map/map.component';
import Swal from 'sweetalert2';

@Component({
	selector: 'app-donaciones',
	templateUrl: './donaciones.component.html',
	styleUrls: ['./donaciones.component.scss']
})
export class DonacionesComponent {

	donaciones: DonacionModel[] = [];
	donacionesCardList: CardModel[] = [];

	loading: boolean = false;

	constructor(public dialog: MatDialog, private donacionesService: DonacionesService,
		private auth: AuthService, private showErrorService: ShowErrorService) {
		this.getDonaciones()
	}

	getDonaciones() {
		this.loading = true;
		this.donacionesService.getMisDonaciones().subscribe({
			next: (res: any) => {
				if(res){
					this.donaciones = res;
					this.donaciones.map((donacion: any) => {
						if(donacion.idDonacion) donacion['last_status'] = donacion.estado;
						if(donacion.imagenes) donacion.parsedImagenes = donacion.imagenes.split('|')
					})
				} else this.showErrorService.show('Error!', 'No se encontró la información de tus donaciones. Intentá nuevamente más tarde.')
			},
			error: (error) => {
				console.log(error);
				this.loading = false;
				//this.showErrorService.show('Error!', 'Ocurrió un error al traer la información de tus donaciones. Intentá nuevamente más tarde.')
			}, complete: () => this.generateCardList()
		})

	}

	generateCardList() {
		this.donacionesCardList.splice(0);
		for (const donacion of this.donaciones) {
			let stringCaracteristicas = '';
			for (const [i, caract] of donacion.caracteristicaDonacion.entries()) {
				if(i==0) stringCaracteristicas = caract.caracteristica
				else stringCaracteristicas += ' - '+caract.caracteristica
			}
			const item: CardModel = {
				id: donacion.idDonacion,
				imagen: donacion.parsedImagenes? donacion.parsedImagenes[0] : 'no_image',
				titulo: donacion.descripcion,
				valorPrincipal: `${donacion.cantidadDonacion} unidades de ${donacion.producto.descripcion}`,
				valorSecundario: stringCaracteristicas,
				fecha: (new Date(donacion.fechaDonacion)).toLocaleDateString(),
				usuario: {
					imagen: 'assets/perfiles/perfiles-17.jpg',//publicacion.particularDTO.
					nombre: donacion.particularDTO.nombre + ' ' + donacion.particularDTO.apellido,
					puntaje: donacion.particularDTO.puntaje,
					localidad: donacion.particularDTO.direcciones[0].localidad
				},
				action: 'detail',
				buttons: [{name: 'CANCELAR', icon: 'close', color: 'warn', status: 'CANCELADA'}],
				estado: donacion.estadoDonacion,
				//idAuxiliar: this.colecta.idColecta
			}
			this.donacionesCardList.push(item)
			this.loading = false;
		}
	}

	zoomImage(img: string) {
		Swal.fire({
			html: `<img src="${img}" style="width: 100%"/>`,
			showConfirmButton: false,
			showCloseButton: true
		})
	}

	openDialog() {
		this.dialog.open(MapComponent, {
			maxWidth: '70vw',
			maxHeight: '60vh',
			height: '100%',
			width: '100%',
			panelClass: 'full-screen-modal'
		});
	}

	getImage(image: any) {
		return this.donacionesService.getImagen(image)
	}

}
