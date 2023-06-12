export interface SolicitudModel {
	id_solicitud: number,
	id_fundacion: number, // FK
	s_titulo: string,
	s_descripcion: string,
	f_solicitud: Date,
	c_activa: boolean
}