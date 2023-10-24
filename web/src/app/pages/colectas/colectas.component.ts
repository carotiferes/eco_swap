import { DatePipe } from '@angular/common';
import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { MatAutocompleteSelectedEvent } from '@angular/material/autocomplete';
import { MatChipInputEvent } from '@angular/material/chips';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { map, startWith } from 'rxjs/operators';
import { CardModel } from 'src/app/models/card.model';
import { ColectaModel } from 'src/app/models/colecta.model';
import { AuthService } from 'src/app/services/auth.service';
import { DonacionesService } from 'src/app/services/donaciones.service';
import { FundacionesService } from 'src/app/services/fundaciones.service';
import { ProductosService } from 'src/app/services/productos.service';
import { ShowErrorService } from 'src/app/services/show-error.service';
import Swal from 'sweetalert2';

@Component({
	selector: 'app-colectas',
	templateUrl: './colectas.component.html',
	styleUrls: ['./colectas.component.scss'],
	encapsulation: ViewEncapsulation.None
})
export class ColectasComponent implements OnInit {

	//colectas: ColectaModel[] = [];
	isMyColectas: boolean = false;
	colectasToShow: ColectaModel[] = [];
	userData: any;

	loading: boolean = true;

	formFiltros: FormGroup;
	tipos_productos: any[] = [];
	optionsFundaciones: any[] = [/* { idFundacion: 1, nombre: 'Tzedaka' }, { idFundacion: 2, nombre: 'Cruz Roja' } */];
	filteredOptions: Observable<any[]>;
	filtros: any = {};

	colectasCardList: CardModel[] = [];

	filteredLocalidades: Observable<string[]>;
	localidades: string[] = [];
	allLocalidades: string[] = [];

	constructor(private router: Router, private auth: AuthService,
		private donacionesService: DonacionesService, private fundacionesService: FundacionesService,
		private fb: FormBuilder, private productosService: ProductosService,
		private showErrorService: ShowErrorService, private datePipe: DatePipe) {

		if (router.url == '/mis-colectas') this.isMyColectas = true;

		this.formFiltros = fb.group({
			fundacion: [''],
			localidad: [''],
			tipoProducto: ['']
		})

		this.filteredOptions = this.formFiltros.controls['fundacion'].valueChanges.pipe(
			startWith(''),
			map(value => {
				const nombre = typeof value === 'string' ? value : value?.nombre;
				return nombre ? this._filter(nombre as string) : this.optionsFundaciones.slice();
			}),
		);
		this.userData = { isSwapper: auth.isUserSwapper() }

		this.filteredLocalidades = this.formFiltros.controls['localidad'].valueChanges.pipe(
			startWith(''),
			map((localidad: string | null) => (localidad ? this._filterLocalidad(localidad) : this.allLocalidades.slice())),
		);
	}

	ngOnInit() {
		this.getFundaciones();
		this.getTiposProductos();
		this.getLocalidades();

		this.filtrarColectas();
	}

	filtrarColectas() {
		this.loading = true;
		if (this.isMyColectas) {
			this.donacionesService.getMisColectas().subscribe({
				next: (res: any) => {
					this.colectasToShow = res;
					console.log(this.colectasToShow);
				},
				error: (error) => {
					console.log('error mis colectas', error);
					//this.showErrorService.show('Ocurrió un error!', 'Ocurrió un error al traer las colectas de la fundación. Por favor volvé a intentarlo más tarde.')
					this.loading = false;
				}, complete: () => {
					this.generateCardList();
					this.loading = false;
				}
			})
		} else {
			this.filtros = {};
			const idFundacion = this.formFiltros.controls['fundacion'].value ? this.formFiltros.controls['fundacion'].value.idFundacion : undefined;
			const tipoProducto = this.formFiltros.controls['tipoProducto'].value;

			if (idFundacion) this.filtros['idFundacion'] = idFundacion;
			if (this.localidades.length > 0) this.filtros['localidades'] = this.localidades;
			if (tipoProducto) this.filtros['tipoProducto'] = tipoProducto;

			this.donacionesService.getAllColectas(this.filtros).subscribe({
				next: (res: any) => {
					this.colectasToShow = res;
				},
				error: (error) => {
					console.log('error all colectas', error);
					this.loading = false;
				}, complete: () => {
					this.generateCardList()
					this.loading = false;
				}
			})
		}
	}

	generateCardList() {
		console.log(this.colectasToShow);
		this.colectasCardList.splice(0)
		for (const colecta of this.colectasToShow) {
			let stringProductos = '';
			for (const [i, producto] of colecta.productos.entries()) {
				if(i==0) stringProductos = producto.descripcion
				else stringProductos += ' - '+producto.descripcion
			}
			this.colectasCardList.push({
				id: colecta.idColecta,
				imagen: colecta.imagen,
				titulo: colecta.titulo,
				valorPrincipal: stringProductos,
				fechaString: this.parseVigencia(colecta),
				usuario: {
					imagen: colecta.fundacionDTO.usuarioDTO.avatar,
					nombre: colecta.fundacionDTO.nombre,
					puntaje: colecta.fundacionDTO.puntaje,
					localidad: colecta.fundacionDTO.direcciones[0].localidad
				},
				action: 'access',
				buttons: []
			})
		}
	}

	parseVigencia(colecta: ColectaModel) {
		if(colecta.fechaInicio && colecta.fechaFin) {
			return 'Desde el ' + this.datePipe.transform(colecta.fechaInicio, 'dd/MM/yyyy') + ' hasta el ' + this.datePipe.transform(colecta.fechaFin, 'dd/MM/yyyy')
		} else if (colecta.fechaInicio) {
			return 'A partir del ' + this.datePipe.transform(colecta.fechaInicio, 'dd/MM/yyyy')
		} else return '';
	}

	addColecta() {
		if (this.auth.isUserLoggedIn) {
			this.router.navigateByUrl('form-colecta')
		} else {
			Swal.fire({
				title: '¡Necesitás una cuenta!',
				text: 'Para poder crear una colecta, tenés que usar la cuenta de una fundación.',
				icon: 'warning',
				confirmButtonText: 'Iniciar sesión',
				showCancelButton: true,
				cancelButtonText: 'Cancelar'
			}).then(({ isConfirmed }) => {
				if (isConfirmed) this.router.navigate(['login'])
			})
		}
	}

	getFundaciones() {
		this.fundacionesService.getFundaciones().subscribe((res: any) => {
			console.log(res);
			this.optionsFundaciones = res;
		})
	}

	getTiposProductos() {
		this.productosService.getTiposProductos().subscribe({
			next: (v: any) => {
				console.log('productos', v);
				this.tipos_productos = v;
			},
			error: (e) => {
				console.error('error', e);
				//this.showErrorService.show('Error!', 'Ha ocurrido un error al traer los tipos de producto')
			},
			complete: () => console.info('complete')
		});
	}

	getLocalidades() {
		const apiUrl = 'https://apis.datos.gob.ar/georef/api/localidades?orden=id&provincia=02&max=50&campos=nombre'
		fetch(apiUrl).then(response => response.json()).then(data => {
			for (const localidad of data.localidades) {
				this.allLocalidades.push(localidad.nombre)
			}
		}).catch(error => console.error(error));
	}

	limpiarFiltros() {
		this.formFiltros.reset();
		this.localidades = [];
		this.filtrarColectas()
	}

	displayFn(fundacion: any): string {
		return fundacion && fundacion.nombre ? fundacion.nombre : '';
	}

	private _filter(nombre: string): any[] {
		const filterValue = nombre.toLowerCase();
		return this.optionsFundaciones.filter(option => option.nombre.toLowerCase().includes(filterValue));
	}

	goToColecta(colecta: ColectaModel) {
		this.router.navigateByUrl('colecta/' + colecta.idColecta)
	}

	/* FUNCIONES PARA FILTRO DE LOCALIDADES */
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
		this.formFiltros.controls['localidad'].setValue('');
	}

	private _filterLocalidad(value: string): string[] {
		const filterValue = value.toLowerCase();
		return this.allLocalidades.filter(localidad => localidad.toLowerCase().includes(filterValue));
	}
}

