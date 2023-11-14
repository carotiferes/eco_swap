import { Injectable } from '@angular/core';
import { webSocket, WebSocketSubject } from 'rxjs/webSocket';
import { AuthService } from './auth.service';
import { environment } from 'src/environments/environment';

@Injectable({
	providedIn: 'root'
})
export class WebSocketService {
	private socket$: WebSocketSubject<any>;
	private idUsuarioEmisor: number;

	constructor(private authService: AuthService) {
		this.idUsuarioEmisor = this.authService.getUserID();
		this.socket$ = webSocket(environment.webSocket + this.idUsuarioEmisor);
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
