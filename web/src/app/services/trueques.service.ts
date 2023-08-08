import { Injectable } from '@angular/core';
import { HttpBackEnd } from './httpBackend.service';

const URL_NAME = 'URImsUsuarios'

@Injectable({
	providedIn: 'root'
})
export class TruequesService {

	constructor(private backendService: HttpBackEnd) { }

	crearPublicacion(body: any){
		return this.backendService.post(URL_NAME, 'api/publicacion', body);
	}
}
