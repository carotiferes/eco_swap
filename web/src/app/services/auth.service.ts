import { Injectable } from '@angular/core';
import { Router } from '@angular/router';

@Injectable({
	providedIn: 'root'
})
export class AuthService {

	isUserLoggedIn: boolean;

	possibleUsers = ['mfernandez', 'sgomez', 'tzedaka', 'cruzroja']

	constructor(private router: Router) {
		if(this.getUserLogin()) this.isUserLoggedIn = true;
		else this.isUserLoggedIn = false;
	}

	login(username: string, password?: string) {
		console.log(username, password);
		// TODO: CHANGE FROM HARDCODED DATA TO CALL BACKEND (access token)
		let userData: any;
		if(username == 'mfernandez'){
			userData = {
				username,
				id_perfil: 1,
				id_swapper: 1,
				isSwapper: true
			}
		} else if(username == 'sgomez'){
			userData = {
				username,
				id_perfil: 4,
				id_swapper: 2,
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
		const user = JSON.parse(localStorage.getItem('userData') as string);
		return user;
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
}
