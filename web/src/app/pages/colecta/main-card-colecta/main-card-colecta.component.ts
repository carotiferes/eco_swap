import { Component, Input } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { ColectaModel } from 'src/app/models/colecta.model';
import { FundacionModel } from 'src/app/models/fundacion.model';
import { AuthService } from 'src/app/services/auth.service';
import { DonacionesService } from 'src/app/services/donaciones.service';
import { MapComponent } from 'src/app/shared/map/map.component';
import Swal from 'sweetalert2';

@Component({
	selector: 'app-main-card-colecta',
	templateUrl: './main-card-colecta.component.html',
	styleUrls: ['./main-card-colecta.component.scss']
})
export class MainCardColectaComponent {
	@Input() colecta?: ColectaModel;
	@Input() userData?: any;

	constructor(private auth: AuthService, private router: Router, private dialog: MatDialog,
		private donacionesService: DonacionesService) {}

	donar() {
		if(this.auth.isUserLoggedIn){
			Swal.fire({
				title: '¡Estás un paso más cerca de hacer tu donación!',
				text: 'Te vamos a pedir algunos datos de lo que vas a donar. Es importante que completes la información requerida para que la fundación conozca lo que vas a donar.', //'Seleccioná el producto que quieras donar',
				icon: 'info',
				confirmButtonText: '¡VAMOS!' //'Confirmar'
			}).then(({isConfirmed, value}) => {
				if(isConfirmed && this.colecta) this.router.navigate(['donacion/'+this.colecta.idColecta])
			})
		} else {
			Swal.fire({
				title: '¡Necesitás una cuenta!',
				text: 'Para poder donar, tenés que usar tu cuenta.',
				icon: 'warning',
				confirmButtonText: 'Iniciar sesión',
				showCancelButton: true,
				cancelButtonText: 'Cancelar'
			}).then(({isConfirmed}) => {
				if(isConfirmed) this.router.navigate(['login'])
			})
		}
	}

	showDireccion(fundacion: any){
		let stringDir: string = fundacion.direcciones[0].direccion + fundacion.direcciones[0].altura || '';
		const localidad = fundacion.direcciones[0].localidad || '';

		const apiUrl = `https://apis.datos.gob.ar/georef/api/direcciones?provincia=02&localidad=${localidad}
		&direccion=${encodeURIComponent(stringDir)}`
		fetch(apiUrl).then(response => response.json()).then(data => {
			if(data.cantidad > 0) {
				const lat = data.direcciones[0].ubicacion.lat;
		      	const lon = data.direcciones[0].ubicacion.lon;
				  this.dialog.open(MapComponent, {
					maxWidth: '70vw',
					maxHeight: '60vh',
					height: '100%',
					width: '100%',
					panelClass: 'full-screen-modal',
					data: {lat, lon}
				});
			}
		  }).catch(error => console.error(error));
	}

	showContactInfo(fundacion: FundacionModel) {
		Swal.fire({
			title: 'Información de la fundación: \n '+ fundacion.nombre,
			html: `
			<p style="font-weight: 400;"><b>Email: </b>${fundacion.usuarioDTO.email}</p>
			<p style="font-weight: 400;"><b>Teléfono: </b>${fundacion.usuarioDTO.telefono}</p>
			`,
			icon: 'info'
		})
	}

	getImage(image: any ){
		return this.donacionesService.getImagen(image)
	}

	zoomImage(img?: string){
		if(img){
			Swal.fire({
				html: `<img src="${this.getImage(img)}" style="width: 100%"/>`,
				showConfirmButton: false,
				showCloseButton: true
			})
		}
	}

	parseVigencia() {
		if(this.colecta) {
			if(this.colecta.fechaInicio && this.colecta.fechaFin) {
				return 'Desde el ' + (new Date(this.colecta.fechaInicio)).toLocaleDateString() + ' hasta el ' + (new Date(this.colecta.fechaFin)).toLocaleDateString()
			} else if (this.colecta.fechaInicio) {
				return 'A partir del ' + (new Date(this.colecta.fechaInicio)).toLocaleDateString();
			} else return '';
		} else return '';
	}
}
