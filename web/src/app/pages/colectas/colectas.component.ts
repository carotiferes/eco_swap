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

	options: any[] = [{ name: 'Mary' }, { name: 'Shelley' }, { name: 'Igor' }];
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

		this.filteredOptions = this.formFiltros.controls['fundacion'].valueChanges.pipe(
			startWith(''),
			map(value => {
				const name = typeof value === 'string' ? value : value?.name;
				return name ? this._filter(name as string) : this.options.slice();
			}),
		);
	}

	ngOnInit() {
		this.filtrarColectas()

	}

	displayFn(fundacion: any): string {
		return fundacion && fundacion.name ? fundacion.name : '';
	}

	private _filter(name: string): any[] {
		const filterValue = name.toLowerCase();

		return this.options.filter(option => option.name.toLowerCase().includes(filterValue));
	}

	goToColecta(colecta: ColectaModel) {
		console.log(colecta);

		this.router.navigateByUrl('colecta/' + colecta.idColecta)
	}

	addColecta() {
		this.router.navigateByUrl('form-colecta')

	}

	filtrarColectas(){
		console.log(this.formFiltros.value);
		this.donacionesService.getColectas(this.filtros).subscribe((res: any) => {
			console.log(res);
			this.colectas = res;
			this.showColectas = this.colectas;

			this.showColectas.map(item => {
				item.imagen = this.donacionesService.getImagen(item.imagen)
			})
			if (this.idFundacion) {
				// TODO: FILTER DONACIONES
				this.colectasFundacion = this.colectas.filter(item => item.idFundacion == Number(this.idFundacion))
				this.showColectas = this.colectasFundacion
			} else {
				this.showColectas = this.colectas;
			}
			this.loading = false;
		})
	}
}
