import { Injectable } from '@angular/core';
import { webSocket, WebSocketSubject } from 'rxjs/webSocket';
import { AuthService } from './auth.service';

@Injectable({
	providedIn: 'root'
})
export class WebSocketService {
	private socket$: WebSocketSubject<any>;
	private idUsuarioEmisor: number;

	constructor(private authService: AuthService) {
		this.idUsuarioEmisor = this.authService.getUserID();
		this.socket$ = webSocket('ws://localhost:8080/chat-socket?idUsuarioEmisor=' + this.idUsuarioEmisor);
	}

	connect() {
		return this.socket$;
	}

	sendMessage(body: { idTrueque: number, mensaje: string, usuarioReceptor: number, fechaHoraEnvio: Date }) {
		this.socket$.next(body);
	}

	close() {
		this.socket$.complete();
	}
}
