import { Component } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { CardButtonModel, CardModel } from 'src/app/models/card.model';
import { DonacionModel } from 'src/app/models/donacion.model';
import { OrdenModel } from 'src/app/models/orden.model';
import { AuthService } from 'src/app/services/auth.service';
import { DonacionesService } from 'src/app/services/donaciones.service';
import { LogisticaService } from 'src/app/services/logistica.service';
import { ShowErrorService } from 'src/app/services/show-error.service';
import { EnvioModalComponent } from 'src/app/shared/envio-modal/envio-modal.component';
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

	selectingMode: boolean = false;
	selectedCards: CardModel[] = [];

	colectaParaEnvio?: number;

	userOrders: OrdenModel[] = [];
	screenWidth: number;

	constructor(public dialog: MatDialog, private donacionesService: DonacionesService,
		private showErrorService: ShowErrorService, private logisticaService: LogisticaService) {
		this.getDonaciones()
		this.screenWidth = (window.innerWidth > 0) ? window.innerWidth : screen.width;
	}

	getDonaciones() {
		this.loading = true;
		this.donaciones.splice(0)
		this.logisticaService.obtenerMisOrdenes('donaciones').subscribe({
			next: (res: any) => {
				if (res.length > 0) {
					this.userOrders = res;
				}
			}
		})
		this.donacionesService.getMisDonaciones().subscribe({
			next: (res: any) => {
				if (res) {
					this.donaciones = res;
					this.donaciones.map((donacion: any) => {
						if (donacion.idDonacion) donacion['last_status'] = donacion.estado;
						if (donacion.imagenes) donacion.parsedImagenes = donacion.imagenes.split('|')
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
		this.donaciones.sort((a, b) => new Date(b.fechaDonacion).getTime() - new Date(a.fechaDonacion).getTime());
		const auxDonaciones: CardModel[] = [];
		for (const donacion of this.donaciones) {
			let stringCaracteristicas = '';
			for (const [i, caract] of donacion.caracteristicaDonacion.entries()) {
				if (i == 0) stringCaracteristicas = caract.caracteristica
				else stringCaracteristicas += ' - ' + caract.caracteristica
			}
			
			const matchingOrders = this.userOrders.filter(order => {
				return order.productosADonarDeOrdenList.some((producto: any) => {
					return producto.idDonacion == donacion.idDonacion;
				});
			});
			
			const item: CardModel = {
				id: donacion.idDonacion,
				imagen: donacion.parsedImagenes ? donacion.parsedImagenes[0] : 'no_image',
				titulo: donacion.descripcion,
				valorPrincipal: `${donacion.cantidadDonacion} ${donacion.cantidadDonacion == 1 ? 'unidad' : 'unidades'} de ${donacion.producto.descripcion}`,
				valorSecundario: stringCaracteristicas,
				fecha: donacion.fechaDonacion,
				usuario: {
					id: donacion.producto.colectaDTO.fundacionDTO.usuarioDTO.idUsuario,
					imagen: donacion.producto.colectaDTO.fundacionDTO.usuarioDTO.avatar,//donacion.particularDTO.usuarioDTO.avatar,
					nombre: donacion.producto.colectaDTO.fundacionDTO.nombre,//donacion.particularDTO.nombre + ' ' + donacion.particularDTO.apellido,
					puntaje: donacion.producto.colectaDTO.fundacionDTO.puntaje,//donacion.particularDTO.puntaje,
					localidad: donacion.producto.colectaDTO.fundacionDTO.direcciones[0].localidad,//donacion.particularDTO.direcciones[0].localidad
				},
				action: 'access',
				buttons: this.getButtonsForCard(donacion, matchingOrders),
				estado: donacion.estadoEnvio == 'RECIBIDO' ? 'RECIBIDA' : donacion.estadoDonacion.replace('_',' '),
				idAuxiliar: donacion.producto.colectaDTO.idColecta,
				codigo: 'Donación',
				estadoAux: donacion.estadoEnvio
			}
			auxDonaciones.push(item)
			this.loading = false;
		}
		this.donacionesCardList = auxDonaciones;
	}

	getButtonsForCard(donacion: DonacionModel, matchingOrders: OrdenModel[]): CardButtonModel[] {
		if(donacion.estadoEnvio == 'RECIBIDO' || donacion.estadoDonacion == 'RECIBIDA') {
			return [{ name: 'OPINAR', icon: 'rate_review', color: 'opinion', status: 'OPINAR', action: 'opinar' }];
		} else if(donacion.estadoDonacion == 'APROBADA') {
			if(matchingOrders.length > 0) { // ya tiene una orden de envio
				const lastOrderCancelled = matchingOrders[matchingOrders.length - 1].listaFechaEnvios.find(item => item.estado == 'CANCELADO');
				if(lastOrderCancelled) return [
					{name: 'Configurar envío', icon: 'local_shipping', color: 'info', status: 'INFO', action: 'configurar_envio'},
					{name: 'Llevar en persona', icon: 'directions_walk', color: 'info', status: 'EN_ESPERA', action: 'change_status'}
				]
				else return [{ name: 'Ver envío', icon: 'local_shipping', color: 'info', status: 'INFO', action: 'ver_envio' }]
			} else return [
				{name: 'Configurar envío', icon: 'local_shipping', color: 'info', status: 'INFO', action: 'configurar_envio'},
				{name: 'Llevar en persona', icon: 'directions_walk', color: 'info', status: 'EN_ESPERA', action: 'change_status'}
			]
		} else if (donacion.estadoDonacion == 'PENDIENTE')
			return [{ name: 'CANCELAR', icon: 'close', color: 'warn', status: 'CANCELADA', action: 'change_status' }]
		else return [];
	}

	selectDonaciones() {
		Swal.fire({
			title: 'Configurá tu envío!',
			text: 'Seleccioná las tarjetas de tus donaciones haciendo click en ellas, vas a ver que las que seleccionen se pintarán. Recordá que solo podés seleccionar donaciones a una misma colecta. Una vez que termines, clickeá el botón CONFIRMAR SELECCIÓN.',
			icon: 'info',
			confirmButtonText: '¡VAMOS!'
		})
		const auxDonaciones = this.donacionesCardList;
		auxDonaciones.map(donacion => {
			// Find the orders that match the idDonacion from the cards array
			const matchingOrders = this.userOrders.filter(order => {
				return order.productosADonarDeOrdenList.some((producto: any) => {
					return producto.idDonacion == donacion.id;
				});
			});
			
			if (donacion.estado == 'APROBADA' && matchingOrders.length == 0) {
				donacion.action = 'select';
				donacion.codigo = 'Donación';
				donacion.buttons = [{ name: 'Agregar', icon: 'add', color: 'info', status: 'INFO', action: 'add_or_remove' }]
			} else {
				donacion.buttons = []
			}
		});
		this.donacionesCardList = auxDonaciones;
		this.selectingMode = true;
	}

	showButtons() {
		if (this.donacionesCardList.some(item => item.estado == 'APROBADA')) return true;
		else return false;
	}

	selectCard(cardID: number) {
		/* VALIDAR QUE LA TARJETA SELECCIONADA TENGA EL MISMO ID FUNDACION QUE LAS DEMAS QUE YA ESTÉN SELECCIONADAS */
		const donacionSeleccionada = this.donacionesCardList.find(item => item.id == cardID)

		if (donacionSeleccionada) {
			if (this.selectedCards.length == 0) { // first one selected
				this.colectaParaEnvio = donacionSeleccionada.idAuxiliar;
				this.donacionesCardList.map(item => { if (item.id == cardID) item.isSelected = true })
				this.selectedCards.push(donacionSeleccionada)
				const auxDonaciones = this.donacionesCardList;
				auxDonaciones.map(donacion => {
					if (donacion.idAuxiliar != this.colectaParaEnvio) {
						donacion.action = 'detail';
						donacion.codigo = undefined;
						donacion.buttons = [];
					} else if (this.selectedCards.includes(donacionSeleccionada) && donacion.id == donacionSeleccionada.id) {
						donacion.buttons = [{ name: 'Quitar', icon: 'remove', color: 'info', status: 'INFO', action: 'add_or_remove' }]
					}
				});
				this.donacionesCardList = auxDonaciones;
			} else {
				if (this.colectaParaEnvio != donacionSeleccionada.idAuxiliar) {
					Swal.fire('¡Colecta distinta!', 'No podés seleccionar distintas colectas para un mismo envío', 'warning')
				} else {
					this.selectedCards.push(donacionSeleccionada)
					this.donacionesCardList.map(donacion => {
						if(donacion.id == donacionSeleccionada.id) {
							donacion.buttons = [{ name: 'Quitar', icon: 'remove', color: 'info', status: 'INFO', action: 'add_or_remove' }]
						}
					})

				}
			}

		}

	}

	unselectCard(cardID: number) {
		const donacionDeseleccionada = this.donacionesCardList.find(item => item.id == cardID)
		if (donacionDeseleccionada) {
			if (this.selectedCards.length == 1) { // last one unselected
				this.colectaParaEnvio = undefined;
				const auxDonaciones = this.donacionesCardList;
				auxDonaciones.map(donacion => {
					// Find the orders that match the idDonacion from the cards array
					const matchingOrders = this.userOrders.filter(order => {
						return order.productosADonarDeOrdenList.some((producto: any) => {
							return producto.idDonacion == donacion.id
						});
					});
					//if(matchingOrders.length > 0) this.yaTieneEnvio = matchingOrders;
					console.log(donacion, matchingOrders);
					
					if (donacion.estado == 'APROBADA' && matchingOrders.length == 0) {
						donacion.action = 'select';
						donacion.codigo = 'Donación';
						donacion.buttons = [{ name: 'Agregar', icon: 'add', color: 'info', status: 'INFO', action: 'add_or_remove' }]
					} else {
						donacion.action = 'detail';
						donacion.codigo = undefined;
						donacion.buttons = [];
					}
				});
				this.donacionesCardList = auxDonaciones;
				this.selectedCards = [];
			} else {
				const aux = this.selectedCards.filter(item => item.id != cardID)
				this.selectedCards = aux;
				this.donacionesCardList.map(donacion => {
					if(donacion.id == cardID) {
						donacion.action = 'select';
						donacion.codigo = 'Donación';
						donacion.buttons = [{ name: 'Agregar', icon: 'add', color: 'info', status: 'INFO', action: 'add_or_remove' }]
					}
				})
			}
		}
		this.donacionesCardList.map(item => { if (item.id == cardID) item.isSelected = false })
	}

	cancelSelect() {
		this.selectedCards = [];
		this.selectingMode = false;
		const auxDonaciones = this.donacionesCardList;
		auxDonaciones.map(donacion => {

			// Find the orders that match the idDonacion from the cards array
			const matchingOrders = this.userOrders.filter(order => {
				return order.productosADonarDeOrdenList.some((producto: any) => {
					return donacion.id == producto.idDonacion
				});
			});

			const donacModel = this.donaciones.find(d => d.idDonacion == donacion.id)

			donacion.action = 'detail';
			donacion.codigo = undefined;
			if(donacModel) donacion.buttons = this.getButtonsForCard(donacModel, matchingOrders)

		});
		this.donacionesCardList = auxDonaciones;
		this.selectedCards = [];
	}

	confirmSelectedCards() {
		const cardsWithData = this.donaciones.filter(item => this.selectedCards.some(card => card.id == item.idDonacion))
		const dialogRef = this.dialog.open(EnvioModalComponent, {
			maxWidth: '60vw',
			maxHeight: '70vh',
			width: '100%',
			panelClass: 'full-screen-modal',
			data: { cards: cardsWithData }
		});
		dialogRef.afterClosed().subscribe((result) => {
			console.log('closed', result);
			if(result) this.getDonaciones()
		})
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

	
	cardModalClosed(event: any) {
		if(event.result) this.cancelSelect()
	}
}
