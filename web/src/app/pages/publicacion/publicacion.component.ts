import { AfterViewInit, Component } from '@angular/core';
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
export class PublicacionComponent implements AfterViewInit {

	loading: boolean = false;
	publicacion!: PublicacionModel;
	id_publicacion: any;

	userData: any;
	userInfo: any;

	publicacionesToShow: PublicacionModel[] = [];
	trueques: TruequeModel[] = [];

	userType: 'notLoggedIn' | 'publicacionOrigen' | 'publicacionPropuesta' = 'notLoggedIn';

	constructor(private truequeService: TruequesService, private route: ActivatedRoute,
		private showErrorService: ShowErrorService, private auth: AuthService,
		private router: Router, private usuarioService: UsuarioService, public dialog: MatDialog) {

		this.userData = { isSwapper: auth.isUserSwapper(), isLoggedIn: auth.isUserLoggedIn }
		this.route.paramMap.subscribe(params => {
			this.id_publicacion = params.get('id_publicacion');
		})
	}

	ngAfterViewInit(): void {
		if (this.userData && this.userData.isLoggedIn) {
			this.usuarioService.getUserByID(this.auth.getUserID()).subscribe({
				next: (res: any) => {
					this.userInfo = res;
					this.getPublicacion(this.id_publicacion);
				}
			})
		} else this.getPublicacion(this.id_publicacion);
	}

	getPublicacion(id: number) {
		this.truequeService.getPublicacion(id).subscribe({
			next: (res: any) => {
				this.publicacion = res;
				this.publicacion.parsedImagenes = this.publicacion.imagenes.split('|')
				if (this.publicacion && this.userInfo && this.publicacion.particularDTO.idParticular == this.userInfo.particularDTO.idParticular) {
					this.userType = 'publicacionOrigen';
				}
				if(this.userData.isLoggedIn) this.getTrueques()
			},
			error: (error) => {
				console.log(error);
			}
		})
	}

	getTrueques() {
		this.loading = true;
		this.truequeService.getTruequesFromPublicacion(this.publicacion.idPublicacion).subscribe({
			next: async (trueques: any) => {
				//console.log('TRUEQUES', trueques);
				// Todos los trueques en los que esta publicacion es ORIGEN
				this.trueques = await trueques;
				this.publicacionesToShow = [];

				if(this.userType != 'publicacionOrigen' && this.userData.isLoggedIn) {
					this.truequeService.getMisPublicaciones().subscribe({
						next: async (res: any) => {
							//console.log('PUBLICACIONES DEL USER',await res);
							// Publicaciones del usuario loggeado
							const userPublicaciones = await res;
							for (const trueque of trueques) {
								const commonItem = userPublicaciones.find((publicacion: PublicacionModel) =>
									trueque.publicacionDTOpropuesta.idPublicacion == publicacion.idPublicacion)
								if(commonItem){
									commonItem.estadoTrueque = trueque.estadoTrueque.trim();
									this.publicacionesToShow.push(commonItem);
									this.userType = 'publicacionPropuesta';
								}
							}
							//console.log('TO SHOW',this.publicacionesToShow);

							this.publicacionesToShow.map(item => {
								item.parsedImagenes = item.imagenes.split('|')
							})
							this.loading = false;
						}, error: () => {this.loading = false}
					})
					
				} else if (this.userType == 'publicacionOrigen' && this.userData.isLoggedIn) {
					for (const trueque of trueques) {
						trueque.publicacionDTOpropuesta.estadoTrueque = trueque.estadoTrueque.trim();
						this.publicacionesToShow.push(trueque.publicacionDTOpropuesta)
					}
					//console.log('TO SHOW',this.publicacionesToShow);
					this.publicacionesToShow.map(item => {
						item.parsedImagenes = item.imagenes.split('|')
					})
					this.loading = false;
				}
			}, error: () => {this.loading = false}
		})
	}

	intercambiar() {
		if (this.auth.isUserLoggedIn) {
			const dialogRef = this.dialog.open(TrocarModalComponent, {
				data: {
					publicacion: this.publicacion,
				},
				minWidth: 100,
				maxHeight: '90vh'
			});

			dialogRef.afterClosed().subscribe((result: any) => {
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

	getButtonsForCards() {
		if(this.userType == 'publicacionPropuesta') {
			return [{name: 'cancelar', icon: 'close', color: 'warn', status: 'CANCELADO'}];
		} else if (this.userType == 'publicacionOrigen'){
			return [
				{name: 'aceptar', icon: 'check', color: 'primary', status: 'APROBADO'},
				{name: 'rechazar', icon: 'close', color: 'warn', status: 'RECHAZADO'},
				{name: 'recibida', icon: 'done_all', color: 'primary', status: 'RECIBIDO'},
			];
		} else return [];
	}

	showPublicaciones(type: 'abiertas' | 'cerradas') {
		return this.publicacionesToShow.filter(publicacion => {
			let condition: boolean = false;
			if(type == 'abiertas') condition = (!!publicacion.estadoTrueque && publicacion.estadoTrueque == 'PENDIENTE' && publicacion.estadoPublicacion == 'PENDIENTE')
			else condition = (!!publicacion.estadoTrueque && (publicacion.estadoTrueque != 'PENDIENTE' && publicacion.estadoTrueque != 'APROBADO') || publicacion.estadoPublicacion != 'PENDIENTE')
			return condition;
		}).sort((a, b) => {
			if (a.estadoTrueque === 'APROBADO' && b.estadoTrueque !== 'APROBADO') {
			  return -1; // 'a' comes before 'b'
			} else if (a.estadoTrueque !== 'APROBADO' && b.estadoTrueque === 'APROBADO') {
			  return 1; // 'b' comes before 'a'
			} else {
			  return 0; // No change in order
			}
		  });
	}

	hasApprovedTrueque() {
		return this.publicacionesToShow.filter(item => item.estadoTrueque == 'APROBADO')
	}
}
