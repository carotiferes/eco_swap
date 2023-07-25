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
import { MatOptionSelectionChange } from '@angular/material/core';

const TIPOS = ['COLCHONES_Y_FRAZADAS', 'LIBROS', 'MUEBLES', 'OTROS', 'SALUD', 'TECNOLOGIA']

@Component({
	selector: 'app-colectas',
	templateUrl: './colectas.component.html',
	styleUrls: ['./colectas.component.scss']
})
export class ColectasComponent implements OnInit {

	colectas: ColectaModel[] = [];
	fundacion?: FundacionModel;

	idFundacion?: string;
	colectasFundacion: ColectaModel[] = []

	showColectas: ColectaModel[] = [];
	userData: any;

	loading: boolean = true;

	formFiltros: FormGroup;
	tipos_productos: string[];

	optionsFundaciones: any[] = [/* { idFundacion: 1, nombre: 'Tzedaka' }, { idFundacion: 2, nombre: 'Cruz Roja' } */];
	filteredOptions: Observable<any[]>;

	filtros: any = {};

	constructor(private router: Router, private route: ActivatedRoute, private auth: AuthService,
		private donacionesService: DonacionesService, private fundacionesService: FundacionesService,
		private fb: FormBuilder) {
		route.paramMap.subscribe(params => {
			this.idFundacion = params.get('id_fundacion') || undefined;
			console.log(params, this.idFundacion);
			if (this.idFundacion) {
				fundacionesService.getFundacion(this.idFundacion).subscribe((res: any) => {
					this.fundacion = res
					console.log(res);
				})
				console.log(this.fundacion);
			}
		});
		this.tipos_productos = TIPOS;

		this.formFiltros = fb.group({
			/* idFundacion: [''],
			nombreFundacion: [''], */
			fundacion: [''],
			codigoPostal: [''],
			tipoProducto: ['']
		})

		this.userData = auth.getUserData();

		this.getFundaciones()
		this.filteredOptions = this.formFiltros.controls['fundacion'].valueChanges.pipe(
			startWith(''),
			map(value => {
				const nombre = typeof value === 'string' ? value : value?.nombre;
				return nombre ? this._filter(nombre as string) : this.optionsFundaciones.slice();
			}),
		);
	}

	ngOnInit() {
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
		console.log(colecta);

		this.router.navigateByUrl('colecta/' + colecta.idColecta)
	}

	addColecta() {
		this.router.navigateByUrl('form-colecta')

	}

	filtrarColectas(hasFiltros: boolean = false){
		//if(!filtros) filtros = this.filtros
		console.log(this.formFiltros.value);
		this.loading = true;
		this.filtros = {};
		const idFundacion = this.formFiltros.controls['fundacion'].value ? this.formFiltros.controls['fundacion'].value.idFundacion : undefined;
		const codigoPostal = this.formFiltros.controls['codigoPostal'].value;
		const tipoProducto = this.formFiltros.controls['tipoProducto'].value;

		if(idFundacion) this.filtros['idFundacion'] = idFundacion;
		if(codigoPostal) this.filtros['codigoPostal'] = codigoPostal;
		if(tipoProducto) this.filtros['tipoProducto'] = tipoProducto;
		console.log(this.filtros);
		this.donacionesService.getColectas(this.filtros).subscribe((res: any) => {
			console.log(res);
			this.colectas = res;
			this.showColectas = this.colectas;

			this.showColectas.map(item => {
				item.imagen = this.donacionesService.getImagen(item.imagen)
			})
			if (this.idFundacion) {
				this.colectasFundacion = this.colectas.filter(item => item.idFundacion == Number(this.idFundacion))
				this.showColectas = this.colectasFundacion
			} else {
				this.showColectas = this.colectas;
			}
			this.loading = false;
		})
	}

	limpiarFiltros(){
		this.formFiltros.reset()
		this.filtrarColectas(false)
	}

	getFundaciones(){
		this.fundacionesService.getFundaciones().subscribe((res: any) => {
			console.log(res);
			this.optionsFundaciones = res;
		})
	}
}
