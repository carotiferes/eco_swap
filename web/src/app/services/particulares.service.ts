import { Injectable } from '@angular/core';
import { HttpBackEnd } from './httpBackend.service';

const URL_NAME = 'URImsUsuarios'

@Injectable({
	providedIn: 'root'
})
export class ParticularesService {

	constructor(private backendService: HttpBackEnd) { }

	getParticular(id_particular: any) {
		return this.backendService.get(URL_NAME, 'api/particular/' + id_particular);
	}
}
