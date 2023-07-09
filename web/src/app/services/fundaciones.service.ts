import { Injectable } from '@angular/core';
import { HttpBackEnd } from './httpBackend.service';

@Injectable({
	providedIn: 'root'
})
export class FundacionesService {

	constructor(private backendService: HttpBackEnd) { }

	getFundacion(id_fundacion: any) {
		return this.backendService.get('fundacion/' + id_fundacion);
	}
}