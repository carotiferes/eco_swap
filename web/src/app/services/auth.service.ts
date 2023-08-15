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

	constructor(private router: Router, private backendService: HttpBackEnd) {
		if(this.getUserLogin()) this.isUserLoggedIn = true;
		else this.isUserLoggedIn = false;
	}

	login(username: string, password?: string) {
		this.backendService.patch(URL_NAME, 'ms-autenticacion/api/v1/usuario/login', {username, password}).subscribe({
			next: (v: any) => {
				console.log('response:', v);
				// TODO: GET USER INFO TO SAVE IN LOCAL STORAGE (AT LEAST IS_SWAPPER)
				this.setLocalStorage('userToken', JSON.stringify(v.token));
				this.isUserLoggedIn = true;
				this.setUserLoggedIn();
				this.router.navigate([''])
			},
			error: (e) => {
				console.error('error:', e);
				Swal.fire('¡Error!', 'Ocurrió un error. Por favor revisá los campos e intentá nuevamente.', 'error')
			},
			complete: () => console.info('login complete') 
		})
	}

	setLocalStorage(key: string, data: string) {
		localStorage.setItem(key, data);
	}

	setUserLoggedIn() {
		localStorage.setItem('userLoggedIn', this.isUserLoggedIn ? 'true' : 'false');
	}

	getUserData() {
		const data = localStorage.getItem('userData');
		if(data && data != 'undefined'){
			const user = JSON.parse(data as string);
			return user;
		} else return {}
	}

	getUserLogin() {
		return localStorage.getItem('userLoggedIn');
	}

	getUserToken(){
		return localStorage.getItem('userToken');
	}

	logout() {
		this.isUserLoggedIn = false;
		localStorage.clear();
		this.router.navigate(['/login']);
	}

	resetPassword(body: any){ /* body: { username: string, nuevoPassword: string, confirmarPassword: string } */
		return this.backendService.put(URL_NAME, 'ms-autenticacion/api/v1/usuario/password', body);
	}
}
