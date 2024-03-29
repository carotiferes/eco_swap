import { CurrencyPipe, DOCUMENT } from '@angular/common';
import { AfterViewInit, Component, Inject } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { CardButtonModel, CardModel } from 'src/app/models/card.model';
import { PublicacionModel } from 'src/app/models/publicacion.model';
import { TruequeModel } from 'src/app/models/trueque.model';
import { AuthService } from 'src/app/services/auth.service';
import { ComprasService } from 'src/app/services/compras.service';
import { ShowErrorService } from 'src/app/services/show-error.service';
import { TruequesService } from 'src/app/services/trueques.service';
import { UsuarioService } from 'src/app/services/usuario.service';
import Swal from 'sweetalert2';
import { TrocarModalComponent } from './trocar-modal/trocar-modal.component';
import { ChatService } from 'src/app/services/chat.service';
import { ParticularModel } from 'src/app/models/particular.model';
import { ChatComponent } from 'src/app/shared/chat/chat.component';

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
	mainPublicacionCard?: CardModel;

	userCompras: any[] = [];

	init: number = 0;
	screenWidth: number;

	mensajes: any[] = [];
	nuevoMensaje: string = '';

	initChat: number = 0;
	elOtroSwapper?: ParticularModel;

	constructor(private truequeService: TruequesService, private route: ActivatedRoute,
		private showErrorService: ShowErrorService, private auth: AuthService,
		private router: Router, private usuarioService: UsuarioService, public dialog: MatDialog,
		private comprasService: ComprasService, @Inject(DOCUMENT) private document: Document,
		private chatService: ChatService, private currencyPipe: CurrencyPipe) {

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
		this.screenWidth = (window.innerWidth > 0) ? window.innerWidth : screen.width;
	}

	ngAfterViewInit(): void {
		if (this.userData && this.userData.isLoggedIn) {
			this.userData.id_user = this.auth.getUserID()
			this.usuarioService.getUserByID(this.auth.getUserID()).subscribe({
				next: (res: any) => {
					this.userInfo = res;
					this.getPublicacion(this.id_publicacion);
				}
			})
		} else this.getPublicacion(this.id_publicacion);
		this.scrollToBottom();
	}

	scrollToBottom() {
		const elem = this.document.getElementById('chatContainer')
		if(elem) elem.scrollTop = elem.scrollHeight;
	}

	getPublicacion(id: number) {
		this.loading = true;
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
		this.publicacionesToShow.splice(0);
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
					/* TODO: MOSTRAR SI LA PUBLIC ESTA VENDIDA
					this.comprasService.getMyCompras().subscribe({
						next: (compras: any) => {
							const userCompras = compras || [];
						}
					}) */
					this.parsePublicaciones();
					this.loading = false;
				}
			}, error: () => {this.loading = false}
		})
	}

	intercambiar() {
		if (this.auth.isUserLoggedIn) {
			let dialogConfig: any;
			if(this.screenWidth < 576) {
				dialogConfig = {
					data: {
						publicacion: this.publicacion,
						trueques: this.trueques
					},
					minWidth: '70vw',
					maxHeight: '80vh',
					position: {
						top: '50vh',
						left: '50vw'
					},
					panelClass:'makeItMiddle'
				}
			} else {
				dialogConfig = {
					data: {
						publicacion: this.publicacion,
						trueques: this.trueques
					},
					minWidth: '80vw',
					maxHeight: '85vh',
				}
			}
			const dialogRef = this.dialog.open(TrocarModalComponent, dialogConfig);

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
						confirmButtonText: '¡VAMOS!',
						allowOutsideClick: false, allowEscapeKey: false
					}).then(({isConfirmed}) => {
						if(isConfirmed) {
							this.router.navigate(['/mis-compras'])
							window.open(res.initPoint, '_blank')
						}
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

	getButtonsForCards(): CardButtonModel[] {
		if(this.userType == 'publicacionPropuesta') {
			return [{name: 'CANCELAR', icon: 'close', color: 'warn', status: 'CANCELADO', action: 'change_status'}];
		} else if (this.userType == 'publicacionOrigen'){
			return [
				{name: 'ACEPTAR', icon: 'check', color: 'primary', status: 'APROBADO', action: 'change_status'},
				{name: 'RECHAZAR', icon: 'close', color: 'warn', status: 'RECHAZADO', action: 'change_status'},
			];
		} else return [];
	}

	parsePublicaciones() {
		this.truequesActivos.splice(0);
		this.historialTrueques.splice(0);
		this.truequeAceptado.splice(0);
		this.mainPublicacionCard = undefined;
		const auxList1: CardModel[] = [];
		const auxList2: CardModel[] = [];
		const auxList3: CardModel[] = [];

		this.publicacionesToShow.sort((a, b) => new Date(b.fechaPublicacion).getTime() - new Date(a.fechaPublicacion).getTime());
		for (const publicacion of this.publicacionesToShow) {
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
				action: 'detail',
				buttons: [],
				estado: publicacion.estadoTrueque,
				idAuxiliar: this.trueques.find(item => item.publicacionDTOpropuesta.idPublicacion == publicacion.idPublicacion)?.idTrueque
			}

			if(publicacion.estadoTrueque == 'APROBADO') {
				// ACEPTADO
				item.buttons = this.userType == 'publicacionOrigen' && publicacion.estadoEnvio != 'RECIBIDO' ? 
					[{name: 'RECIBIDO', icon: 'done_all', color: 'primary', status: 'RECIBIDA', action: 'change_status'}] : 
					this.userType == 'publicacionOrigen' && publicacion.estadoEnvio == 'RECIBIDO' ? 
					[{ name: 'OPINAR', icon: 'rate_review', color: 'opinion', status: 'OPINAR', action: 'opinar' }] : [];
				item.estadoAux = publicacion.estadoEnvio;
				console.log(item);
				
				auxList1.push(item)
				const trueque = this.trueques.find(item => item.publicacionDTOpropuesta.idPublicacion == publicacion.idPublicacion && item.estadoTrueque == 'APROBADO')
				if(trueque) {
					this.elOtroSwapper = this.userType == 'publicacionOrigen' ? trueque.publicacionDTOpropuesta.particularDTO : trueque.publicacionDTOorigen.particularDTO
					this.chatService.getMyMensajes(trueque.idTrueque).subscribe({
						next: (res: any) => {
							this.mensajes = res;
						}
					})
				}
			} else if(publicacion.estadoTrueque == 'PENDIENTE' && publicacion.estadoPublicacion == 'ABIERTA') {
				// ACTIVOS
				item.valorSecundario = publicacion.precioVenta ? `${this.currencyPipe.transform(publicacion.precioVenta)}` : undefined
				item.buttons = this.getButtonsForCards();
				auxList2.push(item)
			} else /* if(publicacion.estadoTrueque != 'PENDIENTE' || publicacion.estadoPublicacion != 'ABIERTA') */ {
				// HISTORIAL
				item.disabled = true;
				auxList3.push(item)
			}
		}
		this.mainPublicacionCard = {
			id: this.publicacion.idPublicacion,
			imagen: this.publicacion.parsedImagenes? this.publicacion.parsedImagenes[0] : 'no_image',
			titulo: this.publicacion.titulo,
			valorPrincipal: `${this.currencyPipe.transform(this.publicacion.valorTruequeMin)} - ${this.currencyPipe.transform(this.publicacion.valorTruequeMax)}`,
			fecha: this.publicacion.fechaPublicacion,
			usuario: {
				id: this.publicacion.particularDTO.usuarioDTO.idUsuario,
				imagen: this.publicacion.particularDTO.usuarioDTO.avatar,
				nombre: this.publicacion.particularDTO.nombre + ' ' + this.publicacion.particularDTO.apellido,
				puntaje: this.publicacion.particularDTO.puntaje,
				localidad: this.publicacion.particularDTO.direcciones[0].localidad
			},
			action: 'detail',
			codigo: 'Publicación',
			buttons: this.userType == 'publicacionPropuesta' && this.publicacion.estadoEnvio != 'RECIBIDO' ? 
				[{name: 'RECIBIDO', icon: 'done_all', color: 'primary', status: 'RECIBIDA', action: 'change_status'}] : 
				this.userType == 'publicacionPropuesta' && this.publicacion.estadoEnvio == 'RECIBIDO' ? 
				[{ name: 'OPINAR', icon: 'rate_review', color: 'opinion', status: 'OPINAR', action: 'opinar' }] : [],
			estado: this.publicacion.estadoPublicacion,
			idAuxiliar: this.trueques.find(item => item.publicacionDTOpropuesta.idPublicacion == this.publicacion.idPublicacion)?.idTrueque,
			estadoAux: this.publicacion.estadoEnvio
		}
		this.truequeAceptado = auxList1;
		this.truequesActivos = auxList2;
		this.historialTrueques = auxList3;

		if(this.truequeAceptado.length > 0 && this.userType == 'publicacionPropuesta') this.mainPublicacionCard.estado = undefined;
	}

	hasApprovedTrueque() {
		return this.publicacionesToShow.filter(item => item.estadoTrueque == 'APROBADO')
	}


	openChat() {
		const dialogRef = this.dialog.open(ChatComponent, {
			data: {
				mensajes: this.mensajes,
				userData: this.userData,
				elOtroSwapper: this.elOtroSwapper,
				trueques: this.trueques,
				userType: this.userType,
				publicacion: this.publicacion,
			},
			minHeight: '50vh',
			minWidth: '50vw',
		});
	
		dialogRef.afterClosed().subscribe((result: any) => {
			console.log('result chat', result);
			const trueque = this.trueques.find(item => item.publicacionDTOorigen.idPublicacion == this.publicacion.idPublicacion && item.estadoTrueque == 'APROBADO')
			if(trueque) this.getMessages(trueque.idTrueque);
		})
	}

	getMessages(idTrueque: number) {
		this.chatService.getMyMensajes(idTrueque).subscribe({
			next: (res: any) => {
				this.mensajes = res;
			}
		})
	}
}
