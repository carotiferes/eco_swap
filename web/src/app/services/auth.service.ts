import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { HttpBackEnd } from './httpBackend.service';

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
		/* this.backendService.patch(URL_NAME, 'ms-autenticacion/api/v1/usuario/login', {username, password}).subscribe({
			next: (v: any) => {
				console.log('next',v);
			},
			error: (e) => {
				console.error('error',e);
			},
			complete: () => console.info('complete') 
		}) */
		if(username == 'mromero'){
			userData = {
				username,
				id_perfil: 1,
				id_particular: 1,
				isSwapper: true
			}
		} else if(username == 'sgomez'){
			userData = {
				username,
				id_perfil: 4,
				id_particular: 2,
				isSwapper: true
			}
		} else if(username == 'tzedaka'){
			userData = {
				username,
				id_perfil: 2,
				id_fundacion: 1,
				isSwapper: false
			}
		} else {
			userData = {
				username,
				id_perfil: 3,
				id_fundacion: 2,
				isSwapper: false
			}
		}
		this.setLocalStorage('userData', JSON.stringify(userData));
		this.isUserLoggedIn = true;
		this.setUserLoggedIn()
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

	logout() {
		this.isUserLoggedIn = false;
		localStorage.clear();
		this.router.navigate(['/']);
		console.log(localStorage.getItem('userData'));
	}

	resetPassword(body: any){ /* body: { username: string, nuevoPassword: string, confirmarPassword: string } */
		return this.backendService.put(URL_NAME, 'ms-autenticacion/api/v1/usuario/password', body);
	}
}
