import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { HttpBackEnd } from './httpBackend.service';


@Injectable({
	providedIn: 'root'
})
export class UsuarioService {

	constructor(private backendService: HttpBackEnd) { }

	test() {
		return this.backendService.post('api/solicitud', {});
	}
}
