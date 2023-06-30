import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FundacionModel } from 'src/app/models/fundacion.model';
import { SolicitudModel } from 'src/app/models/solicitud.model';
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

	constructor(private router: Router, private route: ActivatedRoute){
		this.solicitudes = db.solicitudes;
		route.paramMap.subscribe(params => {
			this.idFundacion = params.get('id_fundacion') || undefined;
			this.fundacion = db.fundaciones.find((item: FundacionModel) => item.id_fundacion == Number(this.idFundacion))
			console.log(params, this.idFundacion);
		})
	}

	ngOnInit(): void {
		if(this.idFundacion){
			// TODO: FILTER DONACIONES
			this.solicitudesFundacion = this.solicitudes.filter(item => item.id_fundacion == Number(this.idFundacion))
			this.showSolicitudes = this.solicitudesFundacion
		} else {
			this.showSolicitudes = this.solicitudes;
		}
	}

	goToSolicitud(solicitud: SolicitudModel){
		this.router.navigateByUrl('solicitud/'+solicitud.id_solicitud)
	}
}
