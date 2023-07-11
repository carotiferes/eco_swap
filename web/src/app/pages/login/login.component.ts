import { Component, ElementRef, ViewChild, ViewEncapsulation } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/services/auth.service';
import Swal from 'sweetalert2';

@Component({
	selector: 'app-login',
	templateUrl: './login.component.html',
	styleUrls: ['./login.component.scss'],
	encapsulation: ViewEncapsulation.None
})
export class LoginComponent {

	username: string = '';
	//password: string = '';

	@ViewChild('user') userHtml: ElementRef | undefined;
	//@ViewChild('password') passwordHtml: ElementRef | undefined;

	//TODO: loading on submit

	constructor(private auth: AuthService, private router: Router) { }

	onSubmit() {
		this.username = this.userHtml?.nativeElement.value;
		//this.password = this.passwordHtml?.nativeElement.value;
		const users = this.auth.getPossibleUsers
		if (!this.username /* || !this.password */) {
			Swal.fire({ title: 'Campos incompletos!', text: 'Por favor completá todos los campos antes de continuar.', icon: 'error' })
		} else {
			if(!users.includes(this.username)){
				Swal.fire({ title: 'Usuario inválido!', text: 'Por favor ingresá un usuario válido para continuar.', icon: 'error' })
			} else {
				this.auth.login(this.username/* , this.password */);
				//TODO: then and catch
				let userData = this.auth.getUserData()
	
				userData.isSwapper ? this.router.navigateByUrl('/donaciones') : this.router.navigateByUrl('/donaciones')
			}
		}
	}

	createAccount(){
		Swal.fire({
			title: '¡Te damos la bienvenida!',
			text: `Nos alegra que te quieras sumar a nuestra comunidad, para hacerlo, dejanos tu email
				y te mandaremos tu usuario para que puedas ingresar.`,
			icon: 'success',
			input: 'email'
		})
	}
}
