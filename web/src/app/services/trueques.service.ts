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
		return this.backendService.get(URL_NAME, 'api/publicaciones', filtros);
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

	crearTrueque(idPublicacionOrigen: number, idPublicacionPropuesta: number){
		return this.backendService.post(URL_NAME, 'api/trueque', {idPublicacionOrigen, idPublicacionPropuesta});
	}

	getTruequesFromPublicacion(idPublicacionOrigen: number){
		return this.backendService.get(URL_NAME, `api/publicacion/${idPublicacionOrigen}/trueques`);
	}

	getMisPublicacionesForTrueque(idPublicacionOrigen: number){
		return this.backendService.get(URL_NAME, `api/publicacion/${idPublicacionOrigen}/trueques`);
	}

	cambiarEstadoTrueque(id_trueque: number, nuevoEstado: string){
		return this.backendService.put(URL_NAME, `api/trueque/${id_trueque}`, {nuevoEstado});
	}
}
