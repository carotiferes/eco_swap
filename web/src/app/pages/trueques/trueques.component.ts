import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/services/auth.service';
import Swal from 'sweetalert2';

@Component({
	selector: 'app-trueque',
	templateUrl: './trueques.component.html',
	styleUrls: ['./trueques.component.scss']
})
export class TruequesComponent {

	userData: any;

	constructor(private router: Router, private auth: AuthService){
		this.userData = auth.getUserData();
	}

	addPublicacion(){
		this.router.navigate(['form-publicacion'])
		//TODO: cuando en el local storage esté la info del user, chequear inicio de sesion:
		/* if(Object.keys(this.userData).length != 0) {
			this.router.navigate(['form-publicacion'])
		} else {
			Swal.fire({
				title: '¡Necesitás una cuenta!',
				text: 'Para poder crear una publicación, tenés que usar tu cuenta.',
				icon: 'warning',
				confirmButtonText: 'Iniciar sesión',
				showCancelButton: true,
				cancelButtonText: 'Cancelar'
			}).then(({isConfirmed}) => {
				if(isConfirmed) this.router.navigate(['login'])
			})
		} */
	}
}
