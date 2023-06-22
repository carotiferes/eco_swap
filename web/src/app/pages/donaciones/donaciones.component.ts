import { Component } from '@angular/core';
import { SolicitudModel } from 'src/app/models/solicitud.model';
const db = require('../../data/db.json')

@Component({
  selector: 'app-donaciones',
  templateUrl: './donaciones.component.html',
  styleUrls: ['./donaciones.component.scss']
})
export class DonacionesComponent {

	solicitudes: SolicitudModel[] = [];

	constructor(){
		this.solicitudes = db.solicitudes;
	}
}
