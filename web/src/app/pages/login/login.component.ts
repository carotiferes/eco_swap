import { Component, ElementRef, ViewChild, ViewEncapsulation } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/services/auth.service';
import Swal from 'sweetalert2';
import { Location } from '@angular/common';

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

	passwordIcon: string = 'visibility_off';
	passwordType: string = 'password';
	loadingSave: boolean = false;

	//TODO: loading on submit

	constructor(private auth: AuthService, private router: Router, private location: Location) {}

	ngOnInit(): void {
		this.loadingSave = false;
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
			this.loadingSave = true;
			this.auth.login(this.username, this.password).then(res => {
				this.loadingSave = false;
			}).catch(()=>this.loadingSave = false)
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
