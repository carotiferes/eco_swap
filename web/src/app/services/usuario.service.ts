import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { HttpBackEnd } from './httpBackend.service';


@Injectable({
	providedIn: 'root'
})
export class UsuarioService {

	constructor(private backendService: HttpBackEnd) { }

	getUser(id_perfil: number) {
		return this.backendService.get('perfil/' + id_perfil);
	}
}
