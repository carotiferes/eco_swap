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
import { CredencialesMpModalComponent } from './credenciales-mp-modal/credenciales-mp-modal.component';
import { ChangePasswordModalComponent } from './change-password-modal/change-password-modal.component';
import { OpinarModalComponent } from './opinar-modal/opinar-modal.component';
import { OpinionesService } from 'src/app/services/opiniones.service';

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
		accessToken?: string,
		publicKey?: string
	}

	pageSize = 5;

	opiniones: OpinionModel[] = [];
	paginatedOpiniones: OpinionModel[] = [];

	refreshHeader: number = 0;

	constructor(private auth: AuthService, private usuarioService: UsuarioService,
		public router: Router, private particularService: ParticularesService,
		public dialog: MatDialog, private opinionesService: OpinionesService) {
		const url = router.url;
		console.log(url);
		const id_user = url.split('/')[2]
		if (url != '/mi-perfil' && id_user != this.auth.getUserID()) { // PERFIL DE OTRO USUARIO
			this.myProfile = false;
			this.getUserInformation(id_user);
			this.getOpiniones(id_user)
		} else { // MI PERFIL
			this.userData = { isSwapper: auth.isUserSwapper(), id: this.auth.getUserID() }
			this.getUserInformation(this.userData.id);
			this.getMyOpiniones()
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
			direccion: this.user.fundacionDTO.direcciones[0]
		}
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
					accessToken: particular.accessToken,
					publicKey: particular.publicKey
				}
			}
		})
	}

	getOpiniones(id_user: string) {
		this.opinionesService.getOpinionesByUserID(Number(id_user)).subscribe({
			next: (res: any) => {
				this.opiniones = res;
				this.paginatedOpiniones = this.opiniones.slice(0, this.pageSize);
			},
			error: (error: any) => console.log(error)
		})
	}

	getMyOpiniones() {
		this.opinionesService.getMyOpiniones().subscribe({
			next: (res: any) => {
				console.log(res);
				
				this.opiniones = res;
				this.paginatedOpiniones = this.opiniones.slice(0, this.pageSize);
			},
			error: (error: any) => console.log(error)
		})
	}

	changePage(event: any) {
		const startIndex = event.pageIndex * event.pageSize;
		const endIndex = startIndex + event.pageSize;
		this.paginatedOpiniones = this.opiniones.slice(startIndex, endIndex);
	}

	showMap(direccion: DireccionModel) {
		console.log(direccion);
		let stringDir: string = direccion.calle + direccion.altura || '';
		const localidad = (direccion.localidad || '').replace(' ', '');
		console.log(stringDir, localidad);


		const apiUrl = `https://apis.datos.gob.ar/georef/api/direcciones?provincia=02&localidad=${encodeURIComponent(localidad)}&direccion=${encodeURIComponent(stringDir)}`.replace(' ', '');
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
			maxHeight: '75vh',
			height: '100%',
			width: '100%',
			panelClass: 'full-screen-modal'
		});
		dialogRef.afterClosed().subscribe((result) => {
			console.log('closed', result);
			if(result){
				this.getUserInformation(this.userData.id);
				this.getMyOpiniones()
				this.refreshHeader++;
			}
		})
	}

	credencialesMP() {
		const dialogRef = this.dialog.open(CredencialesMpModalComponent, {
			maxWidth: '60vw',
			maxHeight: '60vh',
			height: '100%',
			width: '100%',
			panelClass: 'full-screen-modal',
			data: {user: this.user, publicKey: this.userToShow?.publicKey, accessToken: this.userToShow?.accessToken}
		});
		dialogRef.afterClosed().subscribe((result) => {
			console.log('closed', result);
			
		})
	}

	changePassword() {
		const dialogRef = this.dialog.open(ChangePasswordModalComponent, {
			maxWidth: '50vw',
			maxHeight: '60vh',
			height: '100%',
			width: '100%',
			panelClass: 'full-screen-modal',
		});
		dialogRef.afterClosed().subscribe((result) => {
			console.log('closed', result);
			
		})
	}

	opinar() {
		const dialogRef = this.dialog.open(OpinarModalComponent, {
			maxWidth: '50vw',
			maxHeight: '60vh',
			height: '100%',
			width: '100%',
			panelClass: 'full-screen-modal',
			data: {usuario: this.user, user: this.userToShow}
		});
		dialogRef.afterClosed().subscribe((result) => {
			console.log('closed', result);
			if(result) {
				const url = this.router.url;
				const id_user = url.split('/')[2]
				if (url != '/mi-perfil' && id_user != this.auth.getUserID()) { // PERFIL DE OTRO USUARIO
					const id_user = url.split('/')[2]
					this.getUserInformation(id_user);		
					this.getOpiniones(id_user)
				} else { // MI PERFIL --> nunca deberia ser este caso porq no puedo opinar sobre mí mismo
					this.getMyOpiniones()
				}
			}
		})
	}
}
