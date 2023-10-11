import { Injectable } from '@angular/core';
import { HttpBackEnd } from './httpBackend.service';

const URL_NAME_USER = 'URImsUsuarios';
const URL_NAME_TRANSAC = 'URImsTransacciones';

@Injectable({
	providedIn: 'root'
})
export class ComprasService {

	constructor(private backendService: HttpBackEnd) { }

	getMyCompras() {
		return this.backendService.get(URL_NAME_USER, 'api/misCompras');
	}

	comprar(id_publicacion: number) {
		return this.backendService.post(URL_NAME_TRANSAC, `ms-transacciones/api/comprar/${id_publicacion}`, {});
	}
}
