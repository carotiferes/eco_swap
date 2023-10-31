import { Injectable } from '@angular/core';
import { HttpBackEnd } from './httpBackend.service';

const URL_NAME_USER = 'URImsUsuarios';

@Injectable({
	providedIn: 'root'
})
export class ChatService {

	constructor(private backendService: HttpBackEnd) { }

	getMyMensajes(id_trueque: any) {
		return this.backendService.get(URL_NAME_USER, `api/chat/${id_trueque}`);
	}

	sendMensaje(body: any) {
		return this.backendService.post(URL_NAME_USER, `api/chat`, body);
	}
}
