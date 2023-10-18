import { Injectable } from '@angular/core';
import { HttpBackEnd } from './httpBackend.service';

const URL_NAME = 'URImsUsuarios'

@Injectable({
	providedIn: 'root'
})
export class OpinionesService {

	constructor(private backendService: HttpBackEnd) { }

	getOpinionesByUserID(id_usuario: number) {
		return this.backendService.get(URL_NAME, `api/opiniones/usuario/${id_usuario}`);
	}

	getOpinion(id_opinion: number) {
		return this.backendService.get(URL_NAME, `api/opinion/${id_opinion}`);
	}

	getMyOpiniones() {
		return this.backendService.get(URL_NAME, `api/misOpiniones`);
	}

	crearOpinion(opinion: any) {
		return this.backendService.post(URL_NAME, `api/opiniones/crearOpinion`, opinion);
	}
}
