import { Component, ElementRef, OnInit, ViewChild, ViewEncapsulation } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FundacionModel } from 'src/app/models/fundacion.model';
import { ColectaModel } from 'src/app/models/colecta.model';
import { AuthService } from 'src/app/services/auth.service';
import { DonacionesService } from 'src/app/services/donaciones.service';
import { FundacionesService } from 'src/app/services/fundaciones.service';
import { FormBuilder, FormControl, FormGroup } from '@angular/forms';
import { Observable } from 'rxjs';
import { map, startWith } from 'rxjs/operators';
import { ProductosService } from 'src/app/services/productos.service';
import Swal from 'sweetalert2';
import { ShowErrorService } from 'src/app/services/show-error.service';
import { MatPaginator } from '@angular/material/paginator';
import { MatAutocompleteSelectedEvent } from '@angular/material/autocomplete';
import { MatChipInputEvent } from '@angular/material/chips';

@Component({
	selector: 'app-colectas',
	templateUrl: './colectas.component.html',
	styleUrls: ['./colectas.component.scss'],
	encapsulation: ViewEncapsulation.None
})
export class ColectasComponent implements OnInit {

	colectas: ColectaModel[] = [];
	isMyColectas: boolean = false;
	showColectas: ColectaModel[] = [];
	paginatedColectas: ColectaModel[] = [];
	userData: any;

	loading: boolean = true;

	formFiltros: FormGroup;
	tipos_productos: any[] = [];
	optionsFundaciones: any[] = [/* { idFundacion: 1, nombre: 'Tzedaka' }, { idFundacion: 2, nombre: 'Cruz Roja' } */];
	filteredOptions: Observable<any[]>;
	filtros: any = {};

	pageSize = 10;
	@ViewChild(MatPaginator) paginator!: MatPaginator;

	filteredLocalidades: Observable<string[]>;
	localidades: string[] = [];
	allLocalidades: string[] = [];

	@ViewChild('localidadInput') localidadInput!: ElementRef<HTMLInputElement>;

	constructor(private router: Router, private auth: AuthService,
		private donacionesService: DonacionesService, private fundacionesService: FundacionesService,
		private fb: FormBuilder, private productosService: ProductosService,
		private showErrorService: ShowErrorService) {

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
					this.colectas = res;
					this.showColectas = this.colectas.slice(0, this.pageSize);
					this.showColectas.map(item => {
						item.imagen = this.donacionesService.getImagen(item.imagen)
					})
					this.paginatedColectas = this.showColectas.slice(0, this.pageSize);
					this.loading = false;
				},
				error: (error) => {
					console.log('error mis colectas', error);
					//this.showErrorService.show('Ocurrió un error!', 'Ocurrió un error al traer las colectas de la fundación. Por favor volvé a intentarlo más tarde.')
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

			console.log('filtros', this.filtros, this.localidades);
			

			this.donacionesService.getAllColectas(this.filtros).subscribe({
				next: (res: any) => {
					this.colectas = res;
					this.showColectas = this.colectas;
					this.showColectas.map(item => {
						item.imagen = this.donacionesService.getImagen(item.imagen)
					})
					this.paginatedColectas = this.showColectas.slice(0, this.pageSize);
					this.loading = false;
				},
				error: (error) => {
					console.log('error all colectas', error);
					//this.showErrorService.show('Ocurrió un error!', 'Ocurrió un error al traer las colectas. Por favor volvé a intentarlo más tarde.')
					this.loading = false;
				}
			})
		}
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

	changePage(event: any) {
		const startIndex = event.pageIndex * event.pageSize;
		const endIndex = startIndex + event.pageSize;
		this.paginatedColectas = this.showColectas.slice(startIndex, endIndex);
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
		this.localidadInput.nativeElement.value = '';
		this.formFiltros.controls['localidad'].setValue(null);
	}

	private _filterLocalidad(value: string): string[] {
		const filterValue = value.toLowerCase();
		return this.allLocalidades.filter(localidad => localidad.toLowerCase().includes(filterValue));
	}
}

