import { UsuarioModel } from "./usuario.model";

export interface OpinionModel {
	idOpinion: number,
	descripcion: string,
	valoracion: number,
	usuarioOpina: UsuarioModel,
	fechaHoraOpinion: Date
}