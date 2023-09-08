import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { UsuarioModel } from 'src/app/models/usuario.model';
import { AuthService } from 'src/app/services/auth.service';
import { UsuarioService } from 'src/app/services/usuario.service';

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
		//exclusive?: 'particular' | 'fundacion'
	}[] = []

	userData: any;

	constructor(private auth: AuthService, private usuarioService: UsuarioService, private router: Router){
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

	edit() {
		this.router.navigate(['edit-perfil'])
	}
	
	configureColumns(){
		this.screenWidth = (window.innerWidth > 0) ? window.innerWidth : screen.width;
	}
}
