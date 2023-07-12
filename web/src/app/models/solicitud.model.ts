import { ProductoModel } from "./producto.model";

export interface SolicitudModel {
	idSolicitud: number,
	titulo: string,
	tituloSolicitud: string,
	descripcion: string,
	cantidadDisponible: number,
	idFundacion: number,
	fundacion: string,
	imagen: string,
	fechaSolicitud: Date,
	productos: ProductoModel[]
	/* id_solicitud: number,
	id_fundacion: number, // FK
	s_titulo: string,
	s_descripcion: string,
	f_solicitud: Date,
	c_activa: boolean,
	s_imagen: string,
	productos: ProductoModel[] */
}