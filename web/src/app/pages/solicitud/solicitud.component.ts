import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FundacionModel } from 'src/app/models/fundacion.model';
import { PerfilModel } from 'src/app/models/perfil.model';
import { SolicitudModel } from 'src/app/models/solicitud.model';
const db = require('../../data/db.json')

@Component({
  selector: 'app-solicitud',
  templateUrl: './solicitud.component.html',
  styleUrls: ['./solicitud.component.scss']
})
export class SolicitudComponent {

	id_solicitud: string = '';
	solicitud: SolicitudModel;
	fundacion: FundacionModel;
	perfil: PerfilModel;

	constructor(private route: ActivatedRoute){
		route.paramMap.subscribe(params => {
			console.log(params);
			this.id_solicitud = params.get('id_solicitud') || '';
		})
		this.solicitud = db.solicitudes.find((item: SolicitudModel) => item.id_solicitud.toString() == this.id_solicitud)
		this.fundacion = db.fundaciones.find((item: FundacionModel) => item.id_fundacion == this.solicitud.id_fundacion)
		this.perfil = db.perfiles.find((item: PerfilModel) => item.id_perfil == this.fundacion.id_perfil)
	}

	donar() {

	}
}
