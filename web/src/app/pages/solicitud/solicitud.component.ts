import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FundacionModel } from 'src/app/models/fundacion.model';
import { PerfilModel } from 'src/app/models/perfil.model';
import { SolicitudModel } from 'src/app/models/solicitud.model';
import Swal from 'sweetalert2';
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

	constructor(private route: ActivatedRoute, private router: Router){
		route.paramMap.subscribe(params => {
			console.log(params);
			this.id_solicitud = params.get('id_solicitud') || '';
		})
		this.solicitud = db.solicitudes.find((item: SolicitudModel) => item.id_solicitud.toString() == this.id_solicitud)
		this.fundacion = db.fundaciones.find((item: FundacionModel) => item.id_fundacion == this.solicitud.id_fundacion)
		this.perfil = db.perfiles.find((item: PerfilModel) => item.id_perfil == this.fundacion.id_perfil)
	}

	donar() {
		Swal.fire({
			title: '¡Estás un paso más cerca de hacer tu donación!',
			text: 'Te vamos a pedir algunos datos de lo que vas a donar. Es importante que completes la información requerida para que la fundación conozca lo que vas a donar.', //'Seleccioná el producto que quieras donar',
			//input: 'select',
			//inputOptions: [...this.solicitud.productos.map(item => item.s_descripcion)],
			//iconHtml: `<span class="material-icons" style="font-size: 50px; color: var(--primary-color)">add</span>`,
			icon: 'info',
			confirmButtonText: '¡VAMOS!' //'Confirmar'
		}).then(({isConfirmed, value}) => {
			//let producto = this.solicitud.productos[value];
			this.router.navigate(['propuesta/'+this.id_solicitud],/* {queryParams: {id: producto.id_producto}} */)
		})
	}
}
