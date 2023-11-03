import { DOCUMENT } from '@angular/common';
import { Component, EventEmitter, Inject, Input, Optional, Output } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { TruequeModel } from 'src/app/models/trueque.model';
import { ChatService } from 'src/app/services/chat.service';

@Component({
	selector: 'app-chat',
	templateUrl: './chat.component.html',
	styleUrls: ['./chat.component.scss']
})
export class ChatComponent {
	@Input() mensajes: any[] = [];
	@Input() userData: any;
	@Input() elOtroSwapper: any; 
	trueques: TruequeModel[] = []
	userType: any
	publicacion: any

	nuevoMensaje: string = '';
	origin: 'modal' | 'inline' = 'inline';
	initChat = 0;

	constructor(@Optional() @Inject(MAT_DIALOG_DATA) public data: any, private chatService: ChatService,
	@Inject(DOCUMENT) private document: Document, @Optional() public dialogRef: MatDialogRef<ChatComponent>,) {
		if(data) {
			this.mensajes = data.mensajes;
			this.userData = data.userData;
			this.elOtroSwapper = data.elOtroSwapper;
			this.trueques = data.trueques;
			this.userType = data.userType;
			this.publicacion = data.publicacion;
			this.origin = 'modal'
		}
	}

	scrollToBottom() {
		const id = origin == 'inline' ? 'chatContainer' : 'modal-chat-container';
		const elem = this.document.getElementById(id)
		if(elem) elem.scrollTop = elem.scrollHeight;
		this.initChat++
	}

	sendMensaje() {
		const trueque = this.trueques.find(item => item.publicacionDTOorigen.idPublicacion == this.publicacion.idPublicacion && item.estadoTrueque == 'APROBADO')
		if(trueque && trueque.idTrueque) {
			this.chatService.sendMensaje({
				idTrueque: trueque.idTrueque,
				mensaje: this.nuevoMensaje,
				usuarioReceptor: this.userType == 'publicacionOrigen' ? trueque.publicacionDTOpropuesta.particularDTO.usuarioDTO.idUsuario : trueque.publicacionDTOorigen.particularDTO.usuarioDTO.idUsuario
			}).subscribe({
				next: (res: any) => {
					console.log('mensaje enviado:', res);
					this.chatService.getMyMensajes(trueque.idTrueque).subscribe({
						next: (res: any) => {
							this.mensajes = res;
							this.nuevoMensaje = ''
						}
					})
				}
			})

		}
	}
}
