import { Component } from '@angular/core';
import { PerfilModel } from 'src/app/models/perfil.model';
import { AuthService } from 'src/app/services/auth.service';
import { UsuarioService } from 'src/app/services/usuario.service';
const db = require('../../data/db.json')

@Component({
  selector: 'app-perfil',
  templateUrl: './perfil.component.html',
  styleUrls: ['./perfil.component.scss']
})
export class PerfilComponent {
	isSwapper: boolean = true; // CHANGE WHEN BRINGING USER INFO
	user: PerfilModel;
	userInfo: any;

	columns: number = 2;
	colspan: number = 1;
	screenWidth: number = 0;

	accordions: {
		title: string,
		icon: string,
		attributes: {title: string, name: string, value: any}[],
		exclusive?: 'swapper' | 'fundacion'
	}[] = []

	userData: any;

	constructor(private auth: AuthService, private usuarioService: UsuarioService){
		// TODO: GET USER INFO
		/* this.userData = auth.getUserData().userData;
		usuarioService.getUser(this.userData.id_perfil).subscribe(res => {
			console.log(res);
			
		}) */
		this.user = db.perfiles[this.isSwapper ? 0 : 1];
		this.userInfo = this.isSwapper ? db.swappers[0] : db.fundaciones[0];
		this.configureColumns();
	}
	
	configureColumns(){
		this.screenWidth = (window.innerWidth > 0) ? window.innerWidth : screen.width;
		this.accordions = [{
			title: 'Información Básica',
			icon: 'person',
			attributes: [
				{title: 'Usuario', name: 's_usuario', value: this.user.username},
				{title: 'Contraseña', name: 's_contrasena', value: this.user.password},
				{title: 'Nombre', name: 's_nombre', value: this.userInfo.s_nombre},
				{title: 'Apellido', name: 's_apellido', value: this.userInfo.s_apellido}
			]
		}, {
			title: 'Información de Contacto',
			icon: 'contacts',
			attributes: [
				{title: 'Email', name: 's_email', value: this.user.email},
				{title: 'Teléfono', name: 's_telefono', value: this.user.telefono},
			]
		}, {
			title: 'Información de Swapper',
			icon: 'social_distance',
			attributes: [
				{title: 'Fecha de Nacimiento', name: 'f_nacimiento', value: this.userInfo.f_nacimiento},
				{title: 'Puntaje', name: 'n_puntaje', value: this.user.puntaje},
			],
			exclusive: 'swapper'
		}, {
			title: 'Información de la Fundación',
			icon: 'diversity_2',
			attributes: [
				{title: 'Fecha de Nacimiento', name: 'f_nacimiento', value: this.userInfo.f_nacimiento},
				{title: 'Puntaje', name: 'n_puntaje', value: this.user.puntaje},
			],
			exclusive: 'fundacion'
		}]
	}

	showableAccordions(){
		return this.accordions.filter(item => 
			(this.isSwapper && item.exclusive == 'swapper') || (!this.isSwapper && item.exclusive == 'fundacion') || !item.exclusive
		)
	}
}
