import { Injectable } from '@angular/core';
import { AuthService } from '../services/auth.service';
import { webSocket, WebSocketSubject, WebSocketSubjectConfig } from 'rxjs/webSocket';

@Injectable({ providedIn: 'root' })
export class WebSocketInterceptor {
  private wsSubject: WebSocketSubject<any>;
  private idUsuarioEmisor: number;

  constructor(private authService: AuthService) {
    this.idUsuarioEmisor = this.authService.getUserID();
    this.wsSubject = new WebSocketSubject('ws://localhost:8080/chat-socket');
  }

  connect(url: string): WebSocketSubject<any> {
    const wsConfig: WebSocketSubjectConfig<any> = {
      url: `${url}?idUsuarioEmisor=${this.idUsuarioEmisor}`,
      protocol: 'chat-protocol',
      closeObserver: { next: () => console.log('Conexión WebSocket cerrada.') },
      openObserver: { next: () => console.log('Conexión WebSocket abierta.') },
      deserializer: (e) => e, 
      serializer: (value) => value, 
      binaryType: 'blob',
    };

    console.log(`${url}?id=${this.idUsuarioEmisor}`);

    this.wsSubject = webSocket(wsConfig);

    return this.wsSubject;
  }
}
