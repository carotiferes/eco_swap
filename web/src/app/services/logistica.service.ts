import { Injectable } from '@angular/core';
import { HttpBackEnd } from './httpBackend.service';

const URL_NAME = 'URImsUsuarios';

@Injectable({
	providedIn: 'root'
})
export class LogisticaService {

	constructor(private backendService: HttpBackEnd) { }

	getOrden(id_orden: any) {
		return this.backendService.get(URL_NAME, `ms-transacciones/api/logistica/orden/${id_orden}`);
	}

	getOrdenes() {
		return this.backendService.get(URL_NAME, `ms-transacciones/api/logistica/orden`);
	}

	/* ping() {
		return this.backendService.get(URL_NAME, `ms-transacciones/api/logistica/ping`);
	} */

	getCostoEnvio(peso: number) {
		return this.backendService.get(URL_NAME, `ms-transacciones/api/logistica/costoEnvio`, {peso});
	}

	crearOrden(body: any) {
		return this.backendService.post(URL_NAME, `ms-transacciones/api/logistica/orden`, body);
	}

	cambiarEstadoOrden(id_orden: any, nuevoEstado: any) {
		return this.backendService.put(URL_NAME, `ms-transacciones/api/logistica/orden/${id_orden}`, {nuevoEstado});
	}
}
