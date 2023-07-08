import { Injectable } from '@angular/core';
import { HttpBackEnd } from './httpBackend.service';

const URL = 'http://localhost:8080/'

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

	crearPropuesta(id_solicitud: number, body: any){
		return this.backendService.post('api/solicitud'+id_solicitud+'/comunicarPropuesta', body);
	}

	getImagen(img: string){
		let url =  URL + 'api/getImage/' + img;
		console.log('bbbb',url);
		return url
	}
}
