import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { PublicacionModel } from 'src/app/models/publicacion.model';
import { AuthService } from 'src/app/services/auth.service';
import { ShowErrorService } from 'src/app/services/show-error.service';
import { TruequesService } from 'src/app/services/trueques.service';
import { UsuarioService } from 'src/app/services/usuario.service';
import Swal from 'sweetalert2';

@Component({
	selector: 'app-publicacion',
	templateUrl: './publicacion.component.html',
	styleUrls: ['./publicacion.component.scss']
})
export class PublicacionComponent {

	loading: boolean = false;
	publicacion!: PublicacionModel;
	id_publicacion: any;

	userInfo: any;

	showButtons: boolean = true;

	constructor(private truequeService: TruequesService, private route: ActivatedRoute,
		private showErrorService: ShowErrorService, private auth: AuthService,
		private router: Router, private usuarioService: UsuarioService){
		route.paramMap.subscribe(params => {
			console.log(params);
			this.id_publicacion = params.get('id_publicacion') || 0;
			if(!this.id_publicacion) showErrorService.show('Error!', 'No pudimos encontrar la información de la colecta que seleccionaste, por favor volvé a intentarlo más tarde.')
			else this.getPublicacion(this.id_publicacion)
		})
		usuarioService.getUserByID(auth.getUserID()).subscribe({
			next: (res: any) => {
				this.userInfo = res;
				//console.log(this.userInfo);
				if(this.publicacion.particularDTO.idParticular == this.userInfo.particularDTO.idParticular)
					this.showButtons = false;
			}
		})
	}

	getPublicacion(id: number){
		this.truequeService.getPublicacion(id).subscribe({
			next: (res: any) => {
				//console.log(res);
				this.publicacion = res;
				this.publicacion.parsedImagenes = this.publicacion.imagenes.split('|')
				
			},
			error: (error) => {
				console.log(error);
				this.showErrorService.show('Error!', 'No pudimos encontrar la información de la colecta que seleccionaste, por favor volvé a intentarlo más tarde.')
			}
		})
	}

	intercambiar() {
		if(this.auth.isUserLoggedIn) {
			
		} else {
			Swal.fire({
				title: '¡Necesitás una cuenta!',
				text: 'Para poder intercambiar, tenés que usar tu cuenta.',
				icon: 'warning',
				confirmButtonText: 'Iniciar sesión',
				showCancelButton: true,
				cancelButtonText: 'Cancelar'
			}).then(({isConfirmed}) => {
				if(isConfirmed) this.router.navigate(['login'])
			})
		}
	}

	comprar() {
		if(this.auth.isUserLoggedIn) {
			
		} else {
			Swal.fire({
				title: '¡Necesitás una cuenta!',
				text: 'Para poder comprar, tenés que usar tu cuenta.',
				icon: 'warning',
				confirmButtonText: 'Iniciar sesión',
				showCancelButton: true,
				cancelButtonText: 'Cancelar'
			}).then(({isConfirmed}) => {
				if(isConfirmed) this.router.navigate(['login'])
			})
		}
	}

	getImage(image: any) {
		return this.truequeService.getImagen(image)
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
}
