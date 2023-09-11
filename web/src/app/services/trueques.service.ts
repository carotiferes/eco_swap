import { Injectable } from '@angular/core';
import { HttpBackEnd } from './httpBackend.service';
import { environment } from 'src/environments/environment';

//const URL = environment.apiUrl + '8080/';
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
		return this.backendService.get(URL_NAME, 'api/publicacioness', filtros);
	}

	getMisPublicaciones(){
		return this.backendService.get(URL_NAME, 'api/misPublicaciones');
	}

	getPublicacion(id_publicacion: number){
		return this.backendService.get(URL_NAME, 'api/publicacion/' + id_publicacion);
	}

	getImagen(img: string){
		return this.backendService.getUrlByName(URL_NAME) + 'api/getImage/' + img;
	}
}
