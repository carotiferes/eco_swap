import { Injectable } from '@angular/core';
import { HttpBackEnd } from './httpBackend.service';
import { WebSocketService } from './webSocket.service';

const URL_NAME_USER = 'URImsUsuarios';

@Injectable({
  providedIn: 'root'
})
export class ChatService {

  constructor(private webSocketService: WebSocketService, private backendService: HttpBackEnd) { }

  getMyMensajes(id_trueque: any) {
    return this.backendService.get(URL_NAME_USER, `api/chat/${id_trueque}`);
  }

  sendMensaje(body: any) {
    this.webSocketService.sendMessage(body);
  }
}
