import { Component, ElementRef, ViewChild } from '@angular/core';
import { FormGroup, FormBuilder } from '@angular/forms';
import { MatAutocompleteSelectedEvent } from '@angular/material/autocomplete';
import { MatChipInputEvent } from '@angular/material/chips';
import { MatPaginator } from '@angular/material/paginator';
import { Router } from '@angular/router';
import { Observable, map, startWith } from 'rxjs';
import { CardModel } from 'src/app/models/card.model';
import { PublicacionModel } from 'src/app/models/publicacion.model';
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

	loading: boolean = false;

	publicacionesToShow: PublicacionModel[] = [];
	filtros: any;

	filteredLocalidades: Observable<string[]>;
	localidades: string[] = [];
	allLocalidades: string[] = [];

	publicacionesCardList: CardModel[] = []

	@ViewChild('localidadInput') localidadInput!: ElementRef<HTMLInputElement>;

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
		if(this.origin == 'all'){
			this.filtros = {};
			//const localidad = this.formFiltros.controls['localidad'].value;
			const tipoProducto = this.formFiltros.controls['tipoProducto'].value;

			if (this.localidades.length > 0) this.filtros['localidades'] = this.localidades;
			if (tipoProducto) this.filtros['tipoProducto'] = tipoProducto;

			this.truequesService.getPublicaciones(this.filtros).subscribe({
				next: (data: any) => {
					console.log(data);
					this.publicacionesToShow = data;
					this.publicacionesToShow.map(item => {
						item.parsedImagenes = item.imagenes.split('|')
					})
					this.generateCardList()
				}
			})
		} else if(this.origin == 'myPublicaciones'){
			this.truequesService.getMisPublicaciones().subscribe({
				next: (data: any) => {
					console.log(data);
					this.publicacionesToShow = data;
					this.publicacionesToShow.map(item => {
						item.parsedImagenes = item.imagenes.split('|')
					})
					this.generateCardList(true)
				}
			})
		} else { // myCompras
			this.comprasService.getMyCompras().subscribe({
				next: (data: any) => {
					console.log(data);
					this.publicacionesToShow = data;
					this.publicacionesToShow.map(item => {
						item.parsedImagenes = item.imagenes.split('|')
					})
					this.generateCardList()
				}
			})
		}
	}

	generateCardList(my: boolean = false) {
		for (const publicacion of this.publicacionesToShow) {
			this.publicacionesCardList.push({
				id: publicacion.idPublicacion,
				imagen: publicacion.parsedImagenes? publicacion.parsedImagenes[0] : 'no_image',
				titulo: publicacion.titulo,
				valorPrincipal: `$${publicacion.valorTruequeMin} - $${publicacion.valorTruequeMax}`,
				valorSecundario: publicacion.precioVenta ? `$${publicacion.precioVenta}` : undefined,
				fecha: (new Date(publicacion.fechaPublicacion)).toLocaleDateString(),
				usuario: {
					imagen: 'assets/perfiles/perfiles-17.jpg',//publicacion.particularDTO.
					nombre: publicacion.particularDTO.nombre + ' ' + publicacion.particularDTO.apellido,
					puntaje: publicacion.particularDTO.puntaje,
					localidad: publicacion.particularDTO.direcciones[0].localidad
				},
				action: 'access',
				buttons: [],
				estado: my? publicacion.estadoPublicacion : undefined
			})
		}
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
	}

	remove(localidad: string): void {
		const index = this.localidades.indexOf(localidad);
		if (index >= 0) this.localidades.splice(index, 1);
	}

	selected(event: MatAutocompleteSelectedEvent): void {
		this.localidades.push(event.option.viewValue);
		this.localidadInput.nativeElement.value = '';
		this.formFiltros.controls['localidad'].setValue(null);
	}

	private _filterLocalidad(value: string): string[] {
		const filterValue = value.toLowerCase();
		return this.allLocalidades.filter(localidad => localidad.toLowerCase().includes(filterValue));
	}

}
