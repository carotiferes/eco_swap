import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FundacionModel } from 'src/app/models/fundacion.model';
import { UsuarioModel } from 'src/app/models/usuario.model';
import { DonacionModel } from 'src/app/models/donacion.model';
import { ColectaModel } from 'src/app/models/colecta.model';
import { AuthService } from 'src/app/services/auth.service';
import { DonacionesService } from 'src/app/services/donaciones.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-colecta',
  templateUrl: './colecta.component.html',
  styleUrls: ['./colecta.component.scss']
})
export class ColectaComponent {

	id_colecta: string = '';
	colecta?: ColectaModel;
	fundacion?: FundacionModel;
	usuario?: UsuarioModel;
	donaciones: DonacionModel[] = [];

	userData?: any;
	loading: boolean = true;

	buttonsCard: {name: string, icon: string, color: string, status: string, disabled: string}[] = []

	constructor(private route: ActivatedRoute, private router: Router, private auth: AuthService,
		private donacionesService: DonacionesService){
		route.paramMap.subscribe(params => {
			console.log(params);
			this.id_colecta = params.get('id_colecta') || '';
		})
	}
	
	ngOnInit(): void {
		this.userData = this.auth.getUserData();
		console.log(this.userData);
		this.donacionesService.getColecta(this.id_colecta).subscribe((res: any) => {
			console.log(res);

			this.colecta = res;
			this.fundacion = res.fundacion
			this.usuario = res.fundacion.usuario
			if(this.usuario) this.usuario.puntaje = Number(this.usuario?.puntaje)
			console.log(this.fundacion, this.usuario, this.colecta);

			if(this.colecta) this.colecta.imagen = this.getImage(this.colecta.imagen)
			this.loading = false;

			/* TODO: usar esto si cambiamos la logica y traemos x separado las donaciones
			this.donacionesService.getDonaciones(this.id_colecta).subscribe((resDonaciones: any) => {
				console.log(resDonaciones);
				this.donaciones = resDonaciones
			}) */
			if(this.colecta){
				for (const prod of this.colecta.productos) {
					for (const donacion of prod.donaciones) {
						donacion['nombreProducto'] = prod.descripcion;
					}
					this.donaciones.push(...prod.donaciones)
				}
				this.donaciones.map(donacion => {
					if(donacion.imagenes) donacion.parsedImagenes = donacion.imagenes.split('|')
				})
			}
			console.log(this.donaciones);
			if(this.userData.isSwapper){
				this.donaciones = this.donaciones.filter(item => item.particular.idParticular == this.userData.id_particular)
			}
		})
	}

	donar() {
		//console.log(Object.keys(this.userData).length === 0);
		
		if(Object.keys(this.userData).length != 0){
			Swal.fire({
				title: '¡Estás un paso más cerca de hacer tu donación!',
				text: 'Te vamos a pedir algunos datos de lo que vas a donar. Es importante que completes la información requerida para que la fundación conozca lo que vas a donar.', //'Seleccioná el producto que quieras donar',
				icon: 'info',
				confirmButtonText: '¡VAMOS!' //'Confirmar'
			}).then(({isConfirmed, value}) => {
				if(isConfirmed) this.router.navigate(['donacion/'+this.id_colecta])
			})
		} else {
			Swal.fire({
				title: 'Necesitás una cuenta!',
				text: 'Para poder donar, tenés que usar tu cuenta.',
				icon: 'warning',
				confirmButtonText: 'Iniciar sesión',
				showCancelButton: true,
				cancelButtonText: 'Cancelar'
			}).then(({isConfirmed}) => {
				if(isConfirmed) this.router.navigate(['login'])
			})
		}
	}

	zoomImage(img?: string){
		if(img){
			Swal.fire({
				html: `<img src="${this.getImage(img)}" style="width: 100%"/>`,
				showConfirmButton: false,
				showCloseButton: true
			})
		}
	}

	showDireccion(usuario: any){
		console.log( usuario);
		let stringDir: string = ''
		for (const dir of usuario.direcciones) {
			if(dir.direccion) {
				stringDir += (dir.direccion + ' '+ dir.altura)
			}
		}
		console.log(stringDir);

		const dirArray = stringDir.split(' ')
		Swal.fire({
			title: 'Información de la fundación',
			text: stringDir + ' https://www.google.com/maps/search/?api=1&query='+dirArray[0]+'+'+dirArray[1],
			html: `
			<p style="font-weight: 400;"><b>Email: </b>${usuario.email}</p>
			<p style="font-weight: 500;"><b>Dirección: </b>${stringDir}</p>
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
