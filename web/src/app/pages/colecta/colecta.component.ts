import { Component } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { CardModel } from 'src/app/models/card.model';
import { ColectaModel } from 'src/app/models/colecta.model';
import { DonacionModel } from 'src/app/models/donacion.model';
import { AuthService } from 'src/app/services/auth.service';
import { DonacionesService } from 'src/app/services/donaciones.service';
import { ProductosService } from 'src/app/services/productos.service';
import { ShowErrorService } from 'src/app/services/show-error.service';
import { UsuarioService } from 'src/app/services/usuario.service';

@Component({
  selector: 'app-colecta',
  templateUrl: './colecta.component.html',
  styleUrls: ['./colecta.component.scss']
})
export class ColectaComponent {

	id_colecta: string = '';
	colecta!: ColectaModel;
	donaciones: DonacionModel[] = [];

	userData?: any;
	userInfo?: any;
	loading: boolean = true;
	donacionesToShow: DonacionModel[] = [];

	donacionesAbiertas: CardModel[] = [];
	donacionesCerradas: CardModel[] = [];

	constructor(private route: ActivatedRoute, private router: Router, private auth: AuthService,
		private donacionesService: DonacionesService, private showErrorService: ShowErrorService,
		private productoService: ProductosService, private usuarioService: UsuarioService,
		public dialog: MatDialog){
		route.paramMap.subscribe(params => {
			console.log(params);
			this.id_colecta = params.get('id_colecta') || '';
			if(!this.id_colecta) showErrorService.show('Error!', 'No pudimos encontrar la información de la colecta que seleccionaste, por favor volvé a intentarlo más tarde.')
		})
		this.userData = { isSwapper: auth.isUserSwapper(), isLoggedIn: auth.isUserLoggedIn }
		if(this.userData && this.userData.isLoggedIn){
			usuarioService.getUserByID(auth.getUserID()).subscribe({
				next: (res: any) => {
					this.userInfo = res;
				}
			})
		}
	}
	
	ngOnInit(): void {
		this.getColecta();
	}

	getColecta(){
		this.donacionesService.getColecta(this.id_colecta).subscribe({
			next: (colecta: any) => {
				if (colecta) {
					//console.log(colecta);
					this.colecta = colecta;
					//this.colecta.imagen = this.getImage(this.colecta.imagen);

					this.productoService.getProductosColecta(colecta.idColecta).subscribe({
						next: (res: any) => {
							colecta.productos = res;
						},
						error: (error) => {
							console.log('error', error);
						}
					})

					if(this.auth.isUserLoggedIn){
						this.getDonaciones();
					} else this.loading = false;
				} else this.showErrorService.show('Error!', 'No se encontró la información de la colecta que seleccionaste. Intentá nuevamente más tarde.')
			},
			error: (error) => {
				console.log(error);
				//this.showErrorService.show('Error!', 'Ocurrió un error al traer la información de la colecta que seleccionaste. Intentá nuevamente más tarde.')
			}
		})
	}

	getDonaciones() {
		this.loading = true;
		this.donacionesService.getDonacionesColecta(this.colecta.idColecta).subscribe({
			next: (donaciones: any) => {
				console.log(donaciones);
				this.donaciones = donaciones;
				this.donaciones.map(donacion => {
					if(donacion.imagenes) donacion.parsedImagenes = donacion.imagenes.split('|')
				})

				//TODO: REVISAR CUANDO MUESTRA DONACIONES
				if (this.userData.isSwapper && this.userInfo) {
					this.donacionesToShow = this.donaciones.filter(item => item.particularDTO.idParticular == this.userInfo.particularDTO.idParticular)
					//this.showDonaciones = true;
				} else if(this.userData) { // TODO: IF colecta.id_fundacion == userData.id_fundacion --> show donaciones
					this.donacionesToShow = donaciones;
					//this.showDonaciones = true;
				}
				this.donacionesToShow.map(item => {
					item.parsedImagenes = item.imagenes.split('|')
				})
			},
			error: (error) => {
				console.log('error', error);
				
			}, complete: () => {
				this.parseDonaciones();
				this.loading = false;
			}
		})
	}

	parseDonaciones() {
		this.donacionesAbiertas.splice(0);
		this.donacionesCerradas.splice(0);
		const auxListAbierta: CardModel[] = [];
		const auxListCerrada: CardModel[] = [];
		for (const donacion of this.donacionesToShow) {
			console.log(donacion);
			
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
				fecha: donacion.fechaDonacion,
				usuario: {
					id: donacion.particularDTO.usuarioDTO.idUsuario,
					imagen: donacion.particularDTO.usuarioDTO.avatar,
					nombre: donacion.particularDTO.nombre + ' ' + donacion.particularDTO.apellido,
					puntaje: donacion.particularDTO.puntaje,
					localidad: donacion.particularDTO.direcciones[0].localidad
				},
				action: 'detail',
				buttons: this.getButtonsForCard(donacion),
				estado: donacion.estadoDonacion,
				idAuxiliar: this.colecta.idColecta
			}
			if(donacion.estadoDonacion == 'PENDIENTE') auxListAbierta.push(item)//this.donacionesAbiertas.push(item)
			else auxListCerrada.push(item)//this.donacionesCerradas.push(item)
		}

		this.donacionesAbiertas = auxListAbierta;
		this.donacionesCerradas = auxListCerrada.sort((a, b) => {
			if (a.estado === 'EN_ESPERA' && b.estado !== 'EN_ESPERA') {
			  return -1; // 'a' comes before 'b'
			} else if (a.estado !== 'EN_ESPERA' && b.estado === 'EN_ESPERA') {
			  return 1; // 'b' comes before 'a'
			} else {
			  return 0; // No change in order
			}
		  });;
		console.log(this.donacionesAbiertas, this.donacionesCerradas);
		
	}

	getButtonsForCard(donacion: DonacionModel) {
		if(donacion.estadoDonacion == 'PENDIENTE') {
			if(this.userData.isSwapper) return [{name: 'CANCELAR', icon: 'close', color: 'warn', status: 'CANCELADA'}]
			else {
				return [
					{name: 'ACEPTAR', icon: 'check', color: 'primary', status: 'APROBADA'},
					{name: 'RECHAZAR', icon: 'close', color: 'warn', status: 'RECHAZADA'},
				]
			}
		}
		else if (donacion.estadoDonacion == 'EN_ESPERA' && !this.userData.isSwapper) {
			return [{name: 'RECIBIDO', icon: 'done_all', color: 'primary', status: 'RECIBIDA'}]
		} else if(donacion.estadoDonacion == 'APROBADA') {
			if(this.userData.isSwapper) return [
				{name: 'Configurar envío', icon: 'local_shipping', color: 'info', status: 'INFO'},
				{name: 'Llevar en persona', icon: 'directions_walk', color: 'info', status: 'EN_ESPERA'}
			]
			else return []
		} else return [];
	}

	isArray(item:any){
		return item.constructor === Array;
	}

	cardModalClosed(event: any) {
		if(event.result) this.getDonaciones()
	}

}
