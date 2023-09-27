import { Injectable } from '@angular/core';
import { HttpBackEnd } from './httpBackend.service';

const URL_NAME = 'URImsUsuarios'

@Injectable({
	providedIn: 'root'
})
export class FundacionesService {

	constructor(private backendService: HttpBackEnd) { }

	getFundacion(id_fundacion: any) {
		return this.backendService.get(URL_NAME, 'fundacion/' + id_fundacion);
	}

	getFundaciones() {
		return this.backendService.get(URL_NAME, 'fundaciones');
	}
}
