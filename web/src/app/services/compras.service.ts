import { Injectable } from '@angular/core';
import { HttpBackEnd } from './httpBackend.service';

const URL_NAME = 'URImsUsuarios';

@Injectable({
	providedIn: 'root'
})
export class ComprasService {

	constructor(private backendService: HttpBackEnd) { }

	getMyCompras() {
		return this.backendService.get(URL_NAME, 'api/misCompras');
	}
}
