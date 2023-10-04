import { Component } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { DireccionModel } from 'src/app/models/direccion.model';
import { OpinionModel } from 'src/app/models/opinion.model';
import { ParticularModel } from 'src/app/models/particular.model';
import { UsuarioModel } from 'src/app/models/usuario.model';
import { AuthService } from 'src/app/services/auth.service';
import { ParticularesService } from 'src/app/services/particulares.service';
import { UsuarioService } from 'src/app/services/usuario.service';
import { MapComponent } from 'src/app/shared/map/map.component';
import Swal from 'sweetalert2';
import { ImagesModalComponent } from './images-modal/images-modal.component';

@Component({
	selector: 'app-perfil',
	templateUrl: './perfil.component.html',
	styleUrls: ['./perfil.component.scss']
})
export class PerfilComponent {
	//isSwapper: boolean = true; // CHANGE WHEN BRINGING USER INFO
	user!: UsuarioModel;

	colspan: number = 1;

	userData: any = {};
	myProfile: boolean = true;

	userToShow?: {
		nombre: string,
		email: string,
		telefono: string,
		puntaje: number,
		direccion?: DireccionModel,
		opiniones?: OpinionModel[],
		paginatedOpiniones?: OpinionModel[]
	}

	pageSize = 3;

	constructor(private auth: AuthService, private usuarioService: UsuarioService,
		public router: Router, private particularService: ParticularesService,
		public dialog: MatDialog) {
		const url = router.url;
		console.log(url);
		if (url != '/mi-perfil') { // PERFIL DE OTRO USUARIO
			this.myProfile = false;
			const id_user = url.split('/')[2]
			this.getUserInformation(id_user);
		} else { // MI PERFIL
			this.userData = { isSwapper: auth.isUserSwapper(), id: this.auth.getUserID() }
			this.getUserInformation(this.userData.id);
		}
	}

	getUserInformation(id_user: string) {
		this.usuarioService.getUserByID(Number(id_user)).subscribe({
			next: (res: any) => {
				console.log(res);
				this.user = res;
				if (this.user.fundacionDTO) {
					this.configureFundacion(this.user);
				} else {
					this.configureSwapper(this.user);
				}
			},
			error: (error) => { console.log('error', error); }
		})
	}

	configureFundacion(user: UsuarioModel) {
		this.userData.isSwapper = false;
		console.log(this.user);
		
		this.user.fundacionDTO.direcciones[0].localidad = this.user.fundacionDTO.direcciones[0].localidad.toLowerCase();
		this.user.fundacionDTO.direcciones[0].calle = this.user.fundacionDTO.direcciones[0].calle.toLowerCase();
		this.userToShow = {
			nombre: this.user.fundacionDTO.nombre,
			email: this.user.email,
			telefono: this.user.telefono,
			puntaje: this.user.puntaje,
			direccion: this.user.fundacionDTO.direcciones[0],
			opiniones: [{
				idOpinion: 1,
				descripcion: 'Responsable',
				valoracion: 4,
				usuarioOpina: 103,
				fechaOpinion: new Date('2023-03-24')
			}]
		}
		this.userToShow?.paginatedOpiniones?.slice(0, this.pageSize);
	}

	configureSwapper(usuario: UsuarioModel) {
		this.userData.isSwapper = true;
		this.particularService.getParticular(this.user.particularDTO.idParticular).subscribe({
			next: (particular: any) => {
				particular.direcciones[0].localidad = particular.direcciones[0].localidad.toLowerCase();
				particular.direcciones[0].calle = particular.direcciones[0].calle.toLowerCase();
				this.userToShow = {
					nombre: particular.nombre + ' ' + particular.apellido,
					email: particular.usuarioDTO.email,
					telefono: particular.usuarioDTO.telefono,
					puntaje: particular.usuarioDTO.puntaje,
					direccion: particular.direcciones[0],
					opiniones: [{
						idOpinion: 1,
						descripcion: 'Responsable',
						valoracion: 4,
						usuarioOpina: 103,
						fechaOpinion: new Date('2023-03-24')
					}]
				}
				this.userToShow?.paginatedOpiniones?.slice(0, this.pageSize);
			}
		})
	}

	changePage(event: any) {
		const startIndex = event.pageIndex * event.pageSize;
		const endIndex = startIndex + event.pageSize;
		this.userToShow!.paginatedOpiniones = this.userToShow?.paginatedOpiniones?.slice(startIndex, endIndex);
	}

	showMap(direccion: DireccionModel) {
		console.log(direccion);
		let stringDir: string = direccion.calle + direccion.altura || '';
		const localidad = (direccion.localidad || '').replace(' ', '');
		console.log(stringDir, localidad);


		const apiUrl = `https://apis.datos.gob.ar/georef/api/direcciones?provincia=02&localidad=${localidad}&direccion=${encodeURIComponent(stringDir)}`.replace(' ', '');
		console.log(apiUrl);

		fetch(apiUrl).then(response => response.json()).then(data => {
			console.log(data);
			if (data.cantidad > 0) {
				const lat = data.direcciones[0].ubicacion.lat;
				const lon = data.direcciones[0].ubicacion.lon;
				this.dialog.open(MapComponent, {
					maxWidth: '70vw',
					maxHeight: '60vh',
					height: '100%',
					width: '100%',
					panelClass: 'full-screen-modal',
					data: { lat, lon }
				});
			}
		}).catch(error => console.error(error));
	}

	edit() {
		this.router.navigate(['edit-perfil'])
	}

	selectImage() {
		const dialogRef = this.dialog.open(ImagesModalComponent, {
			maxWidth: '80vw',
			maxHeight: '60vh',
			height: '100%',
			width: '100%',
			panelClass: 'full-screen-modal'
		});
		dialogRef.afterClosed().subscribe((result) => {
			console.log('closed', result);
			
		})
	}
}
