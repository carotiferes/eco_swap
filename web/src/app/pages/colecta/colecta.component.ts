import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FundacionModel } from 'src/app/models/fundacion.model';
import { UsuarioModel } from 'src/app/models/usuario.model';
import { DonacionModel } from 'src/app/models/donacion.model';
import { ColectaModel } from 'src/app/models/colecta.model';
import { AuthService } from 'src/app/services/auth.service';
import { DonacionesService } from 'src/app/services/donaciones.service';
import Swal from 'sweetalert2';
import { ShowErrorService } from 'src/app/services/show-error.service';
import { FundacionesService } from 'src/app/services/fundaciones.service';
import { ProductosService } from 'src/app/services/productos.service';
import { UsuarioService } from 'src/app/services/usuario.service';

@Component({
  selector: 'app-colecta',
  templateUrl: './colecta.component.html',
  styleUrls: ['./colecta.component.scss']
})
export class ColectaComponent {

	id_colecta: string = '';
	colecta!: ColectaModel;
	donaciones: DonacionModel[] = [];

	userData?: any;
	userInfo?: any;
	loading: boolean = true;
	showDonaciones: boolean = false;
	donacionesToShow: DonacionModel[] = [];

	buttonsCard: {name: string, icon: string, color: string, status: string, disabled: string}[] = []

	constructor(private route: ActivatedRoute, private router: Router, private auth: AuthService,
		private donacionesService: DonacionesService, private showErrorService: ShowErrorService,
		private productoService: ProductosService, private usuarioService: UsuarioService){
		route.paramMap.subscribe(params => {
			console.log(params);
			this.id_colecta = params.get('id_colecta') || '';
			if(!this.id_colecta) showErrorService.show('Error!', 'No pudimos encontrar la información de la colecta que seleccionaste, por favor volvé a intentarlo más tarde.')
		})
		this.userData = { isSwapper: auth.isUserSwapper(), isLoggedIn: auth.isUserLoggedIn }
		usuarioService.getUserByID(auth.getUserID()).subscribe({
			next: (res: any) => {
				this.userInfo = res;
				console.log(this.userInfo);
				
			}
		})
	}
	
	ngOnInit(): void {
		this.getColecta();
	}

	getColecta(){
		this.donacionesService.getColecta(this.id_colecta).subscribe({
			next: (colecta: any) => {
				if (colecta) {
					console.log(colecta);
					
					this.colecta = colecta;

					this.colecta.imagen = this.getImage(this.colecta.imagen);

					this.productoService.getProductosColecta(colecta.idColecta).subscribe({
						next: (res: any) => {
							console.log(res);
							
							colecta.productos = res;
						},
						error: (error) => {
							console.log('error', error);
							
						}
					})

					if(this.auth.isUserLoggedIn){
						this.donacionesService.getDonacionesColecta(this.colecta.idColecta).subscribe({
							next: (donaciones: any) => {
								console.log(donaciones);
								this.donaciones = donaciones;
								this.donaciones.map(donacion => {
									if(donacion.imagenes) donacion.parsedImagenes = donacion.imagenes.split('|')
								})

								//TODO: REVISAR CUANDO MUESTRA DONACIONES
								if (this.userData.isSwapper) {
									this.donacionesToShow = this.donaciones.filter(item => item.particularDTO.idParticular == this.userInfo.particularDTO.idParticular)
									this.showDonaciones = true;
								} else if(this.userData) { // TODO: IF colecta.id_fundacion == userData.id_fundacion --> show donaciones
									this.donacionesToShow = donaciones;
									this.showDonaciones = true;
								}
								console.log(this.showDonaciones);
							},
							error: (error) => {
								console.log('error', error);
								
							}
						})
					}
					this.loading = false;
				} else this.showErrorService.show('Error!', 'No se encontró la información de la colecta que seleccionaste. Intentá nuevamente más tarde.')
			},
			error: (error) => {
				console.log(error);
				this.showErrorService.show('Error!', 'Ocurrió un error al traer la información de la colecta que seleccionaste. Intentá nuevamente más tarde.')
			}
		})
	}

	donar() {
		if(this.auth.isUserLoggedIn){
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
				title: '¡Necesitás una cuenta!',
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
