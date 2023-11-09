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
	/* private wsSubject!: WebSocketSubject<any>;
  
	constructor(private websocketInterceptor: WebSocketInterceptor) { }
  
	sendMensaje(body: any) {
	  // Utiliza el interceptor para establecer la conexión y enviar el mensaje
	  console.log("Body del mensaje:" , body)
  
	  if (!this.wsSubject) {
		this.wsSubject = this.websocketInterceptor.connect('ws://localhost:8080/chat-socket');
  
		this.wsSubject.subscribe({
		  next: () => {
			console.log('Conexión WebSocket abierta desde WebSocketService.');
			// Una vez que la conexión esté abierta, puedes enviar el mensaje
			this.wsSubject.next(body);
		  },
		  error: (err: any) => {
			console.error('Error en la conexión WebSocket desde WebSocketService:', err);
		  },
		  complete: () => {
			console.log('Conexión WebSocket cerrada desde WebSocketService.');
		  }
		});
	  } else {
		// Si la conexión ya está establecida, simplemente envía el mensaje
		this.wsSubject.next(body);
	  }
	} */
}
