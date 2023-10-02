import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { PublicacionModel } from 'src/app/models/publicacion.model';
import { AuthService } from 'src/app/services/auth.service';
import { ShowErrorService } from 'src/app/services/show-error.service';
import { TruequesService } from 'src/app/services/trueques.service';
import { UsuarioService } from 'src/app/services/usuario.service';
import Swal from 'sweetalert2';
import { TrocarModalComponent } from './trocar-modal/trocar-modal.component';
import { TruequeModel } from 'src/app/models/trueque.model';

@Component({
	selector: 'app-publicacion',
	templateUrl: './publicacion.component.html',
	styleUrls: ['./publicacion.component.scss']
})
export class PublicacionComponent {

	loading: boolean = false;
	publicacion!: PublicacionModel;
	id_publicacion: any;

	userData: any;
	userInfo: any;

	showButtons: boolean = true;
	publicacionesToShow: PublicacionModel[] = [];
	trueques: TruequeModel[] = [];

	userType: 'notLoggedIn' | 'publicacionOrigen' | 'publicacionPropuesta' = 'notLoggedIn';

	constructor(private truequeService: TruequesService, private route: ActivatedRoute,
		private showErrorService: ShowErrorService, private auth: AuthService,
		private router: Router, private usuarioService: UsuarioService, public dialog: MatDialog) {

		//console.log('PRE USER DATA');
		this.userData = { isSwapper: auth.isUserSwapper(), isLoggedIn: auth.isUserLoggedIn }
		console.log('POST USER DATA', this.userData);

		if (this.userData && this.userData.isLoggedIn) {
			usuarioService.getUserByID(auth.getUserID()).subscribe({
				next: (res: any) => {
					this.userInfo = res;
					console.log(this.publicacion, this.userInfo);
				}
			})
		}

		route.paramMap.subscribe(params => {
			console.log(params);
			this.id_publicacion = params.get('id_publicacion') || 0;
			if (!this.id_publicacion) showErrorService.show('Error!', 'No pudimos encontrar la información de la colecta que seleccionaste, por favor volvé a intentarlo más tarde.')
			else this.getPublicacion(this.id_publicacion)
		})
	}

	getPublicacion(id: number) {
		this.truequeService.getPublicacion(id).subscribe({
			next: (res: any) => {
				//console.log(res);
				this.publicacion = res;
				this.publicacion.parsedImagenes = this.publicacion.imagenes.split('|')
				if (this.publicacion && this.userInfo && this.publicacion.particularDTO.idParticular == this.userInfo.particularDTO.idParticular)
					this.showButtons = false;
				if(this.userData.isLoggedIn) this.getTrueques()
			},
			error: (error) => {
				console.log(error);
				//this.showErrorService.show('Error!', 'No pudimos encontrar la información de la colecta que seleccionaste, por favor volvé a intentarlo más tarde.')
			}
		})
	}

	getTrueques() {
		this.loading = true;
		this.truequeService.getTruequesFromPublicacion(this.publicacion.idPublicacion).subscribe({
			next: async (trueques: any) => {
				console.log(trueques, this.userData.isLoggedIn);
				this.trueques = await trueques;
				if(this.userData.isLoggedIn) {
					this.publicacionesToShow = [];
					console.log(this.publicacionesToShow);
					
					for (const trueque of trueques) {
						this.userType = trueque.publicacionDTOpropuesta.particularDTO.idParticular == this.userInfo.particularDTO.idParticular ? 'publicacionPropuesta' :
							trueque.publicacionDTOorigen.idPublicacion == this.publicacion.idPublicacion ? 'publicacionOrigen' : 'notLoggedIn';
							if(this.userType == 'publicacionOrigen' || this.userType == 'publicacionPropuesta') {
								trueque.publicacionDTOpropuesta.estadoTrueque = trueque.estadoTrueque.trim();
								console.log(trueque.estadoTrueque == 'PENDIENTE');
							
								await this.publicacionesToShow.push(trueque.publicacionDTOpropuesta)
						}
					}
				}
				await this.publicacionesToShow.map(item => {
					item.parsedImagenes = item.imagenes.split('|')
				})
				console.log(this.publicacionesToShow);
				
				this.loading = false;
			}
		})
	}

	intercambiar() {
		if (this.auth.isUserLoggedIn) {
			this.dialog.open(TrocarModalComponent, {
				data: {
					publicacion: this.publicacion,
				},
				minWidth: 100,
				maxHeight: '90vh'
			});

			this.dialog.afterAllClosed.subscribe((result: any) => {
				console.log('result trocar', result);
				if(result) this.getTrueques()
			})
		} else {
			Swal.fire({
				title: '¡Necesitás una cuenta!',
				text: 'Para poder intercambiar, tenés que usar tu cuenta.',
				icon: 'warning',
				confirmButtonText: 'Iniciar sesión',
				showCancelButton: true,
				cancelButtonText: 'Cancelar'
			}).then(({ isConfirmed }) => {
				if (isConfirmed) this.router.navigate(['login'])
			})
		}
	}

	comprar() {
		if (this.auth.isUserLoggedIn) {

		} else {
			Swal.fire({
				title: '¡Necesitás una cuenta!',
				text: 'Para poder comprar, tenés que usar tu cuenta.',
				icon: 'warning',
				confirmButtonText: 'Iniciar sesión',
				showCancelButton: true,
				cancelButtonText: 'Cancelar'
			}).then(({ isConfirmed }) => {
				if (isConfirmed) this.router.navigate(['login'])
			})
		}
	}

	getImage(image: any) {
		return this.truequeService.getImagen(image)
	}

	zoomImage(img?: string) {
		if (img) {
			Swal.fire({
				html: `<img src="${this.getImage(img)}" style="width: 100%"/>`,
				showConfirmButton: false,
				showCloseButton: true
			})
		}
	}

	changeStatusTrueque(event: any) {
		console.log(event);
		const trueque = this.trueques.find(item => item.publicacionDTOpropuesta.idPublicacion == event.publicacion.idPublicacion)
		console.log(trueque);
		if (trueque) {
			let title = '';
			let text = '';
			let confirm = '';
			let cancel = '';
			let deny = '';
			let icon: 'success' | 'warning' = 'warning';

			switch (event.newStatus) {
				case 'CANCELADO':
					title = 'Confirmar Cancelación';
					text = '¿Estás seguro/a que querés cancelar esta donación? La acción es irreversible, pero podrás crear otra donación luego.';
					deny = 'Sí, cancelar donación';
					cancel = 'No, mantener donación';
					icon = 'warning';
					break;
				case 'ACEPTADO':
					title = 'Confirmar Aprobación';
					text = 'Confirmá que aceptás la donación. Esta acción es irreversible ya que comenzará con el proceso de envío.';
					confirm = 'Sí, aceptar donación';
					cancel = 'No, cancelar';
					icon = 'warning';
					break;
				case 'RECHAZADO':
					title = 'Confirmar Rechazo';
					text = '¿Estás seguro/a que querés rechazar esta donación? La acción es irreversible.';
					deny = 'Sí, rechazar donación';
					cancel = 'No, cancelar';
					icon = 'warning';
					break;
				default:
					title = 'Confirmar Acción';
					text = 'Cambiar el estado de la donación es irreversible, ¿Estás seguro/a que querés continuar?';
					confirm = 'Sí, continuar';
					cancel = 'No, cancelar';
					icon = 'warning';
					break;
			}
			Swal.fire({
				title,
				text,
				showConfirmButton: confirm != '',
				confirmButtonText: confirm,
				showDenyButton: deny != '',
				denyButtonText: deny,
				showCancelButton: cancel != '',
				cancelButtonText: cancel,
				icon,
				reverseButtons: true
			}).then(({ isConfirmed, isDenied }) => {
				if (isConfirmed || isDenied) {
					this.truequeService.cambiarEstadoTrueque(trueque.idTrueque, event.newStatus).subscribe({
						next: (res: any) => {
							console.log(res);
							this.getTrueques();
							//Swal.fire('')
						}
					})
				}
			})
		}
	}
}
