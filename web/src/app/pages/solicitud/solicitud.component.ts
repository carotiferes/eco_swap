import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FundacionModel } from 'src/app/models/fundacion.model';
import { PerfilModel } from 'src/app/models/perfil.model';
import { PropuestaModel } from 'src/app/models/propuesta.model';
import { SolicitudModel } from 'src/app/models/solicitud.model';
import { AuthService } from 'src/app/services/auth.service';
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
	propuestas: PropuestaModel[] = [{
		id_propuesta: 1,
		id_producto: 1,
		id_swapper: 1,
		descripcion: 'Frazadas',
		estado: 'NUEVA',
		cantidad_propuesta: 2,
		swapper: {
			id_swapper: 1,
			id_perfil: 1,
			f_nacimiento: new Date(),
			s_nombre: 'Swapper',
			s_apellido: 'Test'
		},
		imagenes: ['assets/test_prop/alim_no_perec.jpg', 'assets/test_prop/gatito1.jpeg']
	}, {
		id_propuesta: 1,
		id_producto: 1,
		id_swapper: 1,
		descripcion: 'Frazadas',
		estado: 'NUEVA',
		cantidad_propuesta: 2,
		swapper: {
			id_swapper: 1,
			id_perfil: 1,
			f_nacimiento: new Date(),
			s_nombre: 'Swapper',
			s_apellido: 'Test'
		},
		imagenes: ['']
	}];

	userData?: any;
	//imgBase64: string = ''
	modal: any;

	constructor(private route: ActivatedRoute, private router: Router, private auth: AuthService){
		route.paramMap.subscribe(params => {
			console.log(params);
			this.id_solicitud = params.get('id_solicitud') || '';
		})
		this.solicitud = db.solicitudes.find((item: SolicitudModel) => item.id_solicitud.toString() == this.id_solicitud)
		this.fundacion = db.fundaciones.find((item: FundacionModel) => item.id_fundacion == this.solicitud.id_fundacion)
		console.log(this.solicitud, this.fundacion);

		// TODO: REPLACE ALL THESE WITH CALL TO BACKEND WITH SOLICITUD ID TO GET DATA
		
		this.perfil = db.perfiles.find((item: PerfilModel) => item.id_perfil == this.fundacion.id_perfil)
		
		this.modal = document.getElementById("myModal");
	}
	
	ngOnInit(): void {
		this.userData = this.auth.getUserData().userData
		console.log(this.userData);
		
		//Called after the constructor, initializing input properties, and the first call to ngOnChanges.
		//Add 'implements OnInit' to the class.
		
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
