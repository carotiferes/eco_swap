import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { SolicitudModel } from 'src/app/models/solicitud.model';
const db = require('../../data/db.json')

@Component({
  selector: 'app-donaciones',
  templateUrl: './donaciones.component.html',
  styleUrls: ['./donaciones.component.scss']
})
export class DonacionesComponent {

	solicitudes: SolicitudModel[] = [];

	constructor(private router: Router){
		this.solicitudes = db.solicitudes;
	}

	goToSolicitud(solicitud: SolicitudModel){
		this.router.navigateByUrl('solicitud/'+solicitud.id_solicitud)
	}
}
