
export interface OpinionModel {
	idOpinion: number,
	descripcion: string,
	valoracion: number,
	usuarioOpina?: number,
	fechaOpinion: Date
}