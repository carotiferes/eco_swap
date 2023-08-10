import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { HttpBackEnd } from './httpBackend.service';

const URL_NAME = 'URImsAutenticacion'

@Injectable({
	providedIn: 'root'
})
export class UsuarioService {

	constructor(private backendService: HttpBackEnd) { }

	getCurrentUser() {
		return this.backendService.get(URL_NAME, 'perfil/');
	}

	createUser(user: any){ // SIGNUP
		return this.backendService.post(URL_NAME, 'ms-autenticacion/api/v1/usuario/signup', user);
	}
}
