import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FundacionModel } from 'src/app/models/fundacion.model';
import { SolicitudModel } from 'src/app/models/solicitud.model';
import { AuthService } from 'src/app/services/auth.service';
import { DonacionesService } from 'src/app/services/donaciones.service';
const db = require('../../data/db.json')

@Component({
  selector: 'app-donaciones',
  templateUrl: './donaciones.component.html',
  styleUrls: ['./donaciones.component.scss']
})
export class DonacionesComponent implements OnInit {

	solicitudes: SolicitudModel[] = [];
	fundacion?: FundacionModel;

	idFundacion?: string;
	solicitudesFundacion: SolicitudModel[] = []

	showSolicitudes: SolicitudModel[] = [];
	userData: any;

	constructor(private router: Router, private route: ActivatedRoute, private auth: AuthService,
		private donacionesService: DonacionesService){
		//this.solicitudes = db.solicitudes;
		route.paramMap.subscribe(params => {
			this.idFundacion = params.get('id_fundacion') || undefined;
			this.fundacion = db.fundaciones.find((item: FundacionModel) => item.idFundacion == Number(this.idFundacion))
			console.log(params, this.idFundacion);
		})

		this.userData = auth.getUserData().userData

	}
	
	ngOnInit() {
		this.donacionesService.getSolicitudes().subscribe((res:any) => {
			console.log(res);
			this.solicitudes = res;
			this.showSolicitudes = this.solicitudes;

		})
		/* if(this.idFundacion){
			// TODO: FILTER DONACIONES
			this.solicitudesFundacion = this.solicitudes.filter(item => item.fundacion.idFundacion == Number(this.idFundacion))
			this.showSolicitudes = this.solicitudesFundacion
		} else {
			this.showSolicitudes = this.solicitudes;
		} */

	}

	goToSolicitud(solicitud: SolicitudModel){
		this.router.navigateByUrl('solicitud/'+solicitud.idSolicitud)
	}

	addSolicitud(){
		this.router.navigateByUrl('form-solicitud')
		
	}
}
