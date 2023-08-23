import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { HttpBackEnd } from './httpBackend.service';
import Swal from 'sweetalert2';
import jwtDecode from 'jwt-decode';
import { UsuarioService } from './usuario.service';

const URL_NAME = 'URImsAutenticacion'

@Injectable({
	providedIn: 'root'
})
export class AuthService {

	isUserLoggedIn: boolean;
	userData: any;

	constructor(private router: Router, private backendService: HttpBackEnd, private usuarioService: UsuarioService) {
		if(this.getUserLogin()) this.isUserLoggedIn = true;
		else this.isUserLoggedIn = false;
	}

	login(username: string, password?: string) {
		this.backendService.patch(URL_NAME, 'ms-autenticacion/api/v1/usuario/login', {username, password}).subscribe({
			next: (v: any) => {
				console.log('response:', v);
				// TODO: GET USER INFO TO SAVE IN LOCAL STORAGE (AT LEAST IS_SWAPPER)
				this.setLocalStorage('userToken', v.token);
				const userData: any = jwtDecode(v.token)
				this.setLocalStorage('isSwapper', JSON.stringify(userData.esParticular));
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

	getUserID() {
		const tkn = localStorage.getItem('userToken')
		const decodedToken: any = jwtDecode(tkn || '')
		return decodedToken.id;
		/*const decodedToken: any = jwtDecode(tkn || '')
		this.usuarioService.getUserByID(decodedToken.id).subscribe({
				next: (res) => {
					this.userData = res;
					return this.userData;
				},
				error: (error) => {
					console.log(error);
					return this.userData;
				}
			})
			 if(tkn) {
		}
		else return this.userData; */
		/* const data = localStorage.getItem('userData');
		if(data && data != 'undefined'){
			const user = JSON.parse(data as string);
			return user;
		} else return {} */
	}

	getUserLogin() {
		return localStorage.getItem('userLoggedIn');
	}

	isUserSwapper() {
		const is = localStorage.getItem('isSwapper')
		if(is) return JSON.parse(is);
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
