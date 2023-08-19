import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { HttpBackEnd } from './httpBackend.service';

const URL_NAME = 'URImsAutenticacion'

@Injectable({
	providedIn: 'root'
})
export class UsuarioService {

	constructor(private backendService: HttpBackEnd) { }

	getCurrentUser() { // TODO: CAMBIAR CUANDO TRAIGA X TKN
		return this.backendService.get(URL_NAME, 'ms-autenticacion/api/v1/usuario/103');
	}

	getUserByID(id: number) {
		return this.backendService.get(URL_NAME, 'ms-autenticacion/api/v1/usuario/'+id);
	}

	createUser(user: any){ // SIGNUP
		return this.backendService.post(URL_NAME, 'ms-autenticacion/api/v1/usuario/signup', user);
	}

	getTiposDocumentos() {
		return this.backendService.get(URL_NAME, 'ms-autenticacion/api/v1/tiposDocumentos');
	}
}
