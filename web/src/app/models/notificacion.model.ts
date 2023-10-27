import { UsuarioModel } from "./usuario.model";

export interface NotificacionModel {
	idNotificacion: number,
	usuario: UsuarioModel,
	titulo: string,
    mensaje: string,
    idReferenciaNotificacion: number,
	estadoNotificacion: 'LEIDO' | 'NO_LEIDO',
	tipoNotificacion: string
}