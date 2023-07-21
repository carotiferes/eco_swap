import { Injectable } from '@angular/core';
import { HttpBackEnd } from './httpBackend.service';

const URL = 'http://localhost:8080/'

@Injectable({
	providedIn: 'root'
})
export class DonacionesService {

	constructor(private backendService: HttpBackEnd) { }

	crearColecta(body: any) {
		console.log(body);
		const test = this.backendService.post('api/colecta', body);
		console.log('aaaa', test);

		return test
	}

	getColectas(){
		return this.backendService.get('api/colectas');
	}

	getColecta(id_colecta: any){
		return this.backendService.get('api/colecta/'+id_colecta);
	}

	getDonaciones(id_colecta: any){
		return this.backendService.get('/api/colecta/'+id_colecta+'/donaciones');
	}

	crearDonacion(id_colecta: number, body: any){
		return this.backendService.post('api/colecta/'+id_colecta+'/donaciones', body);
	}

	getImagen(img: string){
		return URL + 'api/getImage/' + img;
	}

	cambiarEstadoDonacion(id_colecta: number, id_donacion: number, body: any){
		return this.backendService.put('api/colecta/'+id_colecta+'/donaciones/'+id_donacion, body);
	}
}
