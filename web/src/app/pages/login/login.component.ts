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
	password: string = '';

	@ViewChild('user') userHtml: ElementRef | undefined;
	@ViewChild('password') passwordHtml: ElementRef | undefined;

	passwordIcon: string = 'visibility';
	passwordType: string = 'password';

	//TODO: loading on submit

	constructor(private auth: AuthService, private router: Router) { }

	ngOnInit(): void {
		if(this.auth.isUserLoggedIn){
			this.router.navigate([''])
		}
	}

	onSubmit() {
		this.username = this.userHtml?.nativeElement.value;
		this.password = this.passwordHtml?.nativeElement.value;

		if (!this.username || !this.password) {
			Swal.fire({ title: 'Campos incompletos!', text: 'Por favor completá todos los campos antes de continuar.', icon: 'error' })
		} else {
			this.auth.login(this.username, this.password) //TODO: then and catch
			//this.router.navigateByUrl('/colectas')
		}
	}

	createAccount(){
		this.router.navigate(['registro'])
	}

	goToHome(){
		this.router.navigate(['home'])
	}

	resetPassword(){
		this.router.navigate(['reset-password'])
	}

	togglePassword(){
		this.passwordType = this.passwordType === 'text' ? 'password' : 'text';
		this.passwordIcon = this.passwordIcon === 'visibility' ? 'visibility_off' : 'visibility';
	}
}
