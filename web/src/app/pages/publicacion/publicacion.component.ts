import { AfterViewInit, Component } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { CardModel } from 'src/app/models/card.model';
import { PublicacionModel } from 'src/app/models/publicacion.model';
import { TruequeModel } from 'src/app/models/trueque.model';
import { AuthService } from 'src/app/services/auth.service';
import { ComprasService } from 'src/app/services/compras.service';
import { ShowErrorService } from 'src/app/services/show-error.service';
import { TruequesService } from 'src/app/services/trueques.service';
import { UsuarioService } from 'src/app/services/usuario.service';
import Swal from 'sweetalert2';
import { TrocarModalComponent } from './trocar-modal/trocar-modal.component';

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

	truequeAceptado: CardModel[] = [];
	historialTrueques: CardModel[] = [];
	truequesActivos: CardModel[] = [];

	init: number = 0;

	constructor(private truequeService: TruequesService, private route: ActivatedRoute,
		private showErrorService: ShowErrorService, private auth: AuthService,
		private router: Router, private usuarioService: UsuarioService, public dialog: MatDialog,
		private comprasService: ComprasService) {

		this.userData = { isSwapper: auth.isUserSwapper(), isLoggedIn: auth.isUserLoggedIn }
		this.route.paramMap.subscribe(params => {
			this.loading = true;
			this.id_publicacion = params.get('id_publicacion');
			this.truequeAceptado = [];
			this.historialTrueques = [];
			this.truequesActivos = [];
			
			if(this.init != 0) this.ngAfterViewInit();
			this.init++;
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
				else this.loading = false;
			},
			error: (error) => {
				console.log(error);
				this.loading = false;
			}
		})
	}

	getTrueques() {
		this.loading = true;
		this.truequesActivos.splice(0);
		this.historialTrueques.splice(0);
		this.truequeAceptado.splice(0);
		this.truequeService.getTruequesFromPublicacion(this.publicacion.idPublicacion).subscribe({
			next: (trueques: any) => {
				//console.log('TRUEQUES', trueques);
				// Todos los trueques en los que esta publicacion es ORIGEN
				this.trueques = trueques;
				
				if(this.userType != 'publicacionOrigen' && this.userData.isLoggedIn) {
					this.truequeService.getMisPublicaciones().subscribe({
						next: (res: any) => {
							//console.log('PUBLICACIONES DEL USER', res);
							// Publicaciones del usuario loggeado
							this.publicacionesToShow = [];
							const userPublicaciones = res;
							for (const trueque of this.trueques) {
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
						}, error: () => {this.loading = false},
						complete: () => {
							this.parsePublicaciones()
							this.loading = false;
						}
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
					this.parsePublicaciones();
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
			this.comprasService.comprar(this.publicacion.idPublicacion).subscribe({
				next: (res: any) => {
					console.log(res);
					Swal.fire({
						title: '¡Ya casi es tuyo!',
						text: 'Terminá tu compra en Mercado Pago, luego podrás verla en Mis Compras!',
						icon: 'success',
						confirmButtonText: 'IR A MIS COMPRAS',
						allowOutsideClick: false, allowEscapeKey: false
					}).then(({isConfirmed}) => {
						if(isConfirmed) window.open(res.initPoint, '_blank')
					})
				}
			})
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

	getButtonsForCards() {
		if(this.userType == 'publicacionPropuesta') {
			return [{name: 'CANCELAR', icon: 'close', color: 'warn', status: 'CANCELADO'}];
		} else if (this.userType == 'publicacionOrigen'){
			return [
				{name: 'ACEPTAR', icon: 'check', color: 'primary', status: 'APROBADO'},
				{name: 'RECHAZAR', icon: 'close', color: 'warn', status: 'RECHAZADO'},
				{name: 'RECIBIDO', icon: 'done_all', color: 'primary', status: 'RECIBIDO'},
			];
		} else return [];
	}

	parsePublicaciones() {
		for (const publicacion of this.publicacionesToShow) {
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
				action: 'detail',
				buttons: [],
				estado: publicacion.estadoTrueque,
				idAuxiliar: this.trueques.find(item => item.publicacionDTOpropuesta.idPublicacion == publicacion.idPublicacion)?.idTrueque
			}

			if(publicacion.estadoTrueque == 'APROBADO') {
				// ACEPTADO
				this.truequeAceptado.push(item)
			} else if(publicacion.estadoTrueque == 'PENDIENTE' && publicacion.estadoPublicacion == 'ABIERTA') {
				// ACTIVOS
				item.valorSecundario = publicacion.precioVenta ? `$${publicacion.precioVenta}` : undefined
				item.buttons = this.getButtonsForCards();
				this.truequesActivos.push(item)
			} else /* if(publicacion.estadoTrueque != 'PENDIENTE' || publicacion.estadoPublicacion != 'ABIERTA') */ {
				// HISTORIAL
				item.disabled = true;
				this.historialTrueques.push(item)
			}
		}
	}

	hasApprovedTrueque() {
		return this.publicacionesToShow.filter(item => item.estadoTrueque == 'APROBADO')
	}
}
