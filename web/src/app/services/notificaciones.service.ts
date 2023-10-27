import { Injectable } from '@angular/core';
import { HttpBackEnd } from './httpBackend.service';

const URL_NAME = 'URImsUsuarios';

@Injectable({
	providedIn: 'root'
})
export class NotificacionesService {

	constructor(private backendService: HttpBackEnd) { }

	getMisNotificaciones() {
		return this.backendService.get(URL_NAME, `api/misNotificaciones`);
	}

	leerNotificaciones(idNotificaciones: number[]) {
		return this.backendService.put(URL_NAME, '	api/notificacion', {idNotificaciones});

	}
}
