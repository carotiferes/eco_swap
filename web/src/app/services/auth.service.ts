import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { HttpBackEnd } from './httpBackend.service';
import Swal from 'sweetalert2';
import jwtDecode from 'jwt-decode';
import { UsuarioService } from './usuario.service';
import { MatSnackBar } from '@angular/material/snack-bar';

const URL_NAME = 'URImsAutenticacion'

@Injectable({
	providedIn: 'root'
})
export class AuthService {

	isUserLoggedIn: boolean;
	userData: any;
	isUserValidated: boolean = false;

	constructor(private router: Router, private backendService: HttpBackEnd, private usuarioService: UsuarioService,
		private snackbar: MatSnackBar) {
		if(this.getUserLogin()) this.isUserLoggedIn = true;
		else this.isUserLoggedIn = false;
	}

	login(username: string, password?: string) {
		return new Promise<void>((resolve, reject) => {
			this.backendService.patch(URL_NAME, 'ms-autenticacion/api/v1/usuario/login', {username, password}).subscribe({
				next: (v: any) => {
					const userData: any = jwtDecode(v.token)
					if(userData.usuarioValidado) {
						this.setValidatedUser(v.token, userData)
					} else {
						Swal.fire({
							title: '¡Validá tu cuenta!',
							text: 'Ingresá el código que recibiste por mail para validar tu cuenta.',
							confirmButtonText: 'CONFIRMAR',
							showDenyButton: true,
							denyButtonText: 'Volver a enviar el mail',
							icon: 'info',
							input: 'text',
							reverseButtons: true,
							preDeny: () => {
								this.sendEmailAgain(userData.id).subscribe({
									next: (res) => {
										this.snackbar.open('Se envió el nuevo mail!', '', {
											horizontalPosition: 'center',
											verticalPosition: 'top',
											duration: 3000
										})
									}
								})
								return false;
							}
						}).then(({isConfirmed, value, isDenied}) => {
							if(isConfirmed) {
								this.usuarioService.confirmarCuenta(userData.id, value).subscribe({
									next: (res) => {
										Swal.fire('Excelente!', 'Tu cuenta fue verificada, ya podes usar Ecoswap!', 'success')
										this.setValidatedUser(v.token, userData)
									},
									error: (error) => console.log(error)
								})
							}
						})
					}
				},
				error: (e) => {
					console.error('error:', e);
					Swal.fire('¡Error!', e.message.descripcion/* 'Ocurrió un error. Por favor revisá los campos e intentá nuevamente.' */, 'error')
					reject(e)
				},
				complete: () => {
					console.info('login complete')
					resolve()
				} 
			})
		})
	}

	setValidatedUser(token: string, userData: any){
		this.setLocalStorage('userToken', token);
		this.isUserValidated = true;
		this.setLocalStorage('isSwapper', JSON.stringify(userData.esParticular));
		this.isUserLoggedIn = true;
		this.setUserLoggedIn();
		if(this.getUserID() == 999) this.router.navigate(['admin-logistica'])
		else this.router.navigate([''])
	}

	setLocalStorage(key: string, data: string) {
		localStorage.setItem(key, data);
	}

	setUserLoggedIn() {
		localStorage.setItem('userLoggedIn', this.isUserLoggedIn ? 'true' : 'false');
	}

	getUserID() {
		if(this.isUserLoggedIn){
			const tkn = localStorage.getItem('userToken')
			if(tkn) {
				const decodedToken: any = jwtDecode(tkn)
				return decodedToken.id;
			} else return null;
		} else return null;
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

	sendEmailAgain(id_usuario: number) {
		return this.backendService.patch(URL_NAME, `ms-autenticacion/api/v1/usuario/reenvio/${id_usuario}`, {});
	}
}
