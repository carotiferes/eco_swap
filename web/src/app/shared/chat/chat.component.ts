import { DOCUMENT } from '@angular/common';
import { Component, EventEmitter, Inject, Input, Optional, Output } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { TruequeModel } from 'src/app/models/trueque.model';
import { ChatService } from 'src/app/services/chat.service';
import { WebSocketService } from 'src/app/services/webSocket.service';

@Component({
	selector: 'app-chat',
	templateUrl: './chat.component.html',
	styleUrls: ['./chat.component.scss']
})
export class ChatComponent {
	@Input() mensajes: any[] = [];
	@Input() userData: any;
	@Input() elOtroSwapper: any; 
	@Input() trueques: TruequeModel[] = []
	@Input() userType: any
	@Input() publicacion: any

	@Output() sentMessage = new EventEmitter<any>();

	nuevoMensaje: string = '';
	origin: 'modal' | 'inline' = 'inline';
	initChat = 0;

	constructor(@Optional() @Inject(MAT_DIALOG_DATA) public data: any, private chatService: ChatService,
	@Inject(DOCUMENT) private document: Document, @Optional() public dialogRef: MatDialogRef<ChatComponent>,
	private websocketService: WebSocketService) {
		if(data) {
			this.mensajes = data.mensajes;
			this.userData = data.userData;
			this.elOtroSwapper = data.elOtroSwapper;
			this.trueques = data.trueques;
			this.userType = data.userType;
			this.publicacion = data.publicacion;
			this.origin = 'modal'
		}

		
		if(this.origin == 'inline') { // si lo conecto siempre, se llama 2 veces por estar en la misma pagina
			websocketService.connect().subscribe({
				next: (message) => {
					console.log('message subscription', message, this.origin);
					this.mensajes.push(message);
				},
				error: (error) => {
				  console.error('WebSocket error:', error);
				}
			});
		}
	}

	ngOnDestroy() {
		this.websocketService.close();
	}

	scrollToBottom() {
		const id = this.origin == 'inline' ? 'chatContainer' : 'modal-chat-container';
		const elem = this.document.getElementById(id)
		if(elem) elem.scrollTop = elem.scrollHeight;
		this.initChat++
	}

	sendMensaje() {
		console.log('nuevo mensaje',this.nuevoMensaje);
		
		const trueque = this.trueques.find(item => item.publicacionDTOorigen.idPublicacion == this.publicacion.idPublicacion && item.estadoTrueque == 'APROBADO')
		
		if(trueque && trueque.idTrueque) {
			this.websocketService.sendMessage({
				idTrueque: trueque.idTrueque,
				mensaje: this.nuevoMensaje,
				usuarioReceptor: this.userType == 'publicacionOrigen' ? trueque.publicacionDTOpropuesta.particularDTO.usuarioDTO.idUsuario : trueque.publicacionDTOorigen.particularDTO.usuarioDTO.idUsuario,
				fechaHoraEnvio: new Date()//(new Date()).toISOString().replace('T', ' ').replace('Z', ' ')
			});
		}
		this.nuevoMensaje = '';
		const id = this.origin == 'inline' ? 'chatContainer' : 'modal-chat-container';
		const elem = this.document.getElementById(id)
		if(elem) elem.scrollTop = elem.scrollHeight;
	}

	/* sendMensaje() {
		const trueque = this.trueques.find(item => item.publicacionDTOorigen.idPublicacion == this.publicacion.idPublicacion && item.estadoTrueque == 'APROBADO')
		if(trueque && trueque.idTrueque) {
			this.chatService.sendMensaje({
				idTrueque: trueque.idTrueque,
				mensaje: this.nuevoMensaje,
				usuarioReceptor: this.userType == 'publicacionOrigen' ? trueque.publicacionDTOpropuesta.particularDTO.usuarioDTO.idUsuario : trueque.publicacionDTOorigen.particularDTO.usuarioDTO.idUsuario
			}).subscribe({
				next: (res: any) => {
					console.log('mensaje enviado:', res);
					this.sentMessage.emit(trueque.idTrueque)
					this.chatService.getMyMensajes(trueque.idTrueque).subscribe({
						next: (res: any) => {
							this.mensajes = res;
							this.nuevoMensaje = ''
							if(this.origin == 'modal') {
								this.initChat = 0;
								const elem = document.getElementById('modal-chat-container')
								if(elem) elem.scrollTop = elem.scrollHeight
							}
						}
					})
				}
			})

		}
	} */
}
