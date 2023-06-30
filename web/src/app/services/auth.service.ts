import { Injectable } from '@angular/core';
import { Router } from '@angular/router';

@Injectable({
	providedIn: 'root'
})
export class AuthService {

	isUserLoggedIn: boolean;

	constructor(private router: Router) {
		if(this.getUserLogin()) this.isUserLoggedIn = true;
		else this.isUserLoggedIn = false;
	}

	login(username: string, password: string) {
		console.log(username, password);
		// TODO: CHANGE FROM HARDCODED DATA TO CALL BACKEND (access token)
		this.setLocalStorage('userData', JSON.stringify({
			userData: {
				username,
				idUser: 1,
				isSwapper: username == 'swapper' ? true : false
			}
		}));
		this.isUserLoggedIn = true;
		this.setUserLoggedIn()
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
