import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { HttpBackEnd } from './httpBackend.service';

const URL_NAME = 'URImsAutenticacion'

@Injectable({
	providedIn: 'root'
})
export class UsuarioService {

	constructor(private backendService: HttpBackEnd) { }

	getUser(id_perfil: number) {
		return this.backendService.get(URL_NAME, 'perfil/' + id_perfil);
	}

	createUser(user: any){
		return this.backendService.post(URL_NAME, 'ms-autenticacion/api/v1/usuario/signin', user);
	}
}
