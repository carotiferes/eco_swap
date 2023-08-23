import { Component } from '@angular/core';
import { UsuarioModel } from 'src/app/models/usuario.model';
import { AuthService } from 'src/app/services/auth.service';
import { UsuarioService } from 'src/app/services/usuario.service';
const db = require('../../data/db.json')

@Component({
  selector: 'app-perfil',
  templateUrl: './perfil.component.html',
  styleUrls: ['./perfil.component.scss']
})
export class PerfilComponent {
	//isSwapper: boolean = true; // CHANGE WHEN BRINGING USER INFO
	user!: UsuarioModel;

	columns: number = 2;
	colspan: number = 1;
	screenWidth: number = 0;

	accordions: {
		title: string,
		icon: string,
		attributes: {title: string, name: string, value: any}[],
		exclusive?: 'particular' | 'fundacion'
	}[] = []

	userData: any;

	constructor(private auth: AuthService, private usuarioService: UsuarioService){
		this.getUserInformation();
		this.userData = { isSwapper: auth.isUserSwapper() }
	}

	getUserInformation(){
		this.usuarioService.getUserByID(this.auth.getUserID()).subscribe({
			next: (res: any) => {
				console.log(res);
				this.user = res;
				this.configureColumns();

			},
			error: (error) => {
				console.log('error', error);
				
			}
		})
	}
	
	configureColumns(){
		this.screenWidth = (window.innerWidth > 0) ? window.innerWidth : screen.width;
		this.accordions = [{
			title: 'Información Básica',
			icon: 'person',
			attributes: [
				{title: 'Usuario', name: 's_usuario', value: this.user.username},
				{title: 'Contraseña', name: 's_contrasena', value: this.user.password},
				/* {title: 'Nombre', name: 's_nombre', value: this.user.s_nombre},
				{title: 'Apellido', name: 's_apellido', value: this.user.s_apellido} */
			]
		}, {
			title: 'Información de Contacto',
			icon: 'contacts',
			attributes: [
				{title: 'Email', name: 's_email', value: this.user.email},
				{title: 'Teléfono', name: 's_telefono', value: this.user.telefono},
			]
		}, {
			title: 'Información de Particular',
			icon: 'social_distance',
			attributes: [
				//{title: 'Fecha de Nacimiento', name: 'f_nacimiento', value: this.user.f_nacimiento},
				{title: 'Puntaje', name: 'n_puntaje', value: this.user.puntaje},
			],
			exclusive: 'particular'
		}, {
			title: 'Información de la Fundación',
			icon: 'diversity_2',
			attributes: [
				//{title: 'Fecha de Nacimiento', name: 'f_nacimiento', value: this.user.f_nacimiento},
				{title: 'Puntaje', name: 'n_puntaje', value: this.user.puntaje},
			],
			exclusive: 'fundacion'
		}]
	}

	showableAccordions(){
		return this.accordions.filter(item => 
			(this.userData.isSwapper && item.exclusive == 'particular') || (!this.userData.isSwapper && item.exclusive == 'fundacion') || !item.exclusive
		)
	}
}
