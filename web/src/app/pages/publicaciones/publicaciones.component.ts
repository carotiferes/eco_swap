import { CurrencyPipe } from '@angular/common';
import { Component } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { MatAutocompleteSelectedEvent } from '@angular/material/autocomplete';
import { MatChipInputEvent } from '@angular/material/chips';
import { Router } from '@angular/router';
import { Observable, map, startWith } from 'rxjs';
import { CardButtonModel, CardModel } from 'src/app/models/card.model';
import { OrdenModel } from 'src/app/models/orden.model';
import { PublicacionModel } from 'src/app/models/publicacion.model';
import { TruequeModel } from 'src/app/models/trueque.model';
import { AuthService } from 'src/app/services/auth.service';
import { ComprasService } from 'src/app/services/compras.service';
import { LogisticaService } from 'src/app/services/logistica.service';
import { ProductosService } from 'src/app/services/productos.service';
import { ShowErrorService } from 'src/app/services/show-error.service';
import { TruequesService } from 'src/app/services/trueques.service';
import Swal from 'sweetalert2';

@Component({
	selector: 'app-trueque',
	templateUrl: './publicaciones.component.html',
	styleUrls: ['./publicaciones.component.scss']
})
export class PublicacionesComponent {

	origin: 'all' | 'myPublicaciones' | 'myCompras' = 'all';
	formFiltros: FormGroup;
	tipos_productos: any[] = [];

	loading: boolean = true;

	publicacionesToShow: PublicacionModel[] = [];
	filtros: any;

	filteredLocalidades: Observable<string[]>;
	localidades: string[] = [];
	allLocalidades: string[] = [];

	publicacionesCardList: CardModel[] = []
	filteredPublicacionesCardList: CardModel[] = []

	trueques: TruequeModel[] = [];
	screenWidth: number;

	userOrders: OrdenModel[] = [];

	constructor(private router: Router, private auth: AuthService, private fb: FormBuilder,
		private productosService: ProductosService, private showErrorService: ShowErrorService,
		private truequesService: TruequesService, private comprasService: ComprasService,
		private currencyPipe: CurrencyPipe, private logisticaService: LogisticaService){

		this.formFiltros = fb.group({
			fundacion: [''],
			localidad: [''],
			tipoProducto: ['']
		})

		if (router.url == '/mis-publicaciones') this.origin = 'myPublicaciones';
		if (router.url == '/mis-compras') this.origin = 'myCompras';

		this.filteredLocalidades = this.formFiltros.controls['localidad'].valueChanges.pipe(
			startWith(''),
			map((localidad: string | null) => (localidad ? this._filterLocalidad(localidad) : this.allLocalidades.slice())),
		);
		this.screenWidth = (window.innerWidth > 0) ? window.innerWidth : screen.width;
	}
	
	ngOnInit(): void {
		this.getTiposProductos();
		this.getLocalidades();
		this.filtrarPublicaciones()
	}

	addPublicacion(){
		//this.router.navigate(['form-publicacion'])
		if(this.auth.isUserLoggedIn) {
			this.router.navigate(['form-publicacion'])
		} else {
			Swal.fire({
				title: '¡Necesitás una cuenta!',
				text: 'Para poder crear una publicación, tenés que usar tu cuenta.',
				icon: 'warning',
				confirmButtonText: 'Iniciar sesión',
				showCancelButton: true,
				cancelButtonText: 'Cancelar'
			}).then(({isConfirmed}) => {
				if(isConfirmed) this.router.navigate(['login'])
			})
		}
	}

	getTiposProductos() {
		this.productosService.getTiposProductos().subscribe({
			next: (v: any) => this.tipos_productos = v,
			error: (e) => console.error('error', e)
		});
	}

	filtrarPublicaciones(event?:any) {
		console.log(event);
		
		this.loading = true;
		this.publicacionesToShow.splice(0)
		if(this.origin == 'all'){
			this.filtros = {};
			const tipoProducto = this.formFiltros.controls['tipoProducto'].value;

			if (this.localidades.length > 0) this.filtros['localidades'] = this.localidades;
			if (tipoProducto) this.filtros['tipoProducto'] = tipoProducto;

			console.log('filtro', this.filtros);

			this.truequesService.getPublicaciones(this.filtros).subscribe({
				next: (data: any) => {
					console.log(data);
					this.publicacionesToShow = data;
					this.publicacionesToShow.map(item => {
						item.parsedImagenes = item.imagenes.split('|')
					})
				}, complete: () => this.generateCardList(),
				error: ()=> this.loading = false
			})
		} else if(this.origin == 'myPublicaciones'){
			this.truequesService.getMisPublicaciones().subscribe({
				next: (data: any) => {
					console.log(data);
					this.publicacionesToShow = data;
					this.publicacionesToShow.map(item => {
						item.parsedImagenes = item.imagenes.split('|')
					})
					
				}, complete: () => {
					if(this.publicacionesToShow.length > 0) {
						this.truequesService.getTruequesParticular(this.publicacionesToShow[0].particularDTO.idParticular).subscribe({
							next: (res: any) => {
								this.trueques = res;
							}, complete: () => this.generateCardList()
						})
					} else this.loading = false;
				}, error: ()=> this.loading = false
			})
		} else { // myCompras
			this.logisticaService.obtenerMisOrdenes('publicaciones').subscribe({
				next: (res: any) => {
					if (res.length > 0) {
						this.userOrders = res;
					}
					this.comprasService.getMyCompras().subscribe({
						next: (data: any) => {
							console.log(data);
							for (const publicacion of data) {
								publicacion.publicacionDTO.idCompra = publicacion.idCompra;
								publicacion.publicacionDTO.estadoCompra = publicacion.estadoCompra;
								this.publicacionesToShow.push(publicacion.publicacionDTO);
							}
							this.publicacionesToShow.map(item => {
								item.parsedImagenes = item.imagenes.split('|')
							})
						}, complete: () => this.generateCardList(),
						error: ()=> this.loading = false
					})
				}
			})
		}
	}

	generateCardList() {
		this.publicacionesCardList.splice(0);
		this.publicacionesToShow.sort((a, b) => new Date(b.fechaPublicacion).getTime() - new Date(a.fechaPublicacion).getTime());
		const auxList: CardModel[] = [];
		for (const publicacion of this.publicacionesToShow) {

			let idPublicacionOrigen: number | undefined = undefined;
			let idPublicacionPropuesta: number | undefined = undefined;
			
			if(publicacion.estadoPublicacion == 'CERRADA') {
				const trueque = this.trueques.find(item => item.estadoTrueque == 'APROBADO' && item.publicacionDTOpropuesta.idPublicacion == publicacion.idPublicacion)
				if(trueque != undefined) idPublicacionOrigen = trueque.publicacionDTOorigen.idPublicacion
				else {
					const trueque = this.trueques.find(item => item.estadoTrueque == 'APROBADO' && item.publicacionDTOorigen.idPublicacion == publicacion.idPublicacion)
					if(trueque) idPublicacionPropuesta = trueque.publicacionDTOpropuesta.idPublicacion
				}
			}

			const matchingOrders = this.userOrders.some(order => order.publicacionId == publicacion.idPublicacion);

			auxList.push({
				id: publicacion.idPublicacion,
				imagen: publicacion.parsedImagenes? publicacion.parsedImagenes[0] : 'no_image',
				titulo: publicacion.titulo,
				valorPrincipal: `${this.currencyPipe.transform(publicacion.valorTruequeMin)} - ${this.currencyPipe.transform(publicacion.valorTruequeMax)}`,
				valorSecundario: !!publicacion.particularDTO.accessToken ? (publicacion.precioVenta ? `${this.currencyPipe.transform(publicacion.precioVenta)}` : undefined) : undefined,
				fecha: publicacion.fechaPublicacion,
				usuario: {
					id: publicacion.particularDTO.usuarioDTO.idUsuario,
					imagen: publicacion.particularDTO.usuarioDTO.avatar,
					nombre: publicacion.particularDTO.nombre + ' ' + publicacion.particularDTO.apellido,
					puntaje: publicacion.particularDTO.puntaje,
					localidad: publicacion.particularDTO.direcciones[0].localidad
				},
				action: !!idPublicacionOrigen || !!idPublicacionPropuesta ? 'trueque' : this.origin == 'myPublicaciones' ? 'list' : 'access',
				idAuxiliar: !!idPublicacionOrigen ? idPublicacionOrigen : !!idPublicacionPropuesta ? publicacion.idPublicacion : publicacion.idCompra ? publicacion.idCompra : undefined,
				buttons: this.getButtonsForCard(publicacion, !!idPublicacionOrigen || !!idPublicacionPropuesta, matchingOrders),
				estado: this.origin == 'myPublicaciones' ? publicacion.estadoPublicacion : publicacion.estadoCompra ? publicacion.estadoCompra : undefined,
				codigo: publicacion.idCompra ? 'Compra' : this.origin != 'all' ? !!publicacion.estadoEnvio ? 'Venta' : 'Publicación' : 'Publicación',
				estadoAux: publicacion.estadoEnvio
			})
		}
		this.publicacionesCardList = auxList;
		this.filteredPublicacionesCardList = this.publicacionesCardList;
		this.loading = false;
	}

	getButtonsForCard(publicacion: PublicacionModel, truequeAprobado: boolean = false, matchingOrders: boolean = false): CardButtonModel[] {
		if(this.origin == 'myPublicaciones' && !publicacion.idCompra && !publicacion.estadoEnvio) {
			const list: CardButtonModel[] = [{ name: !truequeAprobado ? '¿Dónde lo propuse?' : 'Ver trueque', icon: 'info', color: 'primary', status: 'INFO', action: !truequeAprobado ? 'list' : 'navigate'}]
			if (publicacion.estadoPublicacion == 'ABIERTA') list.push({name: 'Cerrar publicación', icon: 'close', color: 'warn', status: 'CERRADA', action: 'change_status'});
			return list;
		} /* else if(this.origin == 'myPublicaciones' && publicacion.estadoEnvio) {
			if(publicacion.estadoEnvio == 'EN_ENVIO') return [{name: 'Ver envío', icon: 'local_shipping', color: 'info', status: 'INFO'}];
			else return []
		} */ 
		else if (this.origin == 'myCompras'){
			console.log(publicacion);
			if(publicacion.estadoEnvio == 'RECIBIDA') return [];
			else if(matchingOrders) return [{name: 'Ver envío', icon: 'local_shipping', color: 'info', status: 'INFO', action: 'ver_envio'}];
			else return [{name: 'Configurar envío', icon: 'local_shipping', color: 'info', status: 'INFO', action: 'configurar_envio'}];
		} else return [];
	}

	limpiarFiltros() {
		this.formFiltros.reset()
		this.localidades = [];
		this.filtrarPublicaciones()
	}

	/* FUNCIONES PARA FILTRO DE LOCALIDADES */
	getLocalidades() {
		const apiUrl = 'https://apis.datos.gob.ar/georef/api/localidades?orden=id&provincia=02&max=50&campos=nombre'
		fetch(apiUrl).then(response => response.json()).then(data => {
			for (const localidad of data.localidades) {
				this.allLocalidades.push(localidad.nombre)
			}
		}).catch(error => console.error(error));
	}

	add(event: MatChipInputEvent): void {
		const value = (event.value || '').trim();
		if (value && this.allLocalidades.some(item => item.toUpperCase() == value.toUpperCase())) {
			this.localidades.push(value);
		}
		event.chipInput!.clear();
		this.formFiltros.controls['localidad'].setValue(null);
		console.log('add',this.formFiltros.controls['localidad'].value);
	}

	remove(localidad: string): void {
		const index = this.localidades.indexOf(localidad);
		if (index >= 0) this.localidades.splice(index, 1);
	}

	selected(event: MatAutocompleteSelectedEvent): void {
		this.localidades.push(event.option.viewValue);
		this.formFiltros.controls['localidad'].setValue('');
	}

	private _filterLocalidad(value: string): string[] {
		const filterValue = value.toLowerCase();
		return this.allLocalidades.filter(localidad => localidad.toLowerCase().includes(filterValue));
	}

	filterByStatus(event: any) {
		const status = event.value == 'open' ? 'ABIERTA' : event.value == 'closed' ? 'CERRADA' : ['ABIERTA', 'CERRADA'];
		this.filteredPublicacionesCardList = this.publicacionesCardList.filter(item => item.estado && status.includes(item.estado) )
	}

}
