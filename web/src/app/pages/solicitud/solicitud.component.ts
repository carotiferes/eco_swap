import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FundacionModel } from 'src/app/models/fundacion.model';
import { PerfilModel } from 'src/app/models/perfil.model';
import { PropuestaModel } from 'src/app/models/propuesta.model';
import { SolicitudModel } from 'src/app/models/solicitud.model';
import { AuthService } from 'src/app/services/auth.service';
import { DonacionesService } from 'src/app/services/donaciones.service';
import Swal from 'sweetalert2';
const db = require('../../data/db.json')

@Component({
  selector: 'app-solicitud',
  templateUrl: './solicitud.component.html',
  styleUrls: ['./solicitud.component.scss']
})
export class SolicitudComponent {

	id_solicitud: string = '';
	solicitud?: SolicitudModel;
	fundacion?: FundacionModel;
	perfil?: PerfilModel;
	propuestas: PropuestaModel[] = [];

	userData?: any;
	loading: boolean = true;

	constructor(private route: ActivatedRoute, private router: Router, private auth: AuthService,
		private donacionesService: DonacionesService){
		route.paramMap.subscribe(params => {
			console.log(params);
			this.id_solicitud = params.get('id_solicitud') || '';
		})
	}
	
	ngOnInit(): void {
		this.userData = this.auth.getUserData().userData
		console.log(this.userData);
		this.donacionesService.getSolicitud(this.id_solicitud).subscribe((res: any) => {
			console.log(res);
			this.solicitud = res;
			this.fundacion = res.fundacion
			this.perfil = res.fundacion.perfil
			if(this.perfil) this.perfil.puntaje = Number(this.perfil?.puntaje)
			this.loading = false;
			console.log(this.fundacion, this.perfil, this.solicitud);

		})
	}

	donar() {
		Swal.fire({
			title: '¡Estás un paso más cerca de hacer tu donación!',
			text: 'Te vamos a pedir algunos datos de lo que vas a donar. Es importante que completes la información requerida para que la fundación conozca lo que vas a donar.', //'Seleccioná el producto que quieras donar',
			icon: 'info',
			confirmButtonText: '¡VAMOS!' //'Confirmar'
		}).then(({isConfirmed, value}) => {
			this.router.navigate(['propuesta/'+this.id_solicitud])
		})
	}

	changeEstadoPropuesta(propuesta: PropuestaModel, status: string){
		this.propuestas.map(item => {
			if(item == propuesta) {
				if(item.estado != status) item.estado = status;
				else item.estado = 'NUEVA'
			}
		})
		//TODO: CHANGE STATUS IN BACKEND
	}

	zoomImage(img: string){
		Swal.fire({
			html: `<img src="${img}" style="width: 100%"/>`,
			showConfirmButton: false,
			showCloseButton: true
		})
	}

}
