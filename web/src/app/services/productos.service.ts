import { Injectable } from '@angular/core';
import { HttpBackEnd } from './httpBackend.service';

@Injectable({
	providedIn: 'root'
})
export class ProductosService {

	constructor(private backendService: HttpBackEnd) { }

	getTiposProductos() {
		return this.backendService.get('api/tiposProductos');
	}
}
