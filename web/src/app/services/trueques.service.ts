import { Injectable } from '@angular/core';
import { HttpBackEnd } from './httpBackend.service';

const URL = 'http://localhost:8080/'
const URL_NAME = 'URImsUsuarios'

@Injectable({
	providedIn: 'root'
})
export class TruequesService {

	constructor(private backendService: HttpBackEnd) { }

	crearPublicacion(body: any){
		return this.backendService.post(URL_NAME, 'api/publicacion', body);
	}

	getPublicaciones(filtros: any = {}){
		return this.backendService.get(URL_NAME, 'api/publicaciones', filtros);
	}

	getImagen(img: string){
		return URL + 'api/getImage/' + img;
	}
}
