import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FundacionModel } from 'src/app/models/fundacion.model';
import { ColectaModel } from 'src/app/models/colecta.model';
import { AuthService } from 'src/app/services/auth.service';
import { DonacionesService } from 'src/app/services/donaciones.service';
import { FundacionesService } from 'src/app/services/fundaciones.service';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Observable } from 'rxjs';
import { map, startWith } from 'rxjs/operators';
import { ProductosService } from 'src/app/services/productos.service';
import Swal from 'sweetalert2';
import { ShowErrorService } from 'src/app/services/show-error.service';

@Component({
	selector: 'app-colectas',
	templateUrl: './colectas.component.html',
	styleUrls: ['./colectas.component.scss']
})
export class ColectasComponent implements OnInit {

	colectas: ColectaModel[] = [];
	isMyColectas: boolean = false;
	showColectas: ColectaModel[] = [];
	userData: any;

	loading: boolean = true;

	formFiltros: FormGroup;
	tipos_productos: any[] = [];
	optionsFundaciones: any[] = [/* { idFundacion: 1, nombre: 'Tzedaka' }, { idFundacion: 2, nombre: 'Cruz Roja' } */];
	filteredOptions: Observable<any[]>;
	filtros: any = {};

	constructor(private router: Router, private auth: AuthService,
		private donacionesService: DonacionesService, private fundacionesService: FundacionesService,
		private fb: FormBuilder, private productosService: ProductosService,
		private showErrorService: ShowErrorService) {

		if (router.url == '/mis-colectas') this.isMyColectas = true;

		this.formFiltros = fb.group({
			fundacion: [''],
			codigoPostal: [''],
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
	}

	ngOnInit() {
		this.getFundaciones();
		this.getTiposProductos();

		this.filtrarColectas();
	}

	filtrarColectas() {
		this.loading = true;
		if (this.isMyColectas) {
			this.donacionesService.getMisColectas().subscribe({
				next: (res: any) => {
					this.colectas = res;
					this.showColectas = this.colectas;
					this.showColectas.map(item => {
						item.imagen = this.donacionesService.getImagen(item.imagen)
					})
					this.loading = false;
				},
				error: (error) => {
					console.log('error mis colectas', error);
					this.showErrorService.show('Ocurrió un error!', 'Ocurrió un error al traer las colectas de la fundación. Por favor volvé a intentarlo más tarde.')
					this.loading = false;
				}
			})
		} else {
			this.filtros = {};
			const idFundacion = this.formFiltros.controls['fundacion'].value ? this.formFiltros.controls['fundacion'].value.idFundacion : undefined;
			const codigoPostal = this.formFiltros.controls['codigoPostal'].value;
			const tipoProducto = this.formFiltros.controls['tipoProducto'].value;

			if (idFundacion) this.filtros['idFundacion'] = idFundacion;
			if (codigoPostal) this.filtros['codigoPostal'] = codigoPostal;
			if (tipoProducto) this.filtros['tipoProducto'] = tipoProducto;

			this.donacionesService.getAllColectas(this.filtros).subscribe({
				next: (res: any) => {
					this.colectas = res;
					this.showColectas = this.colectas;
					this.showColectas.map(item => {
						item.imagen = this.donacionesService.getImagen(item.imagen)
					})
					this.loading = false;
				},
				error: (error) => {
					console.log('error all colectas', error);
					this.showErrorService.show('Ocurrió un error!', 'Ocurrió un error al traer las colectas. Por favor volvé a intentarlo más tarde.')
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
				this.showErrorService.show('Error!', 'Ha ocurrido un error al traer los tipos de producto')
			},
			complete: () => console.info('complete')
		});
	}

	limpiarFiltros() {
		this.formFiltros.reset()
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
}
