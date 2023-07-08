import { Injectable } from '@angular/core';
import { HttpBackEnd } from './httpBackend.service';

@Injectable({
	providedIn: 'root'
})
export class DonacionesService {

	constructor(private backendService: HttpBackEnd) { }

	crearSolicitud(body: any) {
		console.log(body);
		const test = this.backendService.post('api/solicitud', body);
		console.log('aaaa', test);
		
		return test
	}

	getSolicitudes(){
		return this.backendService.get('api/solicitudes');
	}

	getSolicitud(id_solicitud: any){
		return this.backendService.get('api/solicitud/'+id_solicitud);
	}
}
