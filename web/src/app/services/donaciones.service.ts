import { Injectable } from '@angular/core';
import { HttpBackEnd } from './httpBackend.service';

const URL = 'http://localhost:8080/'
const URL_NAME = 'URImsUsuarios'

@Injectable({
	providedIn: 'root'
})
export class DonacionesService {

	constructor(private backendService: HttpBackEnd) { }

	crearColecta(body: any) {
		return this.backendService.post(URL_NAME, 'api/colecta', body);
	}

	getAllColectas(filtros?: any){
		return this.backendService.get(URL_NAME, 'api/colectas', filtros);
	}

	getMisColectas(){ // TODO: url de mis COLECTAS (x tkn)
		return this.backendService.get(URL_NAME, 'api/colectas/102');
	}

	getColecta(id_colecta: any){
		return this.backendService.get(URL_NAME, 'api/colecta/'+id_colecta);
	}

	getDonacionesColecta(id_colecta: any){
		return this.backendService.get(URL_NAME, 'api/colecta/'+id_colecta+'/donaciones');
	}

	getMisDonaciones(){ // TODO: url de mis donaciones (x tkn)
		return this.backendService.get(URL_NAME, 'api/1/donaciones');
	}

	crearDonacion(id_colecta: number, body: any){
		return this.backendService.post(URL_NAME, 'api/colecta/'+id_colecta+'/donaciones', body);
	}

	getImagen(img: string){
		return URL + 'api/getImage/' + img;
	}

	cambiarEstadoDonacion(id_colecta: any, id_donacion: number, body: any){
		return this.backendService.put(URL_NAME, 'api/colecta/'+id_colecta+'/donaciones/'+id_donacion, body);
	}
}
