import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/services/auth.service';
import { UsuarioService } from 'src/app/services/usuario.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent {

	screenWidth: number;
	folder: string = 'banners';
	images: string[] = [];
	userData: any;

	constructor(private router: Router, private auth: AuthService){
		this.screenWidth = (window.innerWidth > 0) ? window.innerWidth : screen.width;
		if(this.screenWidth < 576) {
			this.folder = 'banners-mobile';
		}
		for (let index = 1; index <= 4; index++) {
			this.images.push(`assets/${this.folder}/0${index}.jpg`)
		}
		this.userData = {isLoggedIn: auth.isUserLoggedIn, isSwapper: auth.isUserSwapper()}
		console.log(this.userData);
		
	}

	goTo(page: string, restrict: boolean = false) {
		if(restrict && !this.userData.isLoggedIn) {
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
		} else this.router.navigateByUrl(page)
	}
}
