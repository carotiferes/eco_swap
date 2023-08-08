import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { HttpBackEnd } from './httpBackend.service';
import Swal from 'sweetalert2';

const URL_NAME = 'URImsAutenticacion'

@Injectable({
	providedIn: 'root'
})
export class AuthService {

	isUserLoggedIn: boolean;

	possibleUsers = ['mromero', 'sgomez', 'tzedaka', 'cruzroja']

	constructor(private router: Router, private backendService: HttpBackEnd) {
		if(this.getUserLogin()) this.isUserLoggedIn = true;
		else this.isUserLoggedIn = false;
	}

	login(username: string, password?: string) {
		console.log(username, password);
		//return this.backendService.patch('/ms-autenticacion/api/v1/usuario/login', {username, password});

		// TODO: CHANGE FROM HARDCODED DATA TO CALL BACKEND (access token)
		let userData: any;
		this.backendService.patch(URL_NAME, 'ms-autenticacion/api/v1/usuario/login', {username, password}).subscribe({
			next: (v: any) => {
				console.log('next',v);
				//this.setLocalStorage('userData', JSON.stringify(userData));
				this.setLocalStorage('userToken', JSON.stringify(v));
				this.isUserLoggedIn = true;
				this.setUserLoggedIn();
				this.router.navigate([''])
			},
			error: (e) => {
				console.error('error',e);
				Swal.fire('¡Error!', 'Ocurrió un error. Por favor revisá los campos e intentá nuevamente.', 'error')
			},
			complete: () => console.info('complete') 
		})
	}

	get getPossibleUsers(){
		return this.possibleUsers
	}

	setLocalStorage(key: string, data: string) {
		localStorage.setItem(key, data);
	}

	setUserLoggedIn() {
		localStorage.setItem(
			'userLoggedIn',
			this.isUserLoggedIn ? 'true' : 'false'
		);
	}

	getUserData() {
		const data = localStorage.getItem('userData');
		console.log(data);
		
		if(data && data != 'undefined'){
			const user = JSON.parse(data as string);
			return user;
		} else return {}
	}

	getUserLogin() {
		return localStorage.getItem('userLoggedIn');
	}

	getUserToken(){
		const tkn = localStorage.getItem('userToken');
		console.log(tkn);
		
		return tkn;
	}

	logout() {
		this.isUserLoggedIn = false;
		localStorage.clear();
		this.router.navigate(['/login']);
		console.log(localStorage.getItem('userData'));
	}

	resetPassword(body: any){ /* body: { username: string, nuevoPassword: string, confirmarPassword: string } */
		return this.backendService.put(URL_NAME, 'ms-autenticacion/api/v1/usuario/password', body);
	}
}
