import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FundacionModel } from 'src/app/models/fundacion.model';
import { ColectaModel } from 'src/app/models/colecta.model';
import { AuthService } from 'src/app/services/auth.service';
import { DonacionesService } from 'src/app/services/donaciones.service';
import { FundacionesService } from 'src/app/services/fundaciones.service';

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

	constructor(private router: Router, private route: ActivatedRoute, private auth: AuthService,
		private donacionesService: DonacionesService, private fundacionesService: FundacionesService){
		route.paramMap.subscribe(params => {
			this.idFundacion = params.get('id_fundacion') || undefined;
			console.log(params, this.idFundacion);
			if(this.idFundacion){
				fundacionesService.getFundacion(this.idFundacion).subscribe((res:any) => {
					this.fundacion = res
					console.log(res);
				})
				console.log(this.fundacion);
			}
		})
		
		this.userData = auth.getUserData();
	}
	
	ngOnInit() {
		this.donacionesService.getColectas().subscribe((res:any) => {
			console.log(res);
			this.colectas = res;
			this.showColectas = this.colectas;

			this.showColectas.map(item => {
				item.imagen = this.donacionesService.getImagen(item.imagen)
			})
			if(this.idFundacion){
				// TODO: FILTER DONACIONES
				this.colectasFundacion = this.colectas.filter(item => item.idFundacion == Number(this.idFundacion))
				this.showColectas = this.colectasFundacion
			} else {
				this.showColectas = this.colectas;
			}
			this.loading = false;
		})

	}

	goToColecta(colecta: ColectaModel){
		this.router.navigateByUrl('colecta/'+colecta.idSolicitud)
	}

	addColecta(){
		this.router.navigateByUrl('form-colecta')
		
	}
}
