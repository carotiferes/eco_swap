import { Component } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { MatAutocompleteSelectedEvent } from '@angular/material/autocomplete';
import { MatChipInputEvent } from '@angular/material/chips';
import { Router } from '@angular/router';
import { Observable, map, startWith } from 'rxjs';
import { CardModel } from 'src/app/models/card.model';
import { PublicacionModel } from 'src/app/models/publicacion.model';
import { TruequeModel } from 'src/app/models/trueque.model';
import { AuthService } from 'src/app/services/auth.service';
import { ComprasService } from 'src/app/services/compras.service';
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

	constructor(private router: Router, private auth: AuthService, private fb: FormBuilder,
		private productosService: ProductosService, private showErrorService: ShowErrorService,
		private truequesService: TruequesService, private comprasService: ComprasService){

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

	filtrarPublicaciones() {
		this.loading = true;
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
					this.truequesService.getTruequesParticular(this.publicacionesToShow[0].particularDTO.idParticular).subscribe({
						next: (res: any) => {
							this.trueques = res;
						}, complete: () => this.generateCardList()
					})
				}, error: ()=> this.loading = false
			})
		} else { // myCompras
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
	}

	generateCardList() {
		this.publicacionesCardList.splice(0)
		const auxList: CardModel[] = [];
		for (const publicacion of this.publicacionesToShow) {

			let idPublicacionOrigen: number | undefined;
			if(publicacion.estadoPublicacion == 'CERRADA') {
				const trueque = this.trueques.find(item => item.estadoTrueque == 'APROBADO' && item.publicacionDTOpropuesta.idPublicacion == publicacion.idPublicacion)
				if(trueque) idPublicacionOrigen = trueque.publicacionDTOorigen.idPublicacion
			}

			auxList.push({
				id: publicacion.idPublicacion,
				imagen: publicacion.parsedImagenes? publicacion.parsedImagenes[0] : 'no_image',
				titulo: publicacion.titulo,
				valorPrincipal: `$${publicacion.valorTruequeMin} - $${publicacion.valorTruequeMax}`,
				valorSecundario: !!publicacion.particularDTO.accessToken ? (publicacion.precioVenta ? `$${publicacion.precioVenta}` : undefined) : undefined,
				fecha: publicacion.fechaPublicacion,
				usuario: {
					id: publicacion.particularDTO.usuarioDTO.idUsuario,
					imagen: publicacion.particularDTO.usuarioDTO.avatar,
					nombre: publicacion.particularDTO.nombre + ' ' + publicacion.particularDTO.apellido,
					puntaje: publicacion.particularDTO.puntaje,
					localidad: publicacion.particularDTO.direcciones[0].localidad
				},
				action: !!idPublicacionOrigen ? 'trueque' : this.origin == 'myPublicaciones' ? 'list' : 'access',
				idAuxiliar: !!idPublicacionOrigen ? idPublicacionOrigen : publicacion.idCompra ? publicacion.idCompra : undefined,
				buttons: [],
				estado: this.origin == 'myPublicaciones' ? publicacion.estadoPublicacion : publicacion.estadoCompra ? publicacion.estadoCompra : undefined,
				codigo: publicacion.idCompra ? 'Compra' : 'Publicación'
			})
		}
		this.publicacionesCardList = auxList;
		this.filteredPublicacionesCardList = this.publicacionesCardList;
		this.loading = false;
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
