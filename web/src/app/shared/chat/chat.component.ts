import { Component, EventEmitter, Inject, Input, Optional, Output } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';

@Component({
	selector: 'app-chat',
	templateUrl: './chat.component.html',
	styleUrls: ['./chat.component.scss']
})
export class ChatComponent {
	@Input() mensajes: any[] = [];
	@Input() userData: any;
	@Input() elOtroSwapper: any; 
	/* @Input() trueques: any;
	@Input() userType: any;
	@Input() publicacion: any; */

	nuevoMensaje: string = '';
	origin: 'modal' | 'inline' = 'inline';

	@Output() sendMessageEvent = new EventEmitter<string>();

	constructor(@Optional() @Inject(MAT_DIALOG_DATA) public data: any) {
		if(data) {
			this.mensajes = data.mensajes;
			this.userData = data.userData;
			this.elOtroSwapper = data.elOtroSwapper;
			this.origin = 'modal'
		}
	}

	sendMensaje() {
		console.log(this.nuevoMensaje);
		
		this.sendMessageEvent.emit(this.nuevoMensaje)
		/* const trueque = this.trueques.find(item => item.publicacionDTOorigen.idPublicacion == this.publicacion.idPublicacion && item.estadoTrueque == 'APROBADO')
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

		} */
	}
}
