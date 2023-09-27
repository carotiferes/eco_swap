import { Injectable } from '@angular/core';
import { HttpBackEnd } from './httpBackend.service';

const URL_NAME = 'URImsUsuarios'

@Injectable({
	providedIn: 'root'
})
export class ProductosService {

	constructor(private backendService: HttpBackEnd) { }

	getTiposProductos() {
		return this.backendService.get(URL_NAME, 'api/tiposProductos');
	}

	getProductosColecta(id_colecta: number) {
		return this.backendService.get(URL_NAME, 'api/productos/'+id_colecta);
	}
}
