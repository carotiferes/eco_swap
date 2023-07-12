import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FundacionModel } from 'src/app/models/fundacion.model';
import { PerfilModel } from 'src/app/models/perfil.model';
import { PropuestaModel } from 'src/app/models/propuesta.model';
import { SolicitudModel } from 'src/app/models/solicitud.model';
import { AuthService } from 'src/app/services/auth.service';
import { DonacionesService } from 'src/app/services/donaciones.service';
import Swal from 'sweetalert2';

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

	imagenTest: any;

	constructor(private route: ActivatedRoute, private router: Router, private auth: AuthService,
		private donacionesService: DonacionesService){
		route.paramMap.subscribe(params => {
			console.log(params);
			this.id_solicitud = params.get('id_solicitud') || '';
		})
	}
	
	ngOnInit(): void {
		this.userData = this.auth.getUserData();
		console.log(this.userData);
		this.donacionesService.getSolicitud(this.id_solicitud).subscribe((res: any) => {
			console.log(res);

			this.solicitud = res;
			this.fundacion = res.fundacion
			this.perfil = res.fundacion.perfil
			if(this.perfil) this.perfil.puntaje = Number(this.perfil?.puntaje)
			console.log(this.fundacion, this.perfil, this.solicitud);
			
			if(this.solicitud){
				for (const prod of this.solicitud.productos) {
					this.propuestas.push(...prod.propuestas)
				}
			}
			
			if(this.userData.isSwapper){
				this.propuestas = this.propuestas.filter(item => item.swapper.idSwapper == this.userData.id_swapper)
			}

			this.propuestas.forEach(propuesta => {
				propuesta.imagenes = propuesta.imagenes.split('|')
			})
			
			this.getImagen()
			this.loading = false;
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
				if(item.estadoPropuesta != status) item.estadoPropuesta = status;
				else item.estadoPropuesta = 'NUEVA'
			}
		})
		//TODO: CHANGE STATUS IN BACKEND
	}

	zoomImage(img: string){
		Swal.fire({
			html: `<img src="${this.getImage(img)}" style="width: 100%"/>`,
			showConfirmButton: false,
			showCloseButton: true
		})
	}

	getImagen(){
		if(this.solicitud) this.solicitud.imagen = this.donacionesService.getImagen(this.solicitud.imagen)
	}

	showDireccion(perfil: any){
		console.log( perfil.direcciones);
		let stringDir: string = ''
		for (const dir of perfil.direcciones) {
			if(dir.direccion) {
				stringDir += (dir.direccion + ' '+ dir.altura)
			}
		}
		console.log(stringDir);

		const dirArray = stringDir.split(' ')
		Swal.fire({
			title: 'Dirección de la fundación',
			text: stringDir + ' https://www.google.com/maps/search/?api=1&query='+dirArray[0]+'+'+dirArray[1],
			html: `
			<p style="font-weight: 500;">${stringDir}</p>
			<a href="https://www.google.com/maps/search/?api=1&query=${dirArray[0]}+${dirArray[1]}" target="_blank">Ver en Google Maps</a>`,
			//iconHtml: `<span class="material-icons-outlined"> place </span>`
			icon: 'info'
		})
	}

	getImage(image: any ){
		return this.donacionesService.getImagen(image)
	}

	isArray(item:any){
		return item.constructor === Array;
	}

}
